package me.fullfortune.plugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class FortuneListener implements Listener {

    private final FullFortune plugin;
    private final Random random = new Random();

    public FortuneListener(FullFortune plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Fetch the list of blocks from config
        List<String> fortuneBlocks = plugin.getConfigManager().getConfig().getStringList("fortune-blocks");

        if (fortuneBlocks.contains(block.getType().toString())) {
            event.setDropItems(false); // Disable normal drops

            // Check player's fortune level
            int fortuneLevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            if (fortuneLevel == 0) fortuneLevel = 1;

            // Calculate drop amount
            int dropCount = calculateFortuneDrops(fortuneLevel);
            ItemStack drop = new ItemStack(block.getType(), dropCount);

            // Add drops directly to inventory (auto-pickup)
            player.getInventory().addItem(drop);
        }
    }

    private int calculateFortuneDrops(int fortuneLevel) {
        int base = 1;
        for (int i = 0; i < fortuneLevel; i++) {
            if (random.nextDouble() < 0.5) base++;
        }
        return base;
    }
}
