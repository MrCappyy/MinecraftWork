package com.mrcappy.prefixtitles.gui;

import com.mrcappy.prefixtitles.PrefixTitles;
import com.mrcappy.prefixtitles.utils.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PrefixGUI implements Listener {

    private static final int GUI_SIZE = 27; // 3 Rows (9x3)
    private static final int[] TITLE_SLOTS = {
            9, 10, 11, 12, 13, 14, 15, 16, 17, // Second row
            18, 19, 20, 21, 22, 23, 24, 25, 26 // Third row (fully filled)
    };
    private static final Map<UUID, Integer> playerPages = new HashMap<>();
    private static final ItemStack BACK_ARROW = createItem(Material.WHITE_CARPET, ChatColor.RED + "← Previous Page");
    private static final ItemStack FORWARD_ARROW = createItem(Material.WHITE_CARPET, ChatColor.GREEN + "→ Next Page");
    private static final ItemStack INACTIVE_ARROW = createItem(Material.GRAY_CARPET, ChatColor.GRAY + "Inactive");

    public static void openGUI(Player player, int page) {
        FileConfiguration config = PrefixTitles.getInstance().getConfig();
        List<String> titles = config.getStringList("titles.member.titles");

        int totalPages = (int) Math.ceil((double) titles.size() / TITLE_SLOTS.length);
        page = Math.max(1, Math.min(page, totalPages)); // Clamp page range
        playerPages.put(player.getUniqueId(), page);

        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, ChatColor.GOLD + "Select Your Title - Page " + page);

        // Top Row - Navigation Arrows
        gui.setItem(0, page == 1 ? INACTIVE_ARROW : BACK_ARROW);
        gui.setItem(8, page == totalPages ? INACTIVE_ARROW : FORWARD_ARROW);

        // Fill Titles in Second and Third Rows
        int start = (page - 1) * TITLE_SLOTS.length;
        int end = Math.min(start + TITLE_SLOTS.length, titles.size());

        for (int i = start, slotIndex = 0; i < end; i++, slotIndex++) {
            String titleName = titles.get(i);
            ItemStack titleItem = createItem(Material.BOOK, ChatColor.translateAlternateColorCodes('&', titleName));
            gui.setItem(TITLE_SLOTS[slotIndex], titleItem);
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        // Check if it's the Prefix GUI
        if (!event.getView().getTitle().startsWith(ChatColor.GOLD + "Select Your Title")) return;

        event.setCancelled(true); // Prevent any item picking/moving

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        String displayName = clickedItem.getItemMeta().getDisplayName();

        // Navigation Handling
        if (clickedItem.isSimilar(BACK_ARROW)) {
            openGUI(player, playerPages.getOrDefault(uuid, 1) - 1);
            return;
        }
        if (clickedItem.isSimilar(FORWARD_ARROW)) {
            openGUI(player, playerPages.getOrDefault(uuid, 1) + 1);
            return;
        }

        // Title Selection
        if (clickedItem.getType() == Material.BOOK) {
            if (CooldownManager.isOnCooldown(uuid)) {
                long timeLeft = CooldownManager.getTimeLeft(uuid);
                player.sendMessage(ChatColor.RED + "You are on cooldown for " + timeLeft + " seconds!");
            } else {
                player.setMetadata("prefix_title", new org.bukkit.metadata.FixedMetadataValue(
                        PrefixTitles.getInstance(), displayName));
                player.sendMessage(ChatColor.GREEN + "Your title has been set to: " + displayName);
                CooldownManager.setCooldown(uuid, PrefixTitles.getInstance().getConfig().getInt("cooldown.duration"));
            }
            player.closeInventory();
        }
    }

    private static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
