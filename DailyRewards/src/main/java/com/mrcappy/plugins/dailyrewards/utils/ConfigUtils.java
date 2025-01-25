package com.mrcappy.plugins.dailyrewards.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtils {

    public static String getOrDefault(FileConfiguration config, String path, String def) {
        return config.contains(path) ? config.getString(path) : def;
    }
}
