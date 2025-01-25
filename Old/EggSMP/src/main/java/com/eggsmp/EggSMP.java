package com.eggsmp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EggSMP extends JavaPlugin implements Listener {

    // Called when the plugin is enabled
    public void onEnable() {
        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Egg SMP Plugin has been enabled!");
    }

    // Called when the plugin is disabled
    public void onDisable() {
        getLogger().info("Egg SMP Plugin has been disabled!");
    }

    // Event: Handle Ender Dragon death
    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            // Drop the dragon head
            ItemStack dragonHead = new ItemStack(Material.DRAGON_HEAD);
            event.getDrops().add(dragonHead);
            Bukkit.broadcastMessage(ChatColor.GOLD + "The Ender Dragon has been defeated! Its head has dropped.");
        }
    }

    // Event: Announce when the Dragon Egg is placed
    @EventHandler
    public void onEggPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.DRAGON_EGG) {
            Player player = event.getPlayer();
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + player.getName() + " has placed the Dragon Egg!");
        }
    }

    // Event: Announce when the Dragon Egg is picked up
    @EventHandler
    public void onEggPickup(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack().getType() == Material.DRAGON_EGG) {
            Player player = event.getPlayer();
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " has picked up the Dragon Egg!");
        }
    }

    // Command handler: /dragoneggreset
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("dragoneggreset")) {
            Bukkit.broadcastMessage(ChatColor.RED + "The Dragon Egg has been reset!");
            return true; // Indicate command was handled
        }

        return false; // Indicate command was not recognized
    }
}
