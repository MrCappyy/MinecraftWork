package com.mrcappy.villageplugin.commands;

import com.mrcappy.villageplugin.manager.BuffManager;
import com.mrcappy.villageplugin.manager.VillagerManager;
import com.mrcappy.villageplugin.villager.VillagerRole;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetVillagerCommand implements CommandExecutor {
    private final VillagerManager manager;
    private final BuffManager buffManager;

    public SetVillagerCommand(VillagerManager manager, BuffManager buffManager) {
        this.manager = manager;
        this.buffManager = buffManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§eUsage: /setvillager <player> <job>");
            return true;
        }

        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        try {
            VillagerType type = VillagerType.valueOf(args[1].toUpperCase());
            manager.setRole(target, type);
            buffManager.applyBuff(target, type);
            sender.sendMessage("§aSuccessfully assigned " + target.getName() + " the role of " + type.name() + ".");
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cInvalid villager role.");
        }
        return true;
    }
}
