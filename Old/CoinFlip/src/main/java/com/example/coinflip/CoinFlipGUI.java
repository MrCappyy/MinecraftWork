package com.example.coinflip;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CoinFlipGUI implements Listener {
    public static void openCoinFlipGUI(Player player1, Player player2, double betAmount) {
        Inventory gui = Bukkit.createInventory(null, 9, "Accept Coin Flip?");

        ItemStack accept = new ItemStack(Material.GREEN_WOOL);
        ItemMeta acceptMeta = accept.getItemMeta();
        acceptMeta.setDisplayName("Accept");
        accept.setItemMeta(acceptMeta);

        ItemStack deny = new ItemStack(Material.RED_WOOL);
        ItemMeta denyMeta = deny.getItemMeta();
        denyMeta.setDisplayName("Deny");
        deny.setItemMeta(denyMeta);

        gui.setItem(3, accept);
        gui.setItem(5, deny);

        player2.openInventory(gui);
    }

    public void handleInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
