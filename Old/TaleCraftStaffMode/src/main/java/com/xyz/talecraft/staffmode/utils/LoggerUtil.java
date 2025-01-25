package com.xyz.talecraft.staffmode.utils;

import com.xyz.talecraft.staffmode.StaffModePlugin;
import org.bukkit.entity.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class LoggerUtil {
    public static void logAction(Player player, String action) {
        String message = player.getName() + " " + action;
        StaffModePlugin.getInstance().getLogger().log(Level.INFO, message);

        // Log to custom file
        try (FileWriter writer = new FileWriter(StaffModePlugin.getInstance().getDataFolder() + "/staff-actions.log", true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            StaffModePlugin.getInstance().getLogger().log(Level.SEVERE, "Could not write to staff-actions.log", e);
        }
    }
}
