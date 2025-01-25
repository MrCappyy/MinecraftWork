package com.xyz.talecraft.staffmode.tools;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class XRayTool {
    private final JavaPlugin plugin;

    public XRayTool(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static ItemStack getXRayTool() {
        ItemStack tool = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = tool.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6X-Ray Vision Tool");
            tool.setItemMeta(meta);
        }
        return tool;
    }

    public void activateXRay(Player player) {
        int radius = plugin.getConfig().getInt("staffmode.xray-radius", 10); // Get radius from config
        World world = player.getWorld();
        Vector playerLocation = player.getLocation().toVector();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Vector offset = new Vector(x, y, z);
                            Vector blockPos = playerLocation.clone().add(offset);

                            Material blockType = world.getBlockAt(
                                    blockPos.getBlockX(),
                                    blockPos.getBlockY(),
                                    blockPos.getBlockZ()
                            ).getType();

                            if (isTargetBlock(blockType)) {
                                world.spawnParticle(
                                        Particle.FLAME, // Replace with other particles if needed
                                        blockPos.toLocation(world),
                                        1, 0, 0, 0, 0
                                );
                            }
                        }
                    }
                }
            }
        }.runTask(plugin);
    }

    private boolean isTargetBlock(Material material) {
        List<String> targetBlocks = plugin.getConfig().getStringList("staffmode.xray-targets");
        return targetBlocks.contains(material.name());
    }
}
