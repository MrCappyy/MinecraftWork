package com.example.coinflip;

import org.bukkit.ChatColor;

public class Utils {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
