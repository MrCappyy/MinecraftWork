package com.mrcappy.plugins.customfishingrewards.commands;

import com.mrcappy.plugins.customfishingrewards.CustomFishingRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RemoveLootCommand implements CommandExecutor {

    private final CustomFishingRewards plugin;

    public RemoveLootCommand(CustomFishingRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customfishing.manage")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /removeloot <biome> <item>");
            return true;
        }

        String biome = args[0].toLowerCase();
        String item = args[1].toUpperCase();

        List<String> lootTable = plugin.getConfig().getStringList("biomes." + biome + ".loot");

        boolean removed = lootTable.removeIf(entry -> entry.split(":")[0].equalsIgnoreCase(item));
        if (removed) {
            plugin.getConfig().set("biomes." + biome + ".loot", lootTable);
            plugin.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Removed " + ChatColor.AQUA + item + ChatColor.GREEN + " from the " + ChatColor.AQUA + biome + ChatColor.GREEN + " loot table.");
        } else {
            sender.sendMessage(ChatColor.RED + "The item " + ChatColor.AQUA + item + ChatColor.RED + " was not found in the " + ChatColor.AQUA + biome + ChatColor.RED + " loot table.");
        }
        return true;
    }
}
