package me.mrcappy.auctionhouse.gui

import me.mrcappy.auctionhouse.managers.AuctionManager
import me.mrcappy.auctionhouse.managers.FilterManager
import me.mrcappy.auctionhouse.models.AuctionItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object FilterGUI {

    private const val GUI_SIZE = 27
    private const val TITLE = "Filter Auctions"

    /**
     * Open the filter GUI for a player.
     */
    fun openFilterGUI(player: Player) {
        val inventory: Inventory = Bukkit.createInventory(null, GUI_SIZE, TITLE)

        inventory.setItem(10, createFilterItem(Material.DIAMOND_SWORD, "§eWeapons"))
        inventory.setItem(11, createFilterItem(Material.IRON_PICKAXE, "§eTools"))
        inventory.setItem(12, createFilterItem(Material.GRASS_BLOCK, "§eBlocks"))
        inventory.setItem(13, createFilterItem(Material.EMERALD, "§ePrice Range"))
        inventory.setItem(14, createFilterItem(Material.PAPER, "§eSearch by Keyword"))
        inventory.setItem(15, createFilterItem(Material.NAME_TAG, "§eFilter by Seller"))

        player.openInventory(inventory)
    }

    /**
     * Create a filter item for the GUI.
     */
    private fun createFilterItem(material: Material, name: String): ItemStack {
        val item = ItemStack(material)
        val meta: ItemMeta = item.itemMeta!!
        meta.setDisplayName(name)
        item.itemMeta = meta
        return item
    }

    /**
     * Handle clicks in the filter GUI.
     */
    fun handleFilterClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val clickedItem = event.currentItem ?: return
        event.isCancelled = true

        when (clickedItem.type) {
            Material.DIAMOND_SWORD -> {
                val filtered = FilterManager.filterByItemType(AuctionManager.getActiveAuctions(), Material.DIAMOND_SWORD)
                AuctionGUI.openFilteredAuctionGUI(player, filtered)
            }

            Material.IRON_PICKAXE -> {
                val filtered = FilterManager.filterByItemType(AuctionManager.getActiveAuctions(), Material.IRON_PICKAXE)
                AuctionGUI.openFilteredAuctionGUI(player, filtered)
            }

            Material.GRASS_BLOCK -> {
                val filtered = FilterManager.filterByItemType(AuctionManager.getActiveAuctions(), Material.GRASS_BLOCK)
                AuctionGUI.openFilteredAuctionGUI(player, filtered)
            }

            Material.EMERALD -> {
                // Example hardcoded price range
                val filtered = FilterManager.filterByPriceRange(AuctionManager.getActiveAuctions(), 10.0, 100.0)
                AuctionGUI.openFilteredAuctionGUI(player, filtered)
            }

            Material.PAPER -> {
                player.sendMessage("§eUse /ah search <keyword> to search auctions.")
            }

            Material.NAME_TAG -> {
                player.sendMessage("§eUse /ah seller <name> to filter auctions by seller.")
            }

            else -> player.sendMessage("§cInvalid filter option.")
        }
    }
}
