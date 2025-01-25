package com.xyz.talecraft.staffmode;

import com.xyz.talecraft.staffmode.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffModePlugin extends JavaPlugin {
    private static StaffModePlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        // Register the listener
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getLogger().info("TaleCraftStaffMode has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TaleCraftStaffMode has been disabled!");
    }

    public static StaffModePlugin getInstance() {
        return instance;
    }
}
