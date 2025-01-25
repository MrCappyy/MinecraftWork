package com.mrcappy.prefixtitles.utils;

import com.mrcappy.prefixtitles.PrefixTitles;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private static final FileConfiguration config = PrefixTitles.getInstance().getConfig();

    // Titles Section
    public static String getTitleDisplayName(String titleKey) {
        return config.getString("titles." + titleKey + ".name", "&7No Title Found");
    }

    public static String getTitleLore(String titleKey) {
        return config.getString("titles." + titleKey + ".lore", "&7Left click to select this prefix");
    }

    public static String getTitlePermission(String titleKey) {
        return config.getString("titles." + titleKey + ".permission", "prefix.default");
    }

    public static List<String> getAvailableTitles() {
        return config.getConfigurationSection("titles").getKeys(false).stream().toList();
    }

    // Cooldown Section
    public static int getCooldownDuration() {
        return config.getInt("cooldown.duration", 120); // Default to 120 seconds
    }

    public static String getCooldownMessage() {
        return config.getString("cooldown.message", "&cYou must wait {time} seconds before selecting another title.");
    }

    // GUI Section
    public static int getGuiRows() {
        return config.getInt("gui.rows", 3); // Default to 3 rows
    }

    public static String getFirstPageName() {
        return config.getString("gui.first-page-name", "&cFirst Page");
    }

    public static String getLastPageName() {
        return config.getString("gui.last-page-name", "&cLast Page");
    }

    public static String getBackName() {
        return config.getString("gui.back-name", "&fBack");
    }

    public static String getForwardName() {
        return config.getString("gui.forward-name", "&fForward");
    }

    public static String getGuiItemMaterial(String itemKey) {
        return config.getString("gui." + itemKey, "white_carpet");
    }
}
