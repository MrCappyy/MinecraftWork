package com.mrcappy.plugins.customfishingrewards.commands;

import com.mrcappy.plugins.customfishingrewards.CustomFishingRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AddLootCommand implements CommandExecutor {

    private final CustomFishingRewards plugin;

    public AddLootCommand(CustomFishingRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customfishing.manage")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /addloot <biome> <item> <chance> <rarity>");
            return true;
        }

        String biome = args[0].toLowerCase();
        String item = args[1].toUpperCase();
        String chance = args[2];
        String rarity = args[3].toUpperCase();

        try {
            int chanceValue = Integer.parseInt(chance);
            if (chanceValue < 0 || chanceValue > 100) {
                sender.sendMessage(ChatColor.RED + "The chance must be between 0 and 100.");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "The chance must be a valid number.");
            return true;
        }

        String lootEntry = item + ":" + chance + ":" + rarity;
        List<String> lootTable = plugin.getConfig().getStringList("biomes." + biome + ".loot");
        lootTable.add(lootEntry);
        plugin.getConfig().set("biomes." + biome + ".loot", lootTable);
        plugin.saveConfig();

        sender.sendMessage(ChatColor.GREEN + "Added " + ChatColor.AQUA + item + ChatColor.GREEN + " with a chance of " + ChatColor.AQUA + chance + "% " +
                "and rarity " + ChatColor.AQUA + rarity + ChatColor.GREEN + " to the " + ChatColor.AQUA + biome + ChatColor.GREEN + " loot table.");
        return true;
    }
}
