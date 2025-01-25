package com.mrcappy.homeGui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HomeGui extends JavaPlugin implements Listener {

    private final Map<UUID, Map<Integer, Location>> homeLocations = new HashMap<>();
    private final Map<UUID, BukkitRunnable> activeTeleports = new HashMap<>();
    private final Map<UUID, Integer> deletePendingHome = new HashMap<>();
    private File homesFile;
    private YamlConfiguration homesConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("home").setExecutor(this::onCommand);
        Bukkit.getPluginManager().registerEvents(this, this);
        loadHomes();
    }

    @Override
    public void onDisable() {
        saveHomes();
    }

    private void loadHomes() {
        homesFile = new File(getDataFolder(), "homes.yml");
        if (!homesFile.exists()) {
            try {
                homesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);

        for (String uuid : homesConfig.getKeys(false)) {
            UUID playerUUID = UUID.fromString(uuid);
            Map<Integer, Location> playerHomes = new HashMap<>();
            for (String homeNumber : homesConfig.getConfigurationSection(uuid).getKeys(false)) {
                Location location = homesConfig.getLocation(uuid + "." + homeNumber);
                playerHomes.put(Integer.parseInt(homeNumber), location);
            }
            homeLocations.put(playerUUID, playerHomes);
        }
    }

    private void saveHomes() {
        for (Map.Entry<UUID, Map<Integer, Location>> entry : homeLocations.entrySet()) {
            UUID playerUUID = entry.getKey();
            for (Map.Entry<Integer, Location> home : entry.getValue().entrySet()) {
                homesConfig.set(playerUUID.toString() + "." + home.getKey(), home.getValue());
            }
        }
        try {
            homesConfig.save(homesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openHomeGUI(Player player) {
        FileConfiguration config = getConfig();
        Inventory gui = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "Homes"));

        // Decorative Border
        ItemStack glassPane = createItem(Material.GRAY_STAINED_GLASS_PANE, " ", List.of());
        for (int i = 0; i < 36; i++) {
            if (i < 9 || i >= 27 || i % 9 == 0 || (i + 1) % 9 == 0) {
                gui.setItem(i, glassPane);
            }
        }

        // Close Button
        gui.setItem(27, createItem(Material.OAK_DOOR, ChatColor.translateAlternateColorCodes('&', "&cClose Menu"), List.of()));

        UUID playerUUID = player.getUniqueId();
        for (int i = 1; i <= 4; i++) {
            int slot = 10 + i - 1;

            if (!player.isOp() && i > 1 && !player.hasPermission("home." + i)) {
                gui.setItem(slot, createItem(Material.RED_STAINED_GLASS_PANE,
                        ChatColor.translateAlternateColorCodes('&', "&cNo Access"),
                        List.of(ChatColor.translateAlternateColorCodes('&', "&7You need the &ehome." + i + " &7permission."))));
                continue;
            }

            Map<Integer, Location> playerHomes = homeLocations.getOrDefault(playerUUID, new HashMap<>());
            if (playerHomes.containsKey(i)) {
                gui.setItem(slot, createItem(Material.BLUE_BED,
                        ChatColor.translateAlternateColorCodes('&', "&bHome " + i),
                        List.of(ChatColor.translateAlternateColorCodes('&', "&7Click to teleport."))));
                gui.setItem(slot + 9, createItem(Material.BLUE_DYE,
                        ChatColor.translateAlternateColorCodes('&', "&cDelete Home"),
                        List.of(ChatColor.translateAlternateColorCodes('&', "&7Click to delete home."))));
            } else {
                gui.setItem(slot, createItem(Material.WHITE_WOOL,
                        ChatColor.translateAlternateColorCodes('&', "&fSet Home " + i),
                        List.of(ChatColor.translateAlternateColorCodes('&', "&7Click to set home."))));
            }
        }

        player.openInventory(gui);
    }

    private void openDeleteConfirmation(Player player, int homeNumber) {
        Inventory confirmGui = Bukkit.createInventory(null, 9, ChatColor.RED + "Confirm Deletion");

        confirmGui.setItem(3, createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "Yes, Delete",
                List.of(ChatColor.GRAY + "Click to delete home " + homeNumber + ".")));
        confirmGui.setItem(5, createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "No, Cancel",
                List.of(ChatColor.GRAY + "Click to cancel.")));

        deletePendingHome.put(player.getUniqueId(), homeNumber);
        player.openInventory(confirmGui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Ensure the clicker is a player
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Get the inventory title
        String title = event.getView().getTitle();

        // Check if the clicked inventory is one of your custom GUIs
        if (title.equals(ChatColor.translateAlternateColorCodes('&', "Homes")) ||
                title.equals(ChatColor.RED + "Confirm Deletion")) {
            event.setCancelled(true); // Cancel clicks only in the custom GUIs

            int slot = event.getSlot();
            if (title.equals(ChatColor.translateAlternateColorCodes('&', "Homes"))) {
                if (slot == 27) { // Close Menu
                    player.closeInventory();
                } else if (slot >= 10 && slot <= 13) { // Set/Teleport Home
                    int homeNumber = slot - 9;
                    handleSetOrTeleport(player, homeNumber);
                } else if (slot >= 19 && slot <= 22) { // Delete Home
                    int homeNumber = slot - 18;
                    openDeleteConfirmation(player, homeNumber);
                }
            } else if (title.equals(ChatColor.RED + "Confirm Deletion")) {
                if (slot == 3) { // Yes, delete
                    handleDeleteConfirmation(player, slot);
                } else if (slot == 5) { // No, cancel
                    deletePendingHome.remove(player.getUniqueId());
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "Deletion cancelled.");
                }
            }
        }
    }


    private void handleSetOrTeleport(Player player, int homeNumber) {
        UUID playerUUID = player.getUniqueId();
        Map<Integer, Location> playerHomes = homeLocations.computeIfAbsent(playerUUID, k -> new HashMap<>());

        if (playerHomes.containsKey(homeNumber)) {
            teleportWithCountdown(player, playerHomes.get(homeNumber), 5);
        } else {
            playerHomes.put(homeNumber, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Home " + homeNumber + " has been set!");
            saveHomes();
        }
        player.closeInventory();
    }

    private void handleDeleteConfirmation(Player player, int slot) {
        UUID playerUUID = player.getUniqueId();
        if (!deletePendingHome.containsKey(playerUUID)) return;

        if (slot == 3) {
            int homeNumber = deletePendingHome.remove(playerUUID);
            Map<Integer, Location> playerHomes = homeLocations.get(playerUUID);
            if (playerHomes != null) {
                playerHomes.remove(homeNumber);
                player.sendMessage(ChatColor.RED + "Home " + homeNumber + " has been deleted!");
                saveHomes();
            }
            player.closeInventory();
        }
    }

    private void teleportWithCountdown(Player player, Location location, int countdown) {
        UUID playerUUID = player.getUniqueId();
        if (activeTeleports.containsKey(playerUUID)) return;

        String actionBar = ChatColor.translateAlternateColorCodes('&', "&aTeleporting in &e%time% &aseconds...");
        String successMessage = ChatColor.translateAlternateColorCodes('&', "&aTeleported successfully!");
        String cancelMessage = ChatColor.translateAlternateColorCodes('&', "&cTeleportation canceled because you moved!");
        Sound countdownSound = Sound.BLOCK_NOTE_BLOCK_HARP;
        Sound successSound = Sound.ENTITY_ENDERMAN_TELEPORT;

        Location startLocation = player.getLocation();
        BukkitRunnable task = new BukkitRunnable() {
            int timeLeft = countdown;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    player.teleport(location);
                    player.sendMessage(successMessage);
                    player.playSound(location, successSound, 1.0f, 1.0f);
                    activeTeleports.remove(playerUUID);
                    cancel();
                    return;
                }
                player.sendActionBar(actionBar.replace("%time%", String.valueOf(timeLeft)));
                player.playSound(player.getLocation(), countdownSound, 1.0f, 1.0f);
                timeLeft--;
            }
        };

        activeTeleports.put(playerUUID, task);
        task.runTaskTimer(this, 0L, 20L);
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }
        openHomeGUI(player);
        return true;
    }
}
