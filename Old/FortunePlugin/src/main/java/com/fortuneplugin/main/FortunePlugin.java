

package com.fortuneplugin.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FortuneEnhancer extends JavaPlugin implements Listener {
    private FileConfiguration config;
    private final Random random = new Random();

    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();
        config = getConfig();

        // Register event listener
        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("FortuneEnhancer has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("FortuneEnhancer has been disabled!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Material blockType = event.getBlock().getType();

        // Check if the tool has Fortune
        if (!tool.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            return;
        }

        // Check if the block is in the config
        List<String> fortuneBlocks = config.getStringList("fortune-blocks");
        if (!fortuneBlocks.contains(blockType.toString())) {
            return;
        }

        // Cancel default block drop
        event.setDropItems(false);

        // Get Fortune level
        int fortuneLevel = tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        int customMultiplier = config.getInt("fortune-multiplier", 2);

        int dropAmount = calculateDrops(fortuneLevel * customMultiplier);

        // Drop items directly into the player's inventory
        ItemStack drop = new ItemStack(blockType, dropAmount);
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(drop);

        // Drop any leftover items on the ground
        leftovers.values().forEach(item -> player.getWorld().dropItemNaturally(event.getBlock().getLocation(), item));
    }

    private int calculateDrops(int fortuneLevel) {
        int drops = 1; // Default drop
        for (int i = 0; i < fortuneLevel; i++) {
            if (random.nextDouble() < 0.5) {
                drops++;
            }
        }
        return drops;
    }

    @Override
    public void saveDefaultConfig() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            super.saveDefaultConfig();
        }
    }
}
