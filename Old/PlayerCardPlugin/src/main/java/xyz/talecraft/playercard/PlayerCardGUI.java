package xyz.talecraft.playercard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class PlayerCardGUI implements Listener {

    private final PlayerCardPlugin plugin;

    public PlayerCardGUI(PlayerCardPlugin plugin) {
        this.plugin = plugin;
    }

    public static void openPlayerCardGUI(Player viewer, PlayerCard card) {
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GOLD + viewer.getName() + "'s Player Card");

        // Create player head
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(viewer);
            skullMeta.setDisplayName(ChatColor.YELLOW + viewer.getName());
            skullMeta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Name: " + card.getName(),
                    ChatColor.GRAY + "Age: " + card.getAge(),
                    ChatColor.GRAY + "Discord: " + card.getDiscord(),
                    ChatColor.GRAY + "Nation: " + card.getNation(),
                    ChatColor.GRAY + "Pronouns: " + card.getPronouns(),
                    ChatColor.GRAY + "Race: " + card.getRace(),
                    ChatColor.GRAY + "Religion: " + card.getReligion(),
                    ChatColor.GRAY + "Wiki: " + card.getWiki()
            ));
            playerHead.setItemMeta(skullMeta);
        }

        // Place the player head in the center slot
        gui.setItem(4, playerHead);

        // Open the GUI for the viewer
        viewer.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the clicked inventory is a player card GUI
        if (event.getView().getTitle().endsWith("'s Player Card")) {
            // Cancel the event if any slot is clicked
            event.setCancelled(true);

            // Ensure the item is returned to its slot instantly
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Inventory inventory = event.getClickedInventory();
                if (inventory != null && event.getSlot() == 4) {
                    ItemStack playerHead = inventory.getItem(4);
                    inventory.setItem(4, playerHead);
                }
            }, 1L);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // Check if the dragged inventory is a player card GUI
        if (event.getView().getTitle().endsWith("'s Player Card")) {
            // Cancel the event if the drag involves the player head slot (index 4)
            if (event.getRawSlots().contains(4)) {
                event.setCancelled(true);

                // Ensure the item is returned to its slot instantly
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Inventory inventory = event.getInventory();
                    ItemStack playerHead = inventory.getItem(4);
                    inventory.setItem(4, playerHead);
                }, 1L);
            }
        }
    }
}
