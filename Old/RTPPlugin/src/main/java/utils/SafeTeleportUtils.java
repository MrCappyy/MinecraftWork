package com.mrcappy.safertp.utils;

import com.mrcappy.safertp.SafeRTP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class SafeTeleportUtils {

    private static final int MAX_ATTEMPTS = 50; // Max attempts to find a location
    private static final int MIN_Y = 32; // Minimum Y level in the Nether
    private static final int MAX_Y = 102; // Maximum Y level in the Nether

    public static void teleportPlayer(SafeRTP plugin, Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage("§cWorld \"" + worldName + "\" does not exist!");
            return;
        }

        int radiusX = plugin.getConfig().getInt("teleport-settings.radius.x", 10000);
        int radiusZ = plugin.getConfig().getInt("teleport-settings.radius.z", 10000);

        Location location = findSafeLocation(world, radiusX, radiusZ);

        if (location != null) {
            player.teleport(location);
            player.sendMessage(plugin.getConfig().getString("messages.success", "Successfully teleported!")
                    .replace("&", "§"));
        } else {
            player.teleport(world.getSpawnLocation()); // Fallback to spawn if all else fails
            player.sendMessage("§eNo fully safe location found. Teleported to spawn as fallback.");
        }
    }

    private static Location findSafeLocation(World world, int radiusX, int radiusZ) {
        Random random = new Random();

        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            int x = random.nextInt(radiusX * 2) - radiusX;
            int z = random.nextInt(radiusZ * 2) - radiusZ;

            // In the Nether, iterate downward from MAX_Y to MIN_Y to avoid the roof
            if (world.getEnvironment() == World.Environment.NETHER) {
                for (int y = MAX_Y; y >= MIN_Y; y--) {
                    Location loc = new Location(world, x, y, z);
                    if (isLocationSafe(loc)) {
                        return loc;
                    }
                }
            } else {
                // For Overworld or End, get the highest safe block
                int y = world.getHighestBlockYAt(x, z);
                Location loc = new Location(world, x, y + 1, z);
                if (isLocationSafe(loc)) {
                    return loc;
                }
            }
        }
        return null; // Return null if no valid location is found
    }

    private static boolean isLocationSafe(Location loc) {
        World world = loc.getWorld();
        if (world == null) return false;

        // Block checks
        Material blockBelow = loc.clone().subtract(0, 1, 0).getBlock().getType();
        Material blockAtFeet = loc.getBlock().getType();
        Material blockAbove = loc.clone().add(0, 1, 0).getBlock().getType();

        // Conditions:
        // 1. Block below must be solid
        // 2. Block at feet level must be air
        // 3. Block above head must be air
        return blockBelow.isSolid() && blockAtFeet == Material.AIR && blockAbove == Material.AIR;
    }
}
