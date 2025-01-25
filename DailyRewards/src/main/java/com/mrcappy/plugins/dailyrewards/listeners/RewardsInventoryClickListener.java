package com.mrcappy.plugins.dailyrewards.listeners;

import com.mrcappy.plugins.dailyrewards.DailyRewards;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RewardsInventoryClickListener implements Listener {

    private final DailyRewards plugin;

    public RewardsInventoryClickListener(DailyRewards plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Daily Rewards")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            Player player = (Player) event.getWhoClicked();

            if (clickedItem.getType() == Material.CHEST) {
                if (plugin.getRewardManager().canClaim(player)) {
                    plugin.getRewardManager().claimReward(player);
                    player.closeInventory();
                } else {
                    player.sendMessage("§cYou cannot claim your reward yet. Try again later.");
                }
            }
        }
    }
}
