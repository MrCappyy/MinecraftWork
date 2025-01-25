package me.fullfortune.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class FullFortune extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        getLogger().info("FullFortune has been enabled!");

        // Load config
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // Register listener and command
        getServer().getPluginManager().registerEvents(new FortuneListener(this), this);
        getCommand("fullfortune").setExecutor(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("FullFortune has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
