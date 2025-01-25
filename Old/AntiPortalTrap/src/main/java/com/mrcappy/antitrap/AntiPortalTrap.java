package com.mrcappy.antitrap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiPortalTrap extends JavaPlugin implements Listener {

    // Player data tracking
    private final Map<UUID, Location> portalEntryLocations = new HashMap<>();
    private final Map<UUID, Long> portalStuckTime = new HashMap<>();
    private final Map<UUID, Location> lastPlayerLocation = new HashMap<>();

    // Configurable settings
    private String discordWebhookURL;
    private int detectionTime; // Time in milliseconds
    private int countdownTime; // Countdown in seconds
    private int safeSearchRadius;

    @Override
    public void onEnable() {
        // Load config
        saveDefaultConfig();
        loadConfigSettings();

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("AntiPortalTrap enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiPortalTrap disabled.");
    }

    private void loadConfigSettings() {
        FileConfiguration config = getConfig();
        discordWebhookURL = config.getString("webhook_url", "");
        detectionTime = config.getInt("detection_time", 15) * 1000;
        countdownTime = config.getInt("countdown_time", 5);
        safeSearchRadius = config.getInt("safe_search_radius", 5);
    }

    @EventHandler
    public void onPlayerEnterPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (!portalEntryLocations.containsKey(playerId)) {
            portalEntryLocations.put(playerId, player.getLocation());
        }

        new BukkitRunnable() {
            private int countdown = countdownTime;

            @Override
            public void run() {
                if (player.getLocation().getBlock().getType() == Material.NETHER_PORTAL) {
                    portalStuckTime.putIfAbsent(playerId, System.currentTimeMillis());
                    long stuckTime = System.currentTimeMillis() - portalStuckTime.get(playerId);

                    Location currentLocation = player.getLocation();
                    if (!lastPlayerLocation.containsKey(playerId) || !lastPlayerLocation.get(playerId).equals(currentLocation)) {
                        lastPlayerLocation.put(playerId, currentLocation);
                        portalStuckTime.put(playerId, System.currentTimeMillis());
                        countdown = countdownTime;
                        return;
                    }

                    if (stuckTime >= detectionTime) {
                        if (countdown > 0) {
                            player.sendActionBar("\u00a7cPortal Trap Detected: " + countdown);
                            countdown--;
                        } else {
                            teleportPlayerSafely(player);
                            portalStuckTime.remove(playerId);
                            portalEntryLocations.remove(playerId);
                            lastPlayerLocation.remove(playerId);
                            cancel();
                        }
                    }
                } else {
                    portalStuckTime.remove(playerId);
                    portalEntryLocations.remove(playerId);
                    lastPlayerLocation.remove(playerId);
                    cancel();
                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (player.getLocation().getBlock().getType() != Material.NETHER_PORTAL) {
            portalStuckTime.remove(playerId);
            lastPlayerLocation.remove(playerId);
        }
    }

    private void teleportPlayerSafely(Player player) {
        Location originalLocation = portalEntryLocations.get(player.getUniqueId());
        if (originalLocation == null) return;

        Location safeLocation = findSafeLocation(originalLocation);
        if (safeLocation != null) {
            player.teleport(safeLocation);
            player.sendMessage("\u00a7aYou have been teleported out of the portal!");
            getLogger().info(player.getName() + " teleported to a safe location from a portal trap.");
            sendDiscordNotification(player, safeLocation);
        } else {
            getLogger().warning("Failed to find a safe teleport location for " + player.getName());
            sendDiscordNotification(player, null);
        }
    }

    private Location findSafeLocation(Location location) {
        for (int y = location.getBlockY(); y >= location.getWorld().getMinHeight(); y--) {
            for (int x = -safeSearchRadius; x <= safeSearchRadius; x++) {
                for (int z = -safeSearchRadius; z <= safeSearchRadius; z++) {
                    Location checkLocation = location.clone().add(x, 0, z);
                    Block block = checkLocation.getBlock();
                    if (block.getType().isSolid() && block.getRelative(0, 1, 0).getType().isAir() && block.getRelative(0, 2, 0).getType().isAir()) {
                        return checkLocation.add(0.5, 1, 0.5);
                    }
                }
            }
        }
        return null;
    }

    private void sendDiscordNotification(Player player, Location location) {
        if (discordWebhookURL.isEmpty()) return;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(discordWebhookURL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String message;
            if (location != null) {
                message = String.format("{\"content\":\"%s was teleported to a safe location: X=%.2f, Y=%.2f, Z=%.2f.\"}",
                        player.getName(), location.getX(), location.getY(), location.getZ());
            } else {
                message = String.format("{\"content\":\"%s was detected in a portal trap but no safe location could be found.\"}",
                        player.getName());
            }

            try (OutputStream output = connection.getOutputStream()) {
                output.write(message.getBytes());
            }

            connection.getResponseCode();
        } catch (Exception e) {
            getLogger().warning("Failed to send Discord notification: " + e.getMessage());
        }
    }
}
