package com.xyz.talecraft.staffmode.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class NoClipManager {
    public static void enableNoClip(Player player) {
        player.setGameMode(GameMode.CREATIVE); // Enable Creative Mode for no-clip functionality
        player.setAllowFlight(true);
        player.setFlying(true);
        player.sendMessage("§aNo-Clip mode enabled.");
    }

    public static void disableNoClip(Player player) {
        player.setGameMode(GameMode.SURVIVAL); // Reset to Survival Mode
        player.setFlying(false);
        player.setAllowFlight(false);
        player.sendMessage("§cNo-Clip mode disabled.");
    }
}
