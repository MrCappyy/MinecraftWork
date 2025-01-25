package com.mrcappy.plugins.oneblockskyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Random;

public class OneBlockEventListener implements Listener {

    private final OneBlockSkyblock plugin;
    private final Random random = new Random();

    public OneBlockEventListener(OneBlockSkyblock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location blockLocation = event.getBlock().getLocation();

        // Handle progression and block regeneration
        List<Material> blocks = plugin.getBreakableBlocks();
        Material newBlock = blocks.get(random.nextInt(blocks.size()));
        event.getBlock().setType(newBlock);

        // Increment player progress
        plugin.incrementPlayerProgress(player);
        int progress = plugin.getPlayerProgress(player);
        int blocksToNextLevel = plugin.getConfig().getInt("progression.blocks_to_next_level");

        if (progress % blocksToNextLevel == 0) {
            player.sendMessage("§aYou've progressed to the next level! Keep going!");
        } else {
            int blocksLeft = blocksToNextLevel - (progress % blocksToNextLevel);
            player.sendMessage("§e" + blocksLeft + " blocks until the next level.");
        }

        // Randomly spawn mobs
        int mobSpawnChance = plugin.getConfig().getInt("progression.mob_spawn_chance");
        if (random.nextInt(100) < mobSpawnChance) {
            List<EntityType> mobs = plugin.getSpawnableMobs();
            EntityType mob = mobs.get(random.nextInt(mobs.size()));
            Bukkit.getWorld("oneblock_world").spawnEntity(blockLocation.add(1, 1, 1), mob);
            player.sendMessage("§cA wild " + mob.name() + " has appeared!");
        }
    }
}
