package com.mrcappy.plugins.dailyrewards.managers;

import com.mrcappy.plugins.dailyrewards.DailyRewards;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RewardManager {

    private final DailyRewards plugin;
    private final Map<UUID, Long> lastClaimed;
    private final Map<UUID, Integer> streaks;

    public RewardManager(DailyRewards plugin) {
        this.plugin = plugin;
        this.lastClaimed = new HashMap<>();
        this.streaks = new HashMap<>();
    }

    public boolean canClaim(Player player) {
        long now = Instant.now().getEpochSecond();
        return lastClaimed.getOrDefault(player.getUniqueId(), 0L) + plugin.getConfig().getInt("cooldown") <= now;
    }

    public void claimReward(Player player) {
        int streak = getStreak(player);

        ConfigurationSection rewards = plugin.getConfig().getConfigurationSection("rewards");
        if (rewards != null && rewards.contains(String.valueOf(streak))) {
            for (String reward : rewards.getString(String.valueOf(streak)).split(",")) {
                String[] parts = reward.split(":");
                Material material = Material.valueOf(parts[0]);
                int amount = Integer.parseInt(parts[1]);
                player.getInventory().addItem(new ItemStack(material, amount));
            }
        }

        streaks.put(player.getUniqueId(), streak + 1);
        lastClaimed.put(player.getUniqueId(), Instant.now().getEpochSecond());
        player.sendMessage("§aYou have claimed your reward for day " + streak + "!");
    }

    public int getStreak(Player player) {
        long now = Instant.now().getEpochSecond();
        if (now - lastClaimed.getOrDefault(player.getUniqueId(), 0L) > plugin.getConfig().getInt("cooldown")) {
            streaks.put(player.getUniqueId(), 1);
        }
        return streaks.getOrDefault(player.getUniqueId(), 1);
    }
}
