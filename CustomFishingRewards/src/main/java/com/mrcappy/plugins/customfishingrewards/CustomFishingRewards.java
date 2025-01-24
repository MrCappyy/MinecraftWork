package com.mrcappy.plugins.customfishingrewards;

import com.mrcappy.plugins.customfishingrewards.commands.AddFishLootCommand;
import com.mrcappy.plugins.customfishingrewards.commands.AddLootCommand;
import com.mrcappy.plugins.customfishingrewards.commands.ListLootCommand;
import com.mrcappy.plugins.customfishingrewards.commands.RemoveLootCommand;
import com.mrcappy.plugins.customfishingrewards.commands.FishStatsCommand;
import com.mrcappy.plugins.customfishingrewards.commands.FishReloadCommand;
import com.mrcappy.plugins.customfishingrewards.commands.CustomReloadCommand;
import com.mrcappy.plugins.customfishingrewards.events.FishingEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomFishingRewards extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Custom Fishing Rewards Plugin has been enabled!");
        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        getLogger().info("Custom Fishing Rewards Plugin has been disabled!");
    }

    private void registerCommands() {
        getCommand("fishstats").setExecutor(new FishStatsCommand(this));
        getCommand("fishreload").setExecutor(new FishReloadCommand(this));
        getCommand("customfishreload").setExecutor(new CustomReloadCommand(this));
        getCommand("addfishloot").setExecutor(new AddFishLootCommand(this));
        getCommand("addloot").setExecutor(new AddLootCommand(this));
        getCommand("removeloot").setExecutor(new RemoveLootCommand(this));
        getCommand("listloot").setExecutor(new ListLootCommand(this));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new FishingEventListener(this), this);
    }
}
