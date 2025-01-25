package xyz.talecraft.playercard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;
import java.util.UUID;

public class PlayerChatHoverListener implements Listener {

    private final Map<UUID, PlayerCard> playerCards;

    public PlayerChatHoverListener(Map<UUID, PlayerCard> playerCards) {
        this.playerCards = playerCards;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Get the player's card or create a default one if it doesn't exist
        PlayerCard card = playerCards.getOrDefault(uuid, new PlayerCard());

        // Create hover text for the player's card
        Component hoverText = Component.text()
                .append(Component.text("Player Card Information:\n", NamedTextColor.GOLD))
                .append(Component.text("Name: ", NamedTextColor.YELLOW).append(Component.text(card.getName(), NamedTextColor.WHITE)))
                .append(Component.text("\nAge: ", NamedTextColor.YELLOW).append(Component.text(card.getAge(), NamedTextColor.WHITE)))
                .append(Component.text("\nDiscord: ", NamedTextColor.YELLOW).append(Component.text(card.getDiscord(), NamedTextColor.WHITE)))
                .append(Component.text("\nNation: ", NamedTextColor.YELLOW).append(Component.text(card.getNation(), NamedTextColor.WHITE)))
                .append(Component.text("\nPronouns: ", NamedTextColor.YELLOW).append(Component.text(card.getPronouns(), NamedTextColor.WHITE)))
                .append(Component.text("\nRace: ", NamedTextColor.YELLOW).append(Component.text(card.getRace(), NamedTextColor.WHITE)))
                .append(Component.text("\nReligion: ", NamedTextColor.YELLOW).append(Component.text(card.getReligion(), NamedTextColor.WHITE)))
                .append(Component.text("\nWiki: ", NamedTextColor.YELLOW).append(Component.text(card.getWiki(), NamedTextColor.WHITE)))
                .build();

        // Set hover event for the player's name
        String playerNameWithHover = ChatColor.YELLOW + player.getName();
        String formattedMessage = playerNameWithHover + ChatColor.WHITE + ": " + ChatColor.GRAY + event.getMessage();

        // Set the chat format
        event.setFormat(formattedMessage);
    }
}
