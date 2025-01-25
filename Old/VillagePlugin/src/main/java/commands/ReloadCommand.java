package com.mrcappy.villageplugin.commands;

import com.mrcappy.villageplugin.config.ConfigManager;
import com.mrcappy.villageplugin.storage.RoleStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final RoleStorage roleStorage;

    public ReloadCommand(JavaPlugin plugin, RoleStorage roleStorage) {
        this.plugin = plugin;
        this.roleStorage = roleStorage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("villageplugin.admin")) {
            sender.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        // Reload configuration
        plugin.reloadConfig();
        ConfigManager.loadConfig(plugin);
        roleStorage.saveData(); // Ensure any pending changes are saved
        sender.sendMessage("§aVillagePlugin configurations have been reloaded successfully!");

        return true;
    }
}
