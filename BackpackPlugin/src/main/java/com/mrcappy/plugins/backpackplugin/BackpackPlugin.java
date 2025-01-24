package com.mrcappy.plugins.backpackplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackPlugin extends JavaPlugin {

    private static BackpackPlugin instance;
    private BackpackManager backpackManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize managers
        configManager = new ConfigManager(this);
        backpackManager = new BackpackManager(this);

        // Register commands and events
        getCommand("backpack").setExecutor(new BackpackCommand(backpackManager));
        Bukkit.getPluginManager().registerEvents(new BackpackListener(backpackManager), this);

        getLogger().info("BackpackPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        backpackManager.saveAllBackpacks();
        getLogger().info("BackpackPlugin has been disabled!");
    }

    public static BackpackPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
