package com.mrcappy.prefixtitles;

import com.mrcappy.prefixtitles.commands.AdminCommand;
import com.mrcappy.prefixtitles.commands.PrefixCommand;
import com.mrcappy.prefixtitles.gui.PrefixGUI;
import com.mrcappy.prefixtitles.utils.PlaceholderManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PrefixTitles extends JavaPlugin implements Listener {
    private static PrefixTitles instance;
    private File customFile;
    private FileConfiguration customConfig;

    @Override
    public void onEnable() {
        instance = this;

        // Load default config
        saveDefaultConfig();

        // Setup custom configuration file
        setupCustomConfig();

        // Register commands
        getCommand("prefix").setExecutor(new PrefixCommand());
        getCommand("prefixadmin").setExecutor(new AdminCommand());

        // Register GUI events and join listener
        getServer().getPluginManager().registerEvents(new PrefixGUI(), this);
        getServer().getPluginManager().registerEvents(this, this);

        // Register PlaceholderAPI
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlaceholderManager.register(this);
            getLogger().info("PlaceholderAPI found! Placeholders registered.");
        } else {
            getLogger().warning("PlaceholderAPI not found! Placeholders will not work.");
        }

        getLogger().info("PrefixTitles enabled!");
    }

    @Override
    public void onDisable() {
        saveCustomConfig();
        getLogger().info("PrefixTitles disabled!");
    }

    public static PrefixTitles getInstance() {
        return instance;
    }

    public FileConfiguration getCustomConfig() {
        return customConfig;
    }

    public void saveCustomConfig() {
        try {
            customConfig.save(customFile);
        } catch (IOException e) {
            getLogger().severe("Could not save custom.yml!");
            e.printStackTrace();
        }
    }

    private void setupCustomConfig() {
        customFile = new File(getDataFolder(), "custom.yml");
        if (!customFile.exists()) {
            saveResource("custom.yml", false);
        }
        customConfig = YamlConfiguration.loadConfiguration(customFile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Check if it's the player's first join
        if (!player.hasPlayedBefore()) {
            String defaultTitle = ChatColor.translateAlternateColorCodes('&', getConfig().getString("default-title", "&9Bard"));

            // Set the player's metadata for the title
            player.setMetadata("prefix_title", new org.bukkit.metadata.FixedMetadataValue(this, defaultTitle));

            // Send message to the player
            player.sendMessage(ChatColor.GREEN + "Welcome! Your default title has been set to: " + defaultTitle);
            getLogger().info("Default title '" + defaultTitle + "' assigned to " + player.getName());
        }
    }
}
