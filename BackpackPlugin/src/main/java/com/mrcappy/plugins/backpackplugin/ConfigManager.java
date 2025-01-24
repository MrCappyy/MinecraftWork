package com.mrcappy.plugins.backpackplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public int getDefaultBackpackSize() {
        return config.getInt("default-backpack-size", 9);
    }

    public int getMaxBackpackSize() {
        return config.getInt("max-backpack-size", 54);
    }

    public int getUpgradeCost(int size) {
        return config.getInt("upgrade-cost." + size, 0);
    }
}
