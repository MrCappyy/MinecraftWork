package me.fullfortune.plugin;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final FullFortune plugin;

    public ConfigManager(FullFortune plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        plugin.getLogger().info("Configuration has been reloaded.");
    }
}
