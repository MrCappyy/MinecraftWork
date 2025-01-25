package me.mrcappy.auctionhouse.notifications

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object NotificationManager {

    /**
     * Notify a specific player with a custom message.
     */
    fun notifyPlayer(playerName: String, message: String) {
        val player = Bukkit.getPlayer(playerName)
        if (player != null && player.isOnline) {
            player.sendMessage("§a[Auction House] §f$message")
        }
    }

    /**
     * Notify the previous bidder about being outbid.
     */
    fun notifyOutbid(previousBidder: String, itemName: String, newBid: Double) {
        notifyPlayer(previousBidder, "§eYou have been outbid on §a$itemName§e. The new highest bid is §a$$newBid.")
    }

    /**
     * Notify the seller that their item has been sold.
     */
    fun notifySellerSold(seller: String, itemName: String, price: Double, buyer: String) {
        notifyPlayer(seller, "§aYour auction for §f$itemName §ahas been sold to §f$buyer §afor §f$$price§a.")
    }

    /**
     * Notify the winner of the auction.
     */
    fun notifyWinner(winner: String, itemName: String, bidAmount: Double) {
        notifyPlayer(winner, "§aCongratulations! You won the auction for §f$itemName §awith a bid of §f$$bidAmount§a.")
    }

    /**
     * Notify the seller about an unsold item.
     */
    fun notifyUnsold(seller: String, itemName: String) {
        notifyPlayer(seller, "§cYour auction for §f$itemName §cended without any bids. The item has been returned to your inventory.")
    }
}
