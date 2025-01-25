package me.mrcappy.auctionhouse.managers

import me.mrcappy.auctionhouse.models.AuctionItem
import org.bukkit.Material

object FilterManager {

    /**
     * Filter auctions by item type.
     */
    fun filterByItemType(auctions: List<AuctionItem>, material: Material): List<AuctionItem> {
        return auctions.filter { it.item.type == material }
    }

    /**
     * Filter auctions by price range.
     */
    fun filterByPriceRange(auctions: List<AuctionItem>, minPrice: Double, maxPrice: Double): List<AuctionItem> {
        return auctions.filter { it.currentBid in minPrice..maxPrice }
    }

    /**
     * Filter auctions by seller name.
     */
    fun filterBySeller(auctions: List<AuctionItem>, seller: String): List<AuctionItem> {
        return auctions.filter { it.seller.equals(seller, ignoreCase = true) }
    }

    /**
     * Search auctions by item name or lore.
     */
    fun searchByKeyword(auctions: List<AuctionItem>, keyword: String): List<AuctionItem> {
        return auctions.filter { auction ->
            val itemName = auction.item.type.name
            val lore = auction.item.itemMeta?.lore ?: emptyList()
            val fullText = listOf(itemName) + lore
            fullText.any { it.contains(keyword, ignoreCase = true) }
        }
    }
}
