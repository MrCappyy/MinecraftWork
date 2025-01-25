package com.mrcappy.prefixtitles.utils;

import com.mrcappy.prefixtitles.PrefixTitles;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CustomTitleManager {

    private static final FileConfiguration customConfig = PrefixTitles.getInstance().getCustomConfig();

    public static String createTitle(String titleName, Player owner) {
        String titleID = "title-" + UUID.randomUUID().toString().split("-")[0];
        customConfig.set("titles." + titleID + ".name", titleName);
        customConfig.set("titles." + titleID + ".owner", owner.getName());
        customConfig.set("titles." + titleID + ".permitted", List.of(owner.getName()));
        customConfig.set("titles." + titleID + ".lastRenamed", LocalDateTime.now().toString());
        PrefixTitles.getInstance().saveCustomConfig();
        return titleID;
    }

    public static boolean titleExists(String titleID) {
        return customConfig.contains("titles." + titleID);
    }

    public static void assignTitleToPlayer(Player player, String titleID) {
        player.setMetadata("prefix_title", new org.bukkit.metadata.FixedMetadataValue(
                PrefixTitles.getInstance(), titleID));
    }

    public static boolean renameTitle(String titleID, String newName) {
        if (!titleExists(titleID)) return false;

        customConfig.set("titles." + titleID + ".name", newName);
        customConfig.set("titles." + titleID + ".lastRenamed", LocalDateTime.now().toString());
        PrefixTitles.getInstance().saveCustomConfig();
        return true;
    }

    public static void addPlayerToTitle(String titleID, Player player) {
        if (!titleExists(titleID)) return;

        List<String> permitted = customConfig.getStringList("titles." + titleID + ".permitted");
        if (!permitted.contains(player.getName())) {
            permitted.add(player.getName());
            customConfig.set("titles." + titleID + ".permitted", permitted);
            PrefixTitles.getInstance().saveCustomConfig();
        }
    }

    public static void removePlayerFromTitle(String titleID, Player player) {
        if (!titleExists(titleID)) return;

        List<String> permitted = customConfig.getStringList("titles." + titleID + ".permitted");
        if (permitted.contains(player.getName())) {
            permitted.remove(player.getName());
            customConfig.set("titles." + titleID + ".permitted", permitted);
            PrefixTitles.getInstance().saveCustomConfig();
        }
    }
}
