package com.mrcappy.plugins.backpackplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BackpackListener implements Listener {

    private final BackpackManager backpackManager;

    public BackpackListener(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Logic for saving the inventory contents
        backpackManager.saveBackpack(event.getPlayer().getUniqueId(), event.getInventory());
    }
}
