package me.mrcappy.auctionhouse.listeners

import me.mrcappy.auctionhouse.gui.AuctionGUI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class AuctionListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.view.title
        if (inventory.startsWith("Auction House")) {
            AuctionGUI.handleInventoryClick(event)
        }
    }
}
