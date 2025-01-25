package com.mrcappy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class tagsprefixplugin extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private final ConcurrentHashMap<UUID, String> playerTags = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        config = getConfig();
        validateDefaultTag();
        getLogger().info("TagsPrefixPlugin by MrCappy has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TagsPrefixPlugin has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("prefix") || label.equalsIgnoreCase("tags")) {
                openPrefixGUI(player, 1);
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
        }
        return false;
    }

    private void openPrefixGUI(Player player, int page) {
        int size = 27;
        Inventory gui = Bukkit.createInventory(null, size, ChatColor.GOLD + "Select Your Prefix (Page " + page + ")");

        // Add staff section at the top row
        List<String> staffTags = config.getStringList("staff_tags");
        int staffSlot = 0;
        for (String staffTag : staffTags) {
            String permission = config.getString("tags." + staffTag + ".permission", "");
            if (!permission.isEmpty() && !player.hasPermission(permission)) continue;

            String displayName = ChatColor.translateAlternateColorCodes('&', config.getString("tags." + staffTag + ".name", "Unknown Tag"));
            String color = config.getString("tags." + staffTag + ".color", "BLACK");

            ItemStack item = new ItemStack(Material.valueOf(color + "_STAINED_GLASS_PANE"));
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(displayName);
                item.setItemMeta(meta);
            }

            gui.setItem(staffSlot, item);
            staffSlot++;
        }

        // Add navigation carpets at the top row
        gui.setItem(0, createNavigationItem(Material.GRAY_CARPET, ChatColor.RED + "No Previous Page"));
        gui.setItem(8, createNavigationItem(Material.GRAY_CARPET, ChatColor.RED + "No Next Page"));

        // Add normal tags to the GUI
        List<String> tags = config.getStringList("tags");
        int start = (page - 1) * 18;
        int end = Math.min(start + 18, tags.size());
        int slot = 9;

        for (int i = start; i < end && slot < 27; i++, slot++) {
            String tagKey = tags.get(i);
            String permission = config.getString("tags." + tagKey + ".permission", "");
            if (!permission.isEmpty() && !player.hasPermission(permission)) continue;

            String displayName = ChatColor.translateAlternateColorCodes('&', config.getString("tags." + tagKey + ".name", "Unknown Tag"));
            boolean isDefault = config.getBoolean("tags." + tagKey + ".default_tag", false);

            ItemStack item = new ItemStack(isDefault ? Material.BOOK : Material.BOOK);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(displayName);
                item.setItemMeta(meta);
            }

            gui.setItem(slot, item);
        }

        // Update navigation items
        if (page > 1) {
            gui.setItem(0, createNavigationItem(Material.WHITE_CARPET, ChatColor.GREEN + "Previous Page"));
        }
        if (tags.size() > end) {
            gui.setItem(8, createNavigationItem(Material.WHITE_CARPET, ChatColor.GREEN + "Next Page"));
        }

        player.openInventory(gui);
    }

    private ItemStack createNavigationItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith(ChatColor.GOLD + "Select Your Prefix")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();

            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();

            if (displayName.equals(ChatColor.GREEN + "Next Page")) {
                int currentPage = Integer.parseInt(event.getView().getTitle().replaceAll("[^0-9]", ""));
                openPrefixGUI(player, currentPage + 1);
            } else if (displayName.equals(ChatColor.GREEN + "Previous Page")) {
                int currentPage = Integer.parseInt(event.getView().getTitle().replaceAll("[^0-9]", ""));
                openPrefixGUI(player, currentPage - 1);
            } else {
                playerTags.put(player.getUniqueId(), displayName);
                player.sendMessage(ChatColor.GREEN + "You selected: " + displayName);
                player.closeInventory();
            }
        }
    }

    private void validateDefaultTag() {
        boolean hasDefaultTag = false;

        for (String tag : config.getStringList("tags")) {
            if (config.getBoolean("tags." + tag + ".default_tag", false)) {
                if (hasDefaultTag) {
                    getLogger().severe("Multiple default tags found in configuration. Only one tag can be marked as default.");
                    return;
                }
                hasDefaultTag = true;
            }
        }

        if (!hasDefaultTag) {
            getLogger().severe("No default tag found in configuration. Please mark one tag as default using 'default_tag: true'.");
        }
    }

    public String getPlayerTag(Player player) {
        return playerTags.getOrDefault(player.getUniqueId(), getDefaultTag());
    }

    private String getDefaultTag() {
        for (String tag : config.getStringList("tags")) {
            if (config.getBoolean("tags." + tag + ".default_tag", false)) {
                return ChatColor.translateAlternateColorCodes('&', config.getString("tags." + tag + ".name"));
            }
        }
        return "Unknown Tag";
    }

    public String replacePlaceholders(Player player, String message) {
        if (player == null || message == null) {
            return message;
        }

        String tag = getPlayerTag(player);
        return message.replace("%prefix_tag%", tag);
    }
}
