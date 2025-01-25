package me.mrcappy.auctionhouse.commands

import me.mrcappy.auctionhouse.managers.AuctionManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class AuctionAdminCommands : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("auctionhouse.admin")) {
            sender.sendMessage("§cYou do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§eAdmin Commands: /ahadmin [remove|force|logs|reload]")
            return true
        }

        when (args[0].lowercase()) {
            "remove" -> {
                if (args.size < 2) {
                    sender.sendMessage("§cUsage: /ahadmin remove <auction-id>")
                    return true
                }

                val auctionId = try { UUID.fromString(args[1]) } catch (e: Exception) {
                    sender.sendMessage("§cInvalid auction ID.")
                    return true
                }

                val auction = AuctionManager.getAuctionById(auctionId)
                if (auction == null) {
                    sender.sendMessage("§cAuction not found.")
                    return true
                }

                AuctionManager.forceRemoveAuction(auctionId)
                sender.sendMessage("§aAuction for ${auction.item.type} has been removed.")
            }

            "force" -> {
                if (args.size < 2) {
                    sender.sendMessage("§cUsage: /ahadmin force <auction-id>")
                    return true
                }

                val auctionId = try { UUID.fromString(args[1]) } catch (e: Exception) {
                    sender.sendMessage("§cInvalid auction ID.")
                    return true
                }

                val auction = AuctionManager.getAuctionById(auctionId)
                if (auction == null) {
                    sender.sendMessage("§cAuction not found.")
                    return true
                }

                AuctionManager.forceSellAuction(auctionId)
                sender.sendMessage("§aAuction for ${auction.item.type} has been forcibly sold.")
            }

            "logs" -> {
                AuctionManager.getAuctionLogs().forEach { log ->
                    sender.sendMessage("§7$log")
                }
            }

            "reload" -> {
                AuctionManager.reloadConfig()
                sender.sendMessage("§aAuctionHouse configuration reloaded.")
            }

            else -> sender.sendMessage("§eUsage: /ahadmin [remove|force|logs|reload]")
        }

        return true
    }
}
