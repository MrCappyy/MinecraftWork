package com.mrcappy.plugins.backpackplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UpgradeHandler {

    private final BackpackPlugin plugin;
    private final BackpackManager backpackManager;

    public UpgradeHandler(BackpackPlugin plugin, BackpackManager backpackManager) {
        this.plugin = plugin;
        this.backpackManager = backpackManager;
    }

    public boolean upgradeBackpack(Player player) {
        int currentSize = backpackManager.getBackpack(player).getSize();
        int nextSize = currentSize + 9;

        if (nextSize > plugin.getConfigManager().getMaxBackpackSize()) {
            player.sendMessage(ChatColor.RED + "Your backpack is already at the maximum size!");
            return false;
        }

        int upgradeCost = plugin.getConfigManager().getUpgradeCost(nextSize);

        if (player.getLevel() < upgradeCost) {
            player.sendMessage(ChatColor.RED + "You need " + upgradeCost + " levels to upgrade your backpack!");
            return false;
        }

        // Deduct levels and upgrade the backpack
        player.setLevel(player.getLevel() - upgradeCost);
        backpackManager.upgradeBackpack(player, nextSize);
        player.sendMessage(ChatColor.GREEN + "Your backpack has been upgraded to " + nextSize + " slots!");
        return true;
    }
}
