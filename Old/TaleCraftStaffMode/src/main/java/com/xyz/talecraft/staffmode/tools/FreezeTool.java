package com.xyz.talecraft.staffmode.tools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FreezeTool {
    public static ItemStack getFreezeTool() {
        ItemStack tool = new ItemStack(Material.STICK);
        ItemMeta meta = tool.getItemMeta();
        meta.setDisplayName("§cFreeze Tool");
        tool.setItemMeta(meta);
        return tool;
    }

    public static void freezePlayer(Player target) {
        target.setWalkSpeed(0);
        target.sendMessage("§cYou have been frozen by staff.");
    }

    public static void unfreezePlayer(Player target) {
        target.setWalkSpeed(0.2f); // Reset to default walk speed
        target.sendMessage("§aYou have been unfrozen by staff.");
    }
}
