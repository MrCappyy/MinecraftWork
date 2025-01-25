package com.mrcappy.villageplugin.storage;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RoleStorage {

    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration config;

    public RoleStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "roles.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create roles.yml file!");
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    // Save a player's role to roles.yml
    public void saveRole(UUID playerUUID, String roleName) {
        config.set("players." + playerUUID.toString(), roleName);
        saveConfig();
    }

    // Retrieve a player's role from roles.yml
    public String getRole(UUID playerUUID) {
        return config.getString("players." + playerUUID.toString(), null);
    }

    // Save changes to roles.yml
    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save roles.yml file!");
            e.printStackTrace();
        }
    }

    // Save all data (called onDisable)
    public void saveData() {
        saveConfig();
    }
}
