package com.mrcappy.prefixtitles.commands;

import com.mrcappy.prefixtitles.utils.CustomTitleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("prefix.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /prefixadmin <create|assign|add|remove|rename> <args>");
            return true;
        }

        String subCommand = args[0];

        switch (subCommand.toLowerCase()) {
            case "create":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /prefixadmin create <titleName> <player>");
                    return true;
                }
                String titleName = args[1];
                Player owner = Bukkit.getPlayer(args[2]);
                if (owner == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                String titleID = CustomTitleManager.createTitle(titleName, owner);
                sender.sendMessage(ChatColor.GREEN + "Created title '" + titleName + "' with ID: " + titleID);
                owner.sendMessage(ChatColor.GREEN + "You now own the custom title: " + titleName);
                break;

            case "assign":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /prefixadmin assign <player> <titleID>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                String assignTitleID = args[2];
                if (!CustomTitleManager.titleExists(assignTitleID)) {
                    sender.sendMessage(ChatColor.RED + "Title ID does not exist.");
                    return true;
                }
                CustomTitleManager.assignTitleToPlayer(target, assignTitleID);
                sender.sendMessage(ChatColor.GREEN + "Assigned title to " + target.getName());
                target.sendMessage(ChatColor.GREEN + "You have been assigned a new title!");
                break;

            case "rename":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /prefixadmin rename <titleID> <newName>");
                    return true;
                }
                String renameTitleID = args[1];
                String newName = args[2];
                if (CustomTitleManager.renameTitle(renameTitleID, newName)) {
                    sender.sendMessage(ChatColor.GREEN + "Title renamed successfully.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Cannot rename the title. Cooldown may apply.");
                }
                break;

            case "add":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /prefixadmin add <titleID> <player>");
                    return true;
                }
                String addTitleID = args[1];
                Player addPlayer = Bukkit.getPlayer(args[2]);
                CustomTitleManager.addPlayerToTitle(addTitleID, addPlayer);
                sender.sendMessage(ChatColor.GREEN + "Added " + addPlayer.getName() + " to title ID: " + addTitleID);
                break;

            case "remove":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /prefixadmin remove <titleID> <player>");
                    return true;
                }
                String removeTitleID = args[1];
                Player removePlayer = Bukkit.getPlayer(args[2]);
                CustomTitleManager.removePlayerFromTitle(removeTitleID, removePlayer);
                sender.sendMessage(ChatColor.GREEN + "Removed " + removePlayer.getName() + " from title ID: " + removeTitleID);
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown sub-command.");
        }
        return true;
    }
}
