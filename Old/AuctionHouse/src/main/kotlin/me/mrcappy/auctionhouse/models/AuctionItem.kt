package me.mrcappy.auctionhouse.models

import org.bukkit.inventory.ItemStack
import java.util.UUID

data class AuctionItem(
    val id: UUID,                // Unique ID for the auction
    val seller: String,          // Seller's name
    val item: ItemStack,         // The actual item
    val startingPrice: Double,   // Starting price
    var currentBid: Double,      // Current highest bid
    val buyNowPrice: Double?,    // Optional instant buy price
    val endTime: Long,           // Auction end time
    var highestBidder: String? = null // Name of the highest bidder
)
