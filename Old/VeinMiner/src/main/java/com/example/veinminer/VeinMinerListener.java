package com.example.veinminer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class VeinMinerListener implements Listener {

    private final VeinMinerPlugin plugin;
    private static final BlockFace[] FACES = {
            BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST
    };

    public VeinMinerListener(VeinMinerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();

        // Check if the player is sneaking and holding a valid tool
        if (!player.isSneaking()) return;

        if (isOre(block) && isValidTool(tool, "oreTools")) {
            veinMine(block, tool, "enableOreVeinMining", "oreMining");
        } else if (isLog(block) && isValidTool(tool, "logTools")) {
            veinMine(block, tool, "enableLogVeinMining", "logMining");
        }
    }

    private void veinMine(Block startBlock, ItemStack tool, String configKey, String durabilityKey) {
        if (!plugin.getConfig().getBoolean("settings." + configKey)) return;

        Set<Block> visitedBlocks = new HashSet<>();
        int maxBlocks = plugin.getConfig().getInt("settings.maxBlocksPerVein");
        mineConnectedBlocks(startBlock, tool, visitedBlocks, 0, maxBlocks, durabilityKey);
    }

    private void mineConnectedBlocks(Block block, ItemStack tool, Set<Block> visited, int count, int max, String durabilityKey) {
        if (count >= max || visited.contains(block)) return;

        visited.add(block);
        block.breakNaturally(tool);

        if (plugin.getConfig().getBoolean("settings.durabilityLoss")) {
            tool.setDurability((short) (tool.getDurability() + 1));
        }

        for (BlockFace face : FACES) {
            Block relative = block.getRelative(face);
            if ((isOre(relative) && durabilityKey.equals("oreMining")) || (isLog(relative) && durabilityKey.equals("logMining"))) {
                mineConnectedBlocks(relative, tool, visited, count + 1, max, durabilityKey);
            }
        }
    }

    private boolean isOre(Block block) {
        return block.getType().toString().endsWith("_ORE") || block.getType() == Material.ANCIENT_DEBRIS;
    }

    private boolean isLog(Block block) {
        return block.getType().toString().endsWith("_LOG");
    }

    private boolean isValidTool(ItemStack tool, String toolConfig) {
        return plugin.getConfig().getStringList("settings.tools." + toolConfig).contains(tool.getType().toString());
    }
}
