package com.mrcappy.prefixtitles.commands;

import com.mrcappy.prefixtitles.gui.PrefixGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrefixCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has permission or is an operator
        if (player.hasPermission("prefix.use") || player.isOp()) {
            PrefixGUI.openGUI(player, 1); // Pass player and page 1
        } else {
            player.sendMessage("§cYou don't have permission to use this command!");
        }

        return true;
    }
}
