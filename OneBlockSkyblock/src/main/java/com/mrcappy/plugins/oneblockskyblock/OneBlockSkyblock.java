package com.mrcappy.plugins.oneblockskyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class OneBlockSkyblock extends JavaPlugin implements Listener {

    private final Map<Player, Integer> playerProgress = new HashMap<>();
    private final Random random = new Random();
    private List<Material> breakableBlocks;
    private List<EntityType> spawnableMobs;

    // List of unbreakable blocks
    private final Set<Material> unbreakableBlocks = Set.of(
            Material.BEDROCK,
            Material.END_PORTAL_FRAME,
            Material.END_PORTAL,
            Material.NETHER_PORTAL,
            Material.COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,
            Material.STRUCTURE_BLOCK,
            Material.STRUCTURE_VOID,
            Material.JIGSAW,
            Material.BARRIER
    );

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadBreakableBlocks();
        loadSpawnableMobs();

        getServer().getPluginManager().registerEvents(new OneBlockEventListener(this), this);
        getCommand("startoneblock").setExecutor(new OneBlockCommands(this));
        getCommand("leaveoneblock").setExecutor(new OneBlockCommands(this));
        getCommand("resetoneblock").setExecutor(new OneBlockCommands(this));
        getCommand("adminresetoneblock").setExecutor(new OneBlockCommands(this));

        getLogger().info("OneBlockSkyblock Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("OneBlockSkyblock Plugin Disabled!");
    }

    private void loadBreakableBlocks() {
        // Get all materials that are breakable, excluding unbreakable ones
        breakableBlocks = Arrays.stream(Material.values())
                .filter(Material::isBlock)
                .filter(material -> material.isItem())
                .filter(material -> !unbreakableBlocks.contains(material))
                .collect(Collectors.toList());
    }

    private void loadSpawnableMobs() {
        // Get all valid spawnable mobs
        spawnableMobs = Arrays.stream(EntityType.values())
                .filter(EntityType::isAlive)
                .filter(EntityType::isSpawnable)
                .collect(Collectors.toList());
    }

    public List<Material> getBreakableBlocks() {
        return breakableBlocks;
    }

    public List<EntityType> getSpawnableMobs() {
        return spawnableMobs;
    }

    public int getPlayerProgress(Player player) {
        return playerProgress.getOrDefault(player, 0);
    }

    public void incrementPlayerProgress(Player player) {
        playerProgress.put(player, getPlayerProgress(player) + 1);
    }

    public void resetPlayerProgress(Player player) {
        playerProgress.put(player, 0);
    }

    public Location createIsland(Player player) {
        World world = Bukkit.getWorld("oneblock_world");
        if (world == null) {
            world = Bukkit.createWorld(new WorldCreator("oneblock_world").generator(new VoidWorldGenerator()));
        }

        int islandDistance = getConfig().getInt("island.distance");
        int islandHeight = getConfig().getInt("island.height");
        Location islandLocation = new Location(world, playerProgress.size() * islandDistance, islandHeight, 0);

        Material defaultBlock = Material.valueOf(getConfig().getString("island.default_block", "GRASS_BLOCK"));
        islandLocation.getBlock().setType(defaultBlock);

        return islandLocation;
    }
}
