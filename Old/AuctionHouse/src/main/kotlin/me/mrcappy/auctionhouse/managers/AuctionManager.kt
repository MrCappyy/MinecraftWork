package me.mrcappy.auctionhouse.managers

import me.mrcappy.auctionhouse.Main
import me.mrcappy.auctionhouse.models.AuctionItem
import me.mrcappy.auctionhouse.notifications.NotificationManager
import me.mrcappy.auctionhouse.utils.DatabaseUtils
import me.mrcappy.auctionhouse.utils.ItemSerializer
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.sql.Connection
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.schedule

object AuctionManager {
    private val activeAuctions = ConcurrentHashMap<UUID, AuctionItem>()
    private lateinit var connection: Connection
    private val auctionLogs = mutableListOf<String>()

    private val economy: Economy = Main.instance.server.servicesManager.getRegistration(Economy::class.java)?.provider
        ?: throw IllegalStateException("Vault Economy provider not found!")

    /**
     * Initialize the AuctionManager.
     */
    fun initialize() {
        connection = DatabaseUtils.getConnection()
        loadAuctionsFromDatabase()
        startAuctionTimers()
    }

    /**
     * Shutdown the AuctionManager.
     */
    fun shutdown() {
        saveAuctionsToDatabase()
    }

    /**
     * List an item for auction.
     */
    fun listAuction(player: Player, item: ItemStack, startingPrice: Double, buyNowPrice: Double?, duration: Long): AuctionItem {
        val auction = AuctionItem(
            id = UUID.randomUUID(),
            seller = player.name,
            item = item.clone(),
            startingPrice = startingPrice,
            currentBid = startingPrice,
            buyNowPrice = buyNowPrice,
            endTime = System.currentTimeMillis() + duration
        )

        // Deduct listing fee
        val listingFee = Main.instance.config.getDouble("listing_fee")
        if (economy.getBalance(player) < listingFee) {
            player.sendMessage("§cYou do not have enough money to list this item. Listing fee: $$listingFee.")
            throw IllegalArgumentException("Insufficient balance")
        }

        economy.withdrawPlayer(player, listingFee)
        activeAuctions[auction.id] = auction
        saveAuctionToDatabase(auction)

        Bukkit.getLogger().info("Auction listed: ${item.type} by ${player.name} starting at $$startingPrice.")
        return auction
    }

    /**
     * Place a bid on an auction.
     */
    fun placeBid(player: Player, auctionId: UUID, bidAmount: Double): Boolean {
        val auction = activeAuctions[auctionId] ?: return false

        if (player.name == auction.seller) {
            player.sendMessage("§cYou cannot bid on your own auction.")
            return false
        }

        if (bidAmount <= auction.currentBid) {
            player.sendMessage("§cYour bid must be higher than the current bid of $${auction.currentBid}.")
            return false
        }

        val balance = economy.getBalance(player)
        if (balance < bidAmount) {
            player.sendMessage("§cYou do not have enough money to place this bid.")
            return false
        }

        // Refund previous bidder
        auction.highestBidder?.let { previousBidder ->
            val previousPlayer = Bukkit.getPlayer(previousBidder)
            if (previousPlayer != null) {
                economy.depositPlayer(previousPlayer, auction.currentBid)
                NotificationManager.notifyOutbid(previousBidder, auction.item.type.name, bidAmount)
            }
        }

        // Deduct new bid amount
        economy.withdrawPlayer(player, bidAmount)
        auction.currentBid = bidAmount
        auction.highestBidder = player.name
        updateAuctionInDatabase(auction)

        player.sendMessage("§aYour bid of $$bidAmount has been placed on ${auction.item.type}.")
        NotificationManager.notifyPlayer(
            auction.seller,
            "§aA new bid of §f$$bidAmount §ahas been placed on your auction for §f${auction.item.type.name}§a."
        )

        return true
    }

