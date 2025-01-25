package me.mrcappy.auctionhouse.gui

import me.mrcappy.auctionhouse.managers.AuctionManager
import me.mrcappy.auctionhouse.models.AuctionItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.text.SimpleDateFormat
import java.util.*

object AuctionGUI {
    private const val ITEMS_PER_PAGE = 45
    private const val GUI_SIZE = 54
    private const val TITLE_PREFIX = "Auction House - Page "

    /**
     * Open the Auction House GUI.
     */
    fun openAuctionGUI(player: Player, page: Int = 1) {
        val auctions = AuctionManager.getActiveAuctions()
        openFilteredAuctionGUI(player, auctions, page)
    }

    /**
     * Open a filtered auction GUI.
     */
    fun openFilteredAuctionGUI(player: Player, auctions: List<AuctionItem>, page: Int = 1) {
        val startIndex = (page - 1) * ITEMS_PER_PAGE
        val endIndex = (startIndex + ITEMS_PER_PAGE).coerceAtMost(auctions.size)

        val inventory: Inventory = Bukkit.createInventory(null, GUI_SIZE, "$TITLE_PREFIX$page")

        // Populate auctions
        for (i in startIndex until endIndex) {
            val auction = auctions[i]
            inventory.setItem(i - startIndex, createAuctionItem(auction))
        }

        // Navigation buttons
        if (page > 1) inventory.setItem(45, createControlItem(Material.ARROW, "§ePrevious Page"))
        if (endIndex < auctions.size) inventory.setItem(53, createControlItem(Material.ARROW, "§eNext Page"))

        // Filter button
        inventory.setItem(49, createControlItem(Material.HOPPER, "§eFilter Options"))

        player.openInventory(inventory)
    }

    /**
     * Create an auction display item.
     */
    private fun createAuctionItem(auction: AuctionItem): ItemStack {
        val item = auction.item.clone()
        val meta: ItemMeta = item.itemMeta ?: Bukkit.getItemFactory().getItemMeta(Material.STONE)!!

        val sdf = SimpleDateFormat("mm:ss")
        meta.lore = listOf(
            "§7Seller: §e${auction.seller}",
            "§7Starting Price: §a$${auction.startingPrice}",
            "§7Current Bid: §a$${auction.currentBid}",
            "§7Buy Now Price: §a${auction.buyNowPrice?.let { "$$it" } ?: "N/A"}",
            "§7Time Left: §c${sdf.format(Date(auction.endTime - System.currentTimeMillis()))}",
            "§eLeft-click to place a bid!",
            if (auction.buyNowPrice != null) "§eRight-click to buy now!" else "§cBuy Now not available"
        )
        item.itemMeta = meta
        return item
    }

    /**
     * Create a navigation or filter control item.
     */
    private fun createControlItem(material: Material, name: String): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        item.itemMeta = meta
        return item
    }

    /**
     * Handle inventory clicks in the Auction GUI.
     */
    fun handleInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val clickedItem = event.currentItem ?: return
        event.isCancelled = true

        val title = event.view.title
        if (!title.startsWith(TITLE_PREFIX)) return

        val currentPage = title.removePrefix(TITLE_PREFIX).toIntOrNull() ?: 1

        when (clickedItem.type) {
            Material.ARROW -> {
                if (clickedItem.itemMeta?.displayName?.contains("Next Page") == true) {
                    openAuctionGUI(player, currentPage + 1)
                } else if (clickedItem.itemMeta?.displayName?.contains("Previous Page") == true) {
                    openAuctionGUI(player, currentPage - 1)
                }
            }

            Material.HOPPER -> {
                FilterGUI.openFilterGUI(player)
            }

            else -> {
                val auction = AuctionManager.getAuctionFromItem(clickedItem)
                if (auction != null) {
                    if (event.click.isLeftClick) {
                        player.sendMessage("§ePlacing a bid on ${auction.item.type}...")
                        val success = AuctionManager.placeBid(player, auction.id, auction.currentBid + 10) // Example increment
                        if (success) openAuctionGUI(player) // Refresh GUI
                    } else if (event.click.isRightClick && auction.buyNowPrice != null) {
                        player.sendMessage("§eBuying ${auction.item.type} for $${auction.buyNowPrice}...")
                        val success = AuctionManager.buyNow(player, auction.id)
                        if (success) openAuctionGUI(player) // Refresh GUI
                    }
                }
            }
        }
    }
}
