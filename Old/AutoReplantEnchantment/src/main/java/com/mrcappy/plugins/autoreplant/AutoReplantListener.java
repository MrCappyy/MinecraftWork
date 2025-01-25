package com.mrcappy.plugins.autoreplant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class AutoReplantListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        // Check if the tool has the Auto Replant enchantment
        Enchantment autoReplant = AutoReplantPlugin.AUTO_REPLANT;
        if (autoReplant != null && tool.containsEnchantment(autoReplant)) {
            Block block = event.getBlock();
            Material type = block.getType();

            if (isCrop(type)) {
                event.setDropItems(false);
                block.setType(Material.AIR);

                Material seed = getSeed(type);
                if (seed != null && player.getInventory().contains(seed)) {
                    block.setType(type);
                    player.getInventory().removeItem(new ItemStack(seed, 1));
                }
            }
        }
    }

    private boolean isCrop(Material material) {
        return material == Material.WHEAT ||
                material == Material.CARROTS ||
                material == Material.POTATOES ||
                material == Material.BEETROOTS;
    }

    private Material getSeed(Material crop) {
        switch (crop) {
            case WHEAT: return Material.WHEAT_SEEDS;
            case CARROTS: return Material.CARROT;
            case POTATOES: return Material.POTATO;
            case BEETROOTS: return Material.BEETROOT_SEEDS;
            default: return null;
        }
    }
}
