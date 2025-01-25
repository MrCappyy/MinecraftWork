package com.mrcappy.safertp.gui;

import com.mrcappy.safertp.SafeRTP;
import com.mrcappy.safertp.utils.SafeTeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RTPGui implements Listener {

    private final SafeRTP plugin;
    private final Player player;
    private final Inventory gui;

    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final HashMap<UUID, Integer> teleportTasks = new HashMap<>();

    public RTPGui(SafeRTP plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        // Fetch GUI title and rows from config
        String title = plugin.getConfig().getString("inventory.title", "&8Random Teleport").replace("&", "§");
        int rows = plugin.getConfig().getInt("inventory.rows", 3) * 9;

        this.gui = Bukkit.createInventory(null, rows, title);
        setupItems();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void setupItems() {
        ConfigurationSection worlds = plugin.getConfig().getConfigurationSection("worlds");
        if (worlds != null) {
            for (String key : worlds.getKeys(false)) {
                ConfigurationSection section = worlds.getConfigurationSection(key);
                if (section == null) continue;

                String name = section.getString("name", "World").replace("&", "§");
                Material material = Material.valueOf(section.getString("material", "STONE"));
                int slot = section.getInt("slot", 0);
                List<String> lore = section.getStringList("lore");

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(name);
                    for (int i = 0; i < lore.size(); i++) {
                        lore.set(i, lore.get(i).replace("&", "§"));
                    }
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                gui.setItem(slot, item);
            }
        }
    }

    public void open() {
        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(gui)) return;

        event.setCancelled(true); // Prevent item movement
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String displayName = clicked.getItemMeta().getDisplayName().replace("§", "");
        String worldKey = null;

        ConfigurationSection worlds = plugin.getConfig().getConfigurationSection("worlds");
        if (worlds != null) {
            for (String key : worlds.getKeys(false)) {
                String name = worlds.getConfigurationSection(key).getString("name", "").replace("&", "");
                if (displayName.contains(name)) {
                    worldKey = key;
                    break;
                }
            }
        }

        if (worldKey != null) {
            if (isOnCooldown(player)) {
                player.closeInventory();
                String cooldownMsg = plugin.getConfig().getString("messages.cooldown.active", "&c You are on cooldown! Wait &e%time% &cseconds.")
                        .replace("&", "§").replace("%time%", String.valueOf(getCooldownTimeLeft(player)));
                player.sendActionBar(cooldownMsg);
                playErrorSound(player);
                return;
            }

            String worldName = plugin.getConfig().getString("worlds." + worldKey + ".world");
            if (worldName != null) {
                player.closeInventory();
                startTeleportCountdown(player, worldName);
            }
        }
    }

    private void startTeleportCountdown(Player player, String worldName) {
        int countdownTime = plugin.getConfig().getInt("teleport-settings.countdown", 5);
        UUID playerId = player.getUniqueId();

        teleportTasks.put(playerId, Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int timeLeft = countdownTime;

            @Override
            public void run() {
                if (!teleportTasks.containsKey(playerId)) {
                    cancelTeleport(playerId);
                    return;
                }

                if (timeLeft <= 0) {
                    SafeTeleportUtils.teleportPlayer(plugin, player, worldName);
                    startCooldown(player);
                    cancelTeleport(playerId);
                    return;
                }

                String countdownMsg = plugin.getConfig().getString("messages.teleporting", "&a Teleporting you in &e%seconds% &aseconds...")
                        .replace("&", "§").replace("%seconds%", String.valueOf(timeLeft));
                player.sendActionBar(countdownMsg);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                timeLeft--;
            }
        }, 0L, 20L).getTaskId());
    }

    private void cancelTeleport(UUID playerId) {
        if (teleportTasks.containsKey(playerId)) {
            Bukkit.getScheduler().cancelTask(teleportTasks.get(playerId));
            teleportTasks.remove(playerId);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Check if player moved a block
        if (teleportTasks.containsKey(playerId) && hasPlayerMovedBlock(event)) {
            cancelTeleport(playerId);
            String cancelMsg = plugin.getConfig().getString("messages.cancelled", "&c Teleportation cancelled because you moved.");
            player.sendActionBar(cancelMsg.replace("&", "§"));
            playErrorSound(player);
        }
    }

    private boolean hasPlayerMovedBlock(PlayerMoveEvent event) {
        return event.getFrom().getBlockX() != event.getTo().getBlockX()
                || event.getFrom().getBlockY() != event.getTo().getBlockY()
                || event.getFrom().getBlockZ() != event.getTo().getBlockZ();
    }

    private void startCooldown(Player player) {
        if (!plugin.getConfig().getBoolean("teleport-settings.cooldown.enabled", true)) return;
        int cooldown = plugin.getConfig().getInt("teleport-settings.cooldown.time", 30);
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (cooldown * 1000L));
    }

    private boolean isOnCooldown(Player player) {
        return cooldowns.containsKey(player.getUniqueId()) && cooldowns.get(player.getUniqueId()) > System.currentTimeMillis();
    }

    private long getCooldownTimeLeft(Player player) {
        return (cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
    }

    private void playErrorSound(Player player) {
        String soundName = plugin.getConfig().getString("teleport-settings.cooldown.error-sound", "BLOCK_NOTE_BLOCK_BASS");
        player.playSound(player.getLocation(), Sound.valueOf(soundName), 1.0f, 0.5f);
    }
}
