package com.mrcappy.prefixtitles.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import com.mrcappy.prefixtitles.PrefixTitles;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderManager extends PlaceholderExpansion {

    private final PrefixTitles plugin;

    // Constructor to pass the plugin instance
    public PlaceholderManager(PrefixTitles plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "prefix";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MrCappy";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        // Ensure player is not null
        if (player == null) {
            return "";
        }

        // Check for the "title" placeholder
        if (identifier.equals("title")) {
            return player.hasMetadata("prefix_title")
                    ? player.getMetadata("prefix_title").get(0).asString()
                    : "No Title Selected";
        }

        return null; // Return null for unknown placeholders
    }

    // Static method to register the placeholder
    public static void register(PrefixTitles plugin) {
        new PlaceholderManager(plugin).register();
    }
}
