package com.mrcappy.plugins.autoreplant;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;

public class AutoReplantPlugin extends JavaPlugin {

    private static Enchantment AUTO_REPLANT;

    @Override
    public void onEnable() {
        AUTO_REPLANT = new AutoReplantEnchantment(new NamespacedKey(this, "auto_replant"));
        registerEnchantment(AUTO_REPLANT);
        getServer().getPluginManager().registerEvents(new AutoReplantListener(), this);
    }

    private void registerEnchantment(Enchantment enchantment) {
        try {
            Field byKeyField = Enchantment.class.getDeclaredField("byKey");
            byKeyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byKeyField.get(null);

            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byNameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);

            if (!byKey.containsKey(enchantment.getKey()) && !byName.containsKey(enchantment.getName())) {
                byKey.put(enchantment.getKey(), enchantment);
                byName.put(enchantment.getName(), enchantment);
                getLogger().info("Successfully registered enchantment: " + enchantment.getName());
            } else {
                getLogger().warning("Enchantment already registered: " + enchantment.getName());
            }
        } catch (Exception e) {
            getLogger().severe("Failed to register enchantment: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
