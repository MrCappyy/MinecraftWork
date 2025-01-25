package com.mrcappy.villageplugin.config;

import com.mrcappy.villageplugin.villager.VillagerRole;
import com.mrcappy.villageplugin.villager.VillagerTrade;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private static Map<String, VillagerRole> roles = new HashMap<>();

    public static void loadConfig(JavaPlugin plugin) {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        roles.clear(); // Clear previous data

        ConfigurationSection roleSection = config.getConfigurationSection("roles");
        if (roleSection == null) return;

        for (String roleKey : roleSection.getKeys(false)) {
            ConfigurationSection roleConfig = roleSection.getConfigurationSection(roleKey);

            // Buff and Level
            PotionEffectType buff = PotionEffectType.getByName(roleConfig.getString("buff"));
            int level = roleConfig.getInt("level");

            // Trades
            List<VillagerTrade> trades = new ArrayList<>();
            for (Map<?, ?> tradeData : roleConfig.getMapList("trades")) {
                trades.add(parseTrade(tradeData));
            }

            roles.put(roleKey.toUpperCase(), new VillagerRole(buff, level, trades));
        }

        plugin.getLogger().info("Config loaded successfully with " + roles.size() + " roles.");
    }

    private static VillagerTrade parseTrade(Map<?, ?> tradeData) {
        Material material = Material.getMaterial(tradeData.get("material").toString());
        String name = tradeData.get("name").toString();
        List<String> lore = (List<String>) tradeData.get("lore");
        int quantity = (int) tradeData.get("quantity");

        return new VillagerTrade(material, name, lore, quantity);
    }

    public static VillagerRole getRole(String roleName) {
        return roles.get(roleName.toUpperCase());
    }
}
