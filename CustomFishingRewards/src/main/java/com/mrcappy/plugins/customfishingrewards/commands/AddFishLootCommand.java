package com.mrcappy.plugins.customfishingrewards.commands;

import com.mrcappy.plugins.customfishingrewards.CustomFishingRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddFishLootCommand implements CommandExecutor {

    private final CustomFishingRewards plugin;

    public AddFishLootCommand(CustomFishingRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "This command is not yet implemented.");
        return true;
    }
}
