package com.mrcappy.villageplugin.manager;

import com.mrcappy.villageplugin.config.ConfigManager;
import com.mrcappy.villageplugin.storage.RoleStorage;
import com.mrcappy.villageplugin.villager.VillagerRole;
import org.bukkit.entity.Player;

public class VillagerManager {

    private final RoleStorage roleStorage;

    public VillagerManager(RoleStorage roleStorage) {
        this.roleStorage = roleStorage;
    }

    public void setRole(Player player, String roleName) {
        VillagerRole role = ConfigManager.getRole(roleName);
        if (role == null) {
            player.sendMessage("§cInvalid role: " + roleName);
            return;
        }

        roleStorage.saveRole(player.getUniqueId(), roleName);
        player.sendMessage("§aYour role has been set to §e" + roleName);
    }

    public VillagerRole getRole(Player player) {
        String roleName = roleStorage.getRole(player.getUniqueId());
        if (roleName != null) {
            return ConfigManager.getRole(roleName);
        }
        return null; // No role assigned
    }
}