    /**
     * Buy an item immediately at the "Buy Now" price.
     */
    fun buyNow(player: Player, auctionId: UUID): Boolean {
        val auction = activeAuctions[auctionId] ?: return false

        if (auction.buyNowPrice == null) {
            player.sendMessage("§cThis item does not have a 'Buy Now' option.")
            return false
        }

        val balance = economy.getBalance(player)
        if (balance < auction.buyNowPrice) {
            player.sendMessage("§cYou do not have enough money to buy this item.")
            return false
        }

        economy.withdrawPlayer(player, auction.buyNowPrice)
        val seller = Bukkit.getOfflinePlayer(auction.seller)
        economy.depositPlayer(seller, auction.buyNowPrice)

        // Transfer the item to the buyer
        activeAuctions.remove(auctionId)
        player.inventory.addItem(auction.item)
        player.sendMessage("§aYou bought ${auction.item.type} for $${auction.buyNowPrice}.")
        NotificationManager.notifySellerSold(auction.seller, auction.item.type.name, auction.buyNowPrice, player.name)

        return true
    }

    /**
     * End an auction.
     */
    fun endAuction(auction: AuctionItem) {
        activeAuctions.remove(auction.id)

        if (auction.highestBidder != null) {
            val winner = Bukkit.getPlayer(auction.highestBidder!!)
            winner?.inventory?.addItem(auction.item)
            NotificationManager.notifyWinner(auction.highestBidder!!, auction.item.type.name, auction.currentBid)
            NotificationManager.notifySellerSold(
                auction.seller,
                auction.item.type.name,
                auction.currentBid,
                auction.highestBidder!!
            )
        } else {
            val seller = Bukkit.getPlayer(auction.seller)
            seller?.inventory?.addItem(auction.item)
            NotificationManager.notifyUnsold(auction.seller, auction.item.type.name)
        }

        deleteAuctionFromDatabase(auction.id)
    }

    /**
     * Forcefully remove an auction.
     */
    fun forceRemoveAuction(auctionId: UUID) {
        val auction = activeAuctions.remove(auctionId) ?: return
        Bukkit.getPlayer(auction.seller)?.inventory?.addItem(auction.item)
        logEvent("Auction removed: ${auction.item.type} by ${auction.seller}")
    }

    /**
     * Forcefully sell an auction item.
     */
    fun forceSellAuction(auctionId: UUID) {
        val auction = activeAuctions.remove(auctionId) ?: return
        val seller = Bukkit.getOfflinePlayer(auction.seller)
        economy.depositPlayer(seller, auction.currentBid)
        logEvent("Auction forcibly sold: ${auction.item.type} by ${auction.seller}")
    }

    /**
     * Log auction events.
     */
    private fun logEvent(message: String) {
        auctionLogs.add(message)
        if (auctionLogs.size > 50) auctionLogs.removeFirst()
        Bukkit.getLogger().info("[AuctionHouse] $message")
    }

    /**
     * Save all auctions before shutdown.
     */
    private fun saveAuctionsToDatabase() {
        activeAuctions.values.forEach { saveAuctionToDatabase(it) }
    }

    /**
     * Load auctions from the database.
     */
    private fun loadAuctionsFromDatabase() {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM auctions")
        while (resultSet.next()) {
            val id = UUID.fromString(resultSet.getString("id"))
            val seller = resultSet.getString("seller")
            val item = ItemSerializer.deserialize(resultSet.getBytes("item"))
            val startingPrice = resultSet.getDouble("starting_price")
            val currentBid = resultSet.getDouble("current_bid")
            val buyNowPrice = resultSet.getDouble("buy_now_price")
            val endTime = resultSet.getLong("end_time")

            activeAuctions[id] = AuctionItem(
                id = id,
                seller = seller,
                item = item,
                startingPrice = startingPrice,
                currentBid = currentBid,
                buyNowPrice = if (buyNowPrice > 0) buyNowPrice else null,
                endTime = endTime
            )
        }
        statement.close()
    }
}
