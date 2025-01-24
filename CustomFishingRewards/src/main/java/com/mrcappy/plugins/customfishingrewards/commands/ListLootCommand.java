package com.mrcappy.plugins.customfishingrewards.commands;

import com.mrcappy.plugins.customfishingrewards.CustomFishingRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListLootCommand implements CommandExecutor {

    private final CustomFishingRewards plugin;

    public ListLootCommand(CustomFishingRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customfishing.manage")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /listloot <biome>");
            return true;
        }

        String biome = args[0].toLowerCase();
        List<String> lootTable = plugin.getConfig().getStringList("biomes." + biome + ".loot");

        if (lootTable.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No loot is configured for the biome " + ChatColor.AQUA + biome + ChatColor.RED + ".");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "Loot table for biome: " + ChatColor.AQUA + biome);
        for (String loot : lootTable) {
            sender.sendMessage(ChatColor.YELLOW + "- " + loot);
        }
        return true;
    }
}
