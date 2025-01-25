package com.mrcappy.prefixtitles.utils;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {
    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public static void setCooldown(UUID uuid, int seconds) {
        cooldowns.put(uuid, System.currentTimeMillis() + (seconds * 1000L));
    }

    public static boolean isOnCooldown(UUID uuid) {
        return cooldowns.containsKey(uuid) && cooldowns.get(uuid) > System.currentTimeMillis();
    }

    public static long getTimeLeft(UUID uuid) {
        if (!isOnCooldown(uuid)) return 0;
        return (cooldowns.get(uuid) - System.currentTimeMillis()) / 1000;
    }

    public static void removeCooldown(UUID uuid) {
        cooldowns.remove(uuid);
    }
}
