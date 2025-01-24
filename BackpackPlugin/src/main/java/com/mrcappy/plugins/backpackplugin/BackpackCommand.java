package com.mrcappy.plugins.backpackplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BackpackCommand implements CommandExecutor {

    private final BackpackManager backpackManager;

    public BackpackCommand(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0 && args[0].equalsIgnoreCase("upgrade")) {
            UpgradeHandler upgradeHandler = new UpgradeHandler(BackpackPlugin.getInstance(), backpackManager);
            if (!upgradeHandler.upgradeBackpack(player)) {
                player.sendMessage(ChatColor.RED + "Upgrade failed!");
            }
            return true;
        }

        player.openInventory(backpackManager.getBackpack(player));
        player.sendMessage(ChatColor.GREEN + "Backpack opened!");
        return true;
    }
}
