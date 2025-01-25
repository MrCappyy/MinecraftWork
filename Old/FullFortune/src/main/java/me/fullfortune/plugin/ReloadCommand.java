package me.fullfortune.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final FullFortune plugin;

    public ReloadCommand(FullFortune plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigManager().reloadConfig();
            sender.sendMessage("§aFullFortune configuration reloaded successfully!");
            return true;
        }
        sender.sendMessage("§cUsage: /fullfortune reload");
        return false;
    }
}
