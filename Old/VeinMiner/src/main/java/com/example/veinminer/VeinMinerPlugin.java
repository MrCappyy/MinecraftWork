package com.example.veinminer;

import org.bukkit.plugin.java.JavaPlugin;

public class VeinMinerPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new VeinMinerListener(this), this);
        getLogger().info("VeinMiner has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("VeinMiner has been disabled!");
    }
}
