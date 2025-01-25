package com.xyz.talecraft.staffmode.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
    private final Map<Player, Long> cooldowns = new HashMap<>();
    private final int cooldownTime; // Cooldown in seconds

    public CooldownManager(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public boolean isOnCooldown(Player player) {
        long currentTime = System.currentTimeMillis();
        return cooldowns.containsKey(player) && (currentTime - cooldowns.get(player)) < (cooldownTime * 1000);
    }

    public void setCooldown(Player player) {
        cooldowns.put(player, System.currentTimeMillis());
    }

    public int getRemainingTime(Player player) {
        long currentTime = System.currentTimeMillis();
        return (int) ((cooldowns.get(player) + (cooldownTime * 1000) - currentTime) / 1000);
    }
}
