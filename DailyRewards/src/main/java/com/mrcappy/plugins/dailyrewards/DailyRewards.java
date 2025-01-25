package com.mrcappy.plugins.dailyrewards;

import com.mrcappy.plugins.dailyrewards.commands.RewardsCommand;
import com.mrcappy.plugins.dailyrewards.listeners.PlayerJoinListener;
import com.mrcappy.plugins.dailyrewards.listeners.RewardsInventoryClickListener;
import com.mrcappy.plugins.dailyrewards.managers.RewardManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DailyRewards extends JavaPlugin {

    private RewardManager rewardManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        rewardManager = new RewardManager(this);

        getCommand("rewards").setExecutor(new RewardsCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new RewardsInventoryClickListener(this), this);

        getLogger().info("DailyRewards Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DailyRewards Plugin Disabled!");
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }
}
