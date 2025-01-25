package com.mrcappy.plugins.oneblockskyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class OneBlockSkyblock extends JavaPlugin implements Listener {

    private final Map<UUID, Integer> playerProgress = new HashMap<>();
    private final Map<UUID, Location> playerIslands = new HashMap<>();
    private final Random random = new Random();
    private List<Material> breakableBlocks;
    private List<EntityType> spawnableMobs;
    private File islandFile;
    private YamlConfiguration islandConfig;

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
        setupIslandLog();
        loadBreakableBlocks();
        loadSpawnableMobs();
        loadIslandLog();

        getServer().getPluginManager().registerEvents(new OneBlockEventListener(this), this);
        getCommand("startoneblock").setExecutor(new OneBlockCommands(this));
        getCommand("leaveoneblock").setExecutor(new OneBlockCommands(this));
        getCommand("resetoneblock").setExecutor(new OneBlockCommands(this));
        getCommand("adminresetoneblock").setExecutor(new OneBlockCommands(this));

        getLogger().info("OneBlockSkyblock Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        saveIslandLog();
        getLogger().info("OneBlockSkyblock Plugin Disabled!");
    }

    private void setupIslandLog() {
        // Create the islands.yml file
        islandFile = new File(getDataFolder(), "islands.yml");
        if (!islandFile.exists()) {
            try {
                islandFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Could not create islands.yml file!");
                e.printStackTrace();
            }
        }
        islandConfig = YamlConfiguration.loadConfiguration(islandFile);
    }

    private void loadIslandLog() {
        // Load saved islands from islands.yml
        for (String uuid : islandConfig.getKeys(false)) {
            UUID playerUUID = UUID.fromString(uuid);
            int progress = islandConfig.getInt(uuid + ".progress");
            String worldName = islandConfig.getString(uuid + ".island.world");
            double x = islandConfig.getDouble(uuid + ".island.x");
            double y = islandConfig.getDouble(uuid + ".island.y");
            double z = islandConfig.getDouble(uuid + ".island.z");

            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Location location = new Location(world, x, y, z);
                playerIslands.put(playerUUID, location);
                playerProgress.put(playerUUID, progress);
            }
        }
    }

    public void saveIslandLog() {
        // Save island data to islands.yml
        for (Map.Entry<UUID, Location> entry : playerIslands.entrySet()) {
            UUID playerUUID = entry.getKey();
            Location location = entry.getValue();
            int progress = playerProgress.getOrDefault(playerUUID, 0);

            islandConfig.set(playerUUID + ".progress", progress);
            islandConfig.set(playerUUID + ".island.world", location.getWorld().getName());
            islandConfig.set(playerUUID + ".island.x", location.getX());
            islandConfig.set(playerUUID + ".island.y", location.getY());
            islandConfig.set(playerUUID + ".island.z", location.getZ());
        }

        try {
            islandConfig.save(islandFile);
        } catch (IOException e) {
            getLogger().severe("Could not save islands.yml!");
            e.printStackTrace();
        }
    }

    private void loadBreakableBlocks() {
        // Get all breakable blocks (excluding unbreakable ones)
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
        return playerProgress.getOrDefault(player.getUniqueId(), 0);
    }

    public void incrementPlayerProgress(Player player) {
        playerProgress.put(player.getUniqueId(), getPlayerProgress(player) + 1);
    }

    public void resetPlayerProgress(Player player) {
        playerProgress.put(player.getUniqueId(), 0);
    }

    public Location createIsland(Player player) {
        UUID playerUUID = player.getUniqueId();

        // Check if the player already has an island
        if (playerIslands.containsKey(playerUUID)) {
            return playerIslands.get(playerUUID);
        }

        // Create a new island
        World world = Bukkit.getWorld("oneblock_world");
        if (world == null) {
            world = Bukkit.createWorld(new WorldCreator("oneblock_world").generator(new VoidWorldGenerator()));
        }

        int islandDistance = getConfig().getInt("island.distance");
        int islandHeight = getConfig().getInt("island.height");
        Location islandLocation = new Location(world, playerIslands.size() * islandDistance, islandHeight, 0);

        Material defaultBlock = Material.valueOf(getConfig().getString("island.default_block", "GRASS_BLOCK"));
        islandLocation.getBlock().setType(defaultBlock);

        playerIslands.put(playerUUID, islandLocation);
        playerProgress.put(playerUUID, 0);

        return islandLocation;
    }
}
