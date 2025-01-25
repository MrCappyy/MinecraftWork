package com.xyz.talecraft.staffmode.commands;

import com.xyz.talecraft.staffmode.utils.VanishManager;
import com.xyz.talecraft.staffmode.utils.NoClipManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.HashSet;
import java.util.Set;

public class StaffModeCommand implements CommandExecutor {
    private final Set<Player> staffModePlayers = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("staffmode.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        // Inside the block for enabling staff mode:
        VanishManager.vanish(player);

        // Inside the block for disabling staff mode:
        VanishManager.unvanish(player);

        if (staffModePlayers.contains(player)) {
            // Disable staff mode
            NoClipManager.disableNoClip(player);
            staffModePlayers.remove(player);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(ChatColor.GREEN + "You have exited staff mode.");
        } else {
            // Enable staff mode
            NoClipManager.enableNoClip(player);
            staffModePlayers.add(player);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(ChatColor.AQUA + "You have entered staff mode with flight enabled.");
        }

        return true;
    }
}
