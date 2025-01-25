package com.mrcappy.plugins;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class BannedItemsPlugin extends JavaPlugin implements Listener {

    private final Set<Material> bannedPlaceItems = new HashSet<>();
    private final Set<Material> bannedCraftItems = new HashSet<>();
    private final Set<Material> bannedConsumables = new HashSet<>();
    private final Set<String> bannedArrows = new HashSet<>();
    private final Set<EntityType> bannedEntities = new HashSet<>();
    private final Set<Material> bannedWeaponsTools = new HashSet<>();
    private final Set<Material> bannedDispensedItems = new HashSet<>();

    @Override
    public void onEnable() {
        // Register events
        Bukkit.getPluginManager().registerEvents(this, this);

        // Initialize banned items
        initializeBannedItems();

        getLogger().info("BannedItemsPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BannedItemsPlugin disabled!");
    }

    private void initializeBannedItems() {
        // Add all bed colors to banned items
        for (Material material : Material.values()) {
            if (material.name().endsWith("_BED")) {
                bannedPlaceItems.add(material);
            }
        }

        // Add all chest boats and rafts to banned items
        for (Material material : Material.values()) {
            if (material.name().endsWith("_CHEST_BOAT") || material.name().endsWith("_CHEST_RAFT")) {
                bannedCraftItems.add(material);
                bannedPlaceItems.add(material);
            }
        }

        // Block placement bans
        bannedPlaceItems.add(Material.ENDER_CHEST);
        bannedPlaceItems.add(Material.BARRIER);
        bannedPlaceItems.add(Material.BEDROCK);
        bannedPlaceItems.add(Material.SCULK_CATALYST);
        bannedPlaceItems.add(Material.END_CRYSTAL);

        // Crafting bans for minecarts
        bannedCraftItems.add(Material.CHEST_MINECART);
        bannedCraftItems.add(Material.FURNACE_MINECART);
        bannedCraftItems.add(Material.TNT_MINECART);
        bannedCraftItems.add(Material.HOPPER_MINECART);

        // Arrows and special items
        bannedCraftItems.add(Material.WIND_CHARGE);
        bannedArrows.add("HUNGER_2_ARROW");
        bannedArrows.add("HUNGER_3_ARROW");
        bannedArrows.add("OOZING_ARROW");
        bannedArrows.add("INFESTATION_ARROW");
        bannedArrows.add("WIND_CHARGING_ARROW");
        bannedArrows.add("WEAVING_ARROW");

        // Interaction bans
        bannedWeaponsTools.add(Material.MACE);

        // Consumable bans
        bannedConsumables.add(Material.CHORUS_FRUIT);
        bannedConsumables.add(Material.ENCHANTED_GOLDEN_APPLE);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material blockType = event.getBlock().getType();
        if (bannedPlaceItems.contains(blockType)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You are not allowed to place this block!");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            Material itemType = event.getItem().getType();

            // Check for banned weapons/tools
            if (bannedWeaponsTools.contains(itemType)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("You are not allowed to use this weapon/tool!");
                return;
            }

            // Check for banned arrows
            String displayName = event.getItem().getItemMeta().getDisplayName().toUpperCase();
            if (bannedArrows.contains(displayName)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("You are not allowed to use this item!");
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        Material craftedItem = event.getRecipe().getResult().getType();
        if (bannedCraftItems.contains(craftedItem)) {
            event.setCancelled(true);
            if (event.getWhoClicked() != null) {
                event.getWhoClicked().sendMessage("You are not allowed to craft this item!");
            }
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Material itemType = event.getItem().getType();
        if (bannedConsumables.contains(itemType)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You are not allowed to consume this item!");
        }
    }

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent event) {
        if (event.getItem().getItemStack() != null && bannedCraftItems.contains(event.getItem().getItemStack().getType())) {
            event.setCancelled(true);
            if (event.getEntity() instanceof org.bukkit.entity.Player) {
                ((org.bukkit.entity.Player) event.getEntity()).sendMessage("You are not allowed to pick up this item!");
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You cannot interact with Villagers!");
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof org.bukkit.entity.Player) {
            if (event.isCritical()) {
                event.setCancelled(true);
                ((org.bukkit.entity.Player) event.getDamager()).sendMessage("Critical hits are disabled!");
            }
        }
    }
}
