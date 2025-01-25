package com.mrcappy.villageplugin.manager;

import com.mrcappy.villageplugin.villager.VillagerRole;
import com.mrcappy.villageplugin.villager.VillagerTrade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GUIManager implements Listener {

    private final VillagerManager villagerManager;
    private final Map<Player, Inventory> openGUIs = new HashMap<>();

    public GUIManager(VillagerManager villagerManager) {
        this.villagerManager = villagerManager;
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("VillagePlugin"));
    }

    public void openTradeGUI(Player player) {
        VillagerRole role = villagerManager.getRole(player);

        if (role == null) {
            player.sendMessage("§cYou do not have a role assigned!");
            return;
        }

        // Create the GUI
        Inventory gui = Bukkit.createInventory(null, 27, "§8" + roleName(role) + " Trades");

        // Add trades dynamically
        for (VillagerTrade trade : role.getTrades()) {
            gui.addItem(trade.toItemStack());
        }

        openGUIs.put(player, gui);
        player.openInventory(gui);
    }

    private String roleName(VillagerRole role) {
        return role != null ? role.getBuffEffect().getName() : "Unknown";
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Check if the inventory is managed by the plugin
        if (!openGUIs.containsKey(player)) return;

        Inventory gui = openGUIs.get(player);
        if (!event.getInventory().equals(gui)) return;

        event.setCancelled(true); // Prevent item movement

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // Handle trade logic: Check emeralds and give items
        if (player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), 1)) {
            player.getInventory().removeItem(new ItemStack(Material.EMERALD, 1));
            player.getInventory().addItem(clickedItem.clone());
            player.sendMessage("§aSuccessfully traded for: " + clickedItem.getItemMeta().getDisplayName());
        } else {
            player.sendMessage("§cYou need 1 Emerald to complete this trade!");
        }
    }
}
