package com.mrcappy.plugins.customfishingrewards.commands;

import com.mrcappy.plugins.customfishingrewards.CustomFishingRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CustomReloadCommand implements CommandExecutor {

    private final CustomFishingRewards plugin;

    public CustomReloadCommand(CustomFishingRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Reload the configuration from file without resetting
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Custom Fishing Rewards configuration has been reloaded!");
        return true;
    }
}
