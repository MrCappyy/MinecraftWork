package com.xyz.talecraft.staffmode.listeners;

import com.xyz.talecraft.staffmode.StaffModePlugin;
import com.xyz.talecraft.staffmode.utils.CooldownManager;
import com.xyz.talecraft.staffmode.utils.LoggerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {
    private final CooldownManager cooldownManager;

    public PlayerListener() {
        this.cooldownManager = new CooldownManager(5); // Cooldown time in seconds
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the player is holding the teleportation compass
        if (item != null && item.getType() == Material.COMPASS && item.getItemMeta() != null
                && item.getItemMeta().getDisplayName().equals("§bTeleportation Compass")) {

            // Check cooldown
            if (cooldownManager.isOnCooldown(player)) {
                int remaining = cooldownManager.getRemainingTime(player);
                player.sendMessage("§cTeleport Compass is on cooldown. Try again in " + remaining + " seconds.");
                return;
            }

            openTeleportGUI(player);
            cooldownManager.setCooldown(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Check if the inventory is the teleport GUI
        if (!event.getView().getTitle().equals("§bPlayer Teleport Menu")) return;

        event.setCancelled(true); // Prevent item moving

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        // Extract target player's name
        String targetName = clickedItem.getItemMeta().getDisplayName().replace("§e", "");
        Player target = Bukkit.getPlayerExact(targetName);

        if (target != null) {
            player.teleport(target);
            player.sendMessage("§aTeleported to §e" + target.getName() + "§a!");
            LoggerUtil.logAction(player, "teleported to " + target.getName() + ".");
        } else {
            player.sendMessage("§cCould not find that player. They may have gone offline.");
        }
    }

    private void openTeleportGUI(Player player) {
        // Create GUI size based on the number of players
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        int size = ((onlinePlayers / 9) + 1) * 9; // Round up to the nearest row
        Inventory gui = Bukkit.createInventory(null, size, "§bPlayer Teleport Menu");

        // Add all online players to the GUI
        for (Player target : Bukkit.getOnlinePlayers()) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = playerHead.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§e" + target.getName());
                playerHead.setItemMeta(meta);
            }
            gui.addItem(playerHead);
        }

        player.openInventory(gui);
    }
}
