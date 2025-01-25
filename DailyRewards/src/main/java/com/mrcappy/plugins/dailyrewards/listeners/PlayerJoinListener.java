package com.mrcappy.plugins.dailyrewards.listeners;

import com.mrcappy.plugins.dailyrewards.DailyRewards;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final DailyRewards plugin;

    public PlayerJoinListener(DailyRewards plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("§aWelcome! Don't forget to claim your daily reward with /dailyrewards!");
    }
}
