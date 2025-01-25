package com.talecraft.welcomeplugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class TaleCraftWelcomePlugin extends JavaPlugin implements Listener {

    private JsonObject playerData;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info(ChatColor.GREEN + "TaleCraft Welcome Plugin by MrCappy is enabled!");

        // Load player data from JSON file
        loadPlayerData();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "TaleCraft Welcome Plugin by MrCappy is disabled!");

        // Save player data to JSON file
        savePlayerData();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null); // Suppress the default join message

        String playerName = event.getPlayer().getName();
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (playerData.has(playerUUID.toString())) {
            // Returning player message
            Bukkit.broadcastMessage(ChatColor.GRAY + "Welcome Back " + ChatColor.AQUA + playerName + ChatColor.GRAY + " To TaleCraft.");
        } else {
            // First-time player message
            Bukkit.broadcastMessage(ChatColor.GRAY + "Welcome " + ChatColor.AQUA + playerName + ChatColor.GRAY + " To TaleCraft, Enjoy Your Stay!");
            // Play a sound for the player
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);

            // Save the player to the database
            playerData.add(playerUUID.toString(), new JsonPrimitive(playerName));
            savePlayerData();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null); // Suppress the default quit message
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        // Suppress advancement notifications by sending an empty message
        event.getPlayer().sendMessage("");
    }

    /**
     * Loads the player data from a JSON file.
     */
    private void loadPlayerData() {
        File file = new File(getDataFolder(), "joined-players.json");

        if (!file.exists()) {
            playerData = new JsonObject();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            playerData = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to load player data. Starting with an empty database.");
            playerData = new JsonObject();
        }
    }

    /**
     * Saves the player data to a JSON file.
     */
    private void savePlayerData() {
        File file = new File(getDataFolder(), "joined-players.json");

        // Ensure the plugin folder exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(playerData.toString());
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to save player data.");
        }
    }
}
