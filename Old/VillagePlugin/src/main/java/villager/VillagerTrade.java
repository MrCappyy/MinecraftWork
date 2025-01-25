package com.mrcappy.villageplugin.villager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class VillagerTrade {

    private final Material material;
    private final String name;
    private final List<String> lore;
    private final int quantity;

    public VillagerTrade(Material material, String name, List<String> lore, int quantity) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.quantity = quantity;
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(material, quantity);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
