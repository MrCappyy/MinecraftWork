package com.xyz.talecraft.staffmode.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishManager {
    public static void vanish(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.hasPermission("staffmode.seevanished")) {
                other.hidePlayer(player);
            }
        }
        player.sendMessage("§aYou are now vanished.");
    }

    public static void unvanish(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            other.showPlayer(player);
        }
        player.sendMessage("§cYou are no longer vanished.");
    }
}
