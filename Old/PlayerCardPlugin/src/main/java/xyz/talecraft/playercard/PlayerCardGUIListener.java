package xyz.talecraft.playercard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PlayerCardGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the inventory title contains "Player Card"
        if (event.getView().getTitle().equals(ChatColor.GOLD + "Player Card")) {
            event.setCancelled(true); // Prevent interactions in the GUI

            // Provide feedback to the player
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(ChatColor.RED + "You cannot edit items in the Player Card GUI.");
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Optional: Handle any cleanup or notifications when the GUI is closed
        if (event.getView().getTitle().equals(ChatColor.GOLD + "Player Card")) {
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();
                player.sendMessage(ChatColor.YELLOW + "You have closed your Player Card.");
            }
        }
    }
}
