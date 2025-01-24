package com.mrcappy.plugins.backpackplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackpackManager {

    private final Map<UUID, Inventory> backpacks = new HashMap<>();
    private final BackpackPlugin plugin;

    public BackpackManager(BackpackPlugin plugin) {
        this.plugin = plugin;
        loadBackpacks();
    }

    public Inventory getBackpack(Player player) {
        return backpacks.computeIfAbsent(player.getUniqueId(), id -> createBackpack(plugin.getConfigManager().getDefaultBackpackSize()));
    }

    public void saveAllBackpacks() {
        for (Map.Entry<UUID, Inventory> entry : backpacks.entrySet()) {
            saveBackpack(entry.getKey(), entry.getValue());
        }
    }

    public Inventory createBackpack(int size) {
        return Bukkit.createInventory(null, size, "Backpack");
    }

    public void saveBackpack(UUID playerId, Inventory backpack) {
        // TODO: Implement file-based or database-based persistence logic
        // Example:
        // Save inventory contents to a file associated with playerId
    }

    public Inventory loadBackpack(UUID playerId) {
        // TODO: Implement logic to load inventory from file or database
        // Example:
        // Load inventory contents from a file associated with playerId
        return createBackpack(plugin.getConfigManager().getDefaultBackpackSize());
    }

    private void loadBackpacks() {
        // TODO: Load all backpacks from file or database during server startup
    }

    public void upgradeBackpack(Player player, int newSize) {
        UUID playerId = player.getUniqueId();
        Inventory upgradedBackpack = Bukkit.createInventory(null, newSize, "Backpack");

        // Copy items from the old backpack
        Inventory currentBackpack = backpacks.get(playerId);
        if (currentBackpack != null) {
            for (int i = 0; i < currentBackpack.getSize(); i++) {
                if (currentBackpack.getItem(i) != null) {
                    upgradedBackpack.setItem(i, currentBackpack.getItem(i));
                }
            }
        }

        backpacks.put(playerId, upgradedBackpack);
    }
}
