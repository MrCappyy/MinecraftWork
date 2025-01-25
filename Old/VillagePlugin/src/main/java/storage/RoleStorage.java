package com.mrcappy.villageplugin.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoleStorage {
    private final File file;
    private final FileConfiguration config;
    private final Map<UUID, com.mrcappy.villageplugin.villager.VillagerRole> roles = new HashMap<>();

    public RoleStorage(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "roles.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        loadData();
    }

    public void setRole(UUID uuid, com.mrcappy.villageplugin.villager.VillagerRole role) {
        roles.put(uuid, role);
    }

    public com.mrcappy.villageplugin.villager.VillagerRole getRole(UUID uuid) {
        return roles.getOrDefault(uuid, null);
    }

    public void saveData() {
        for (Map.Entry<UUID, com.mrcappy.villageplugin.villager.VillagerRole> entry : roles.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue().name());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        for (String key : config.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            com.mrcappy.villageplugin.villager.VillagerRole role = com.mrcappy.villageplugin.villager.VillagerRole.valueOf(config.getString(key));
            roles.put(uuid, role);
        }
    }
}
