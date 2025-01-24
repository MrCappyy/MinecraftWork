package com.mrcappy.plugins.customfishingrewards.commands;

import com.mrcappy.plugins.customfishingrewards.CustomFishingRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FishStatsCommand implements CommandExecutor {

    private final CustomFishingRewards plugin;

    public FishStatsCommand(CustomFishingRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        int totalCatches = plugin.getConfig().getInt("stats." + player.getUniqueId() + ".totalCatches", 0);
        int rareCatches = plugin.getConfig().getInt("stats." + player.getUniqueId() + ".rareCatches", 0);

        player.sendMessage(ChatColor.GOLD + "Your Fishing Stats:");
        player.sendMessage(ChatColor.AQUA + "Total Catches: " + ChatColor.GREEN + totalCatches);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Rare Catches: " + ChatColor.GREEN + rareCatches);
        return true;
    }
}
