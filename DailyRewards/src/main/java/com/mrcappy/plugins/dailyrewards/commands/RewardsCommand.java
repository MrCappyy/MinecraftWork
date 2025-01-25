package com.mrcappy.plugins.dailyrewards.commands;

import com.mrcappy.plugins.dailyrewards.DailyRewards;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class RewardsCommand implements org.bukkit.command.CommandExecutor {

    private final DailyRewards plugin;

    public RewardsCommand(DailyRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        openRewardsGUI(player);
        return true;
    }

    private void openRewardsGUI(Player player) {
        Inventory rewardsInventory = Bukkit.createInventory(null, 27, "Daily Rewards");

        ItemStack claimReward = new ItemStack(Material.CHEST);
        ItemMeta claimMeta = claimReward.getItemMeta();
        claimMeta.setDisplayName("§aClaim Daily Reward");
        claimMeta.setLore(Arrays.asList(
                "§7Click to claim your daily reward!",
                "§7Your streak: §e" + plugin.getRewardManager().getStreak(player)
        ));
        claimReward.setItemMeta(claimMeta);

        ItemStack streakInfo = new ItemStack(Material.PAPER);
        ItemMeta streakMeta = streakInfo.getItemMeta();
        streakMeta.setDisplayName("§bStreak Info");
        streakMeta.setLore(Arrays.asList(
                "§7Current Streak: §e" + plugin.getRewardManager().getStreak(player),
                "§7Next reward: §eBetter loot!"
        ));
        streakInfo.setItemMeta(streakMeta);

        rewardsInventory.setItem(13, claimReward); // Center slot
        rewardsInventory.setItem(22, streakInfo); // Bottom center

        player.openInventory(rewardsInventory);
    }
}
