package com.mrcappy.plugins.customfishingrewards.events;

import com.mrcappy.plugins.customfishingrewards.CustomFishingRewards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class FishingEventListener implements Listener {

    private final CustomFishingRewards plugin;
    private final Random random = new Random();

    public FishingEventListener(CustomFishingRewards plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        Player player = event.getPlayer();
        Location location = player.getLocation();
        Biome biome = location.getBlock().getBiome();
        List<String> lootTable = plugin.getConfig().getStringList("biomes." + biome.name().toLowerCase() + ".loot");

        if (lootTable.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No fishing rewards are configured for this biome.");
            return;
        }

        for (String loot : lootTable) {
            String[] parts = loot.split(":");
            if (parts.length != 3) {
                Bukkit.getLogger().warning("Invalid loot format in config: " + loot);
                continue;
            }

            String itemName = parts[0];
            int chance = Integer.parseInt(parts[1]);
            String rarity = parts[2];

            if (random.nextInt(100) < chance) {
                Material material = Material.getMaterial(itemName.toUpperCase());
                if (material != null) {
                    ItemStack reward = new ItemStack(material);
                    player.getInventory().addItem(reward);
                    player.sendMessage(ChatColor.GOLD + "You caught a " + ChatColor.AQUA + rarity + ChatColor.GOLD + " item: " + itemName + "!");
                    break;
                }
            }
        }
    }
}
