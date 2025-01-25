package com.mrcappy.safertp;

import com.mrcappy.safertp.commands.RTPCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SafeRTP extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("SafeRTP plugin enabled!");

        // Register command
        getCommand("rtp").setExecutor(new RTPCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("SafeRTP plugin disabled!");
    }
}
