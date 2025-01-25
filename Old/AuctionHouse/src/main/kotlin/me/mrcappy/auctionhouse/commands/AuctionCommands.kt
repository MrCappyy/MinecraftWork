package me.mrcappy.auctionhouse.commands

import me.mrcappy.auctionhouse.gui.AuctionGUI
import me.mrcappy.auctionhouse.gui.FilterGUI
import me.mrcappy.auctionhouse.managers.AuctionManager
import me.mrcappy.auctionhouse.managers.FilterManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AuctionCommands : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command.")
            return true
        }

        val player: Player = sender

        when (args.getOrNull(0)?.lowercase()) {
            "sell" -> {
                if (args.size < 2) {
                    player.sendMessage("§cUsage: /ah sell <starting price>")
                    return true
                }

                val price = args[1].toDoubleOrNull()
                if (price == null || price <= 0) {
                    player.sendMessage("§cInvalid starting price.")
                    return true
                }

                val item = player.inventory.itemInMainHand
                if (item.type.isAir) {
                    player.sendMessage("§cYou must hold an item to list it.")
                    return true
                }

                AuctionManager.listAuction(player, item, price, null, 60 * 60 * 1000)
                player.inventory.removeItem(item)
                player.sendMessage("§aYour item has been listed for $$price.")
            }

            "filter" -> {
                FilterGUI.openFilterGUI(player)
            }

            "search" -> {
                val keyword = args.getOrNull(1) ?: return false.also {
                    player.sendMessage("§cUsage: /ah search <keyword>")
                }
                val results = FilterManager.searchByKeyword(AuctionManager.getActiveAuctions(), keyword)
                AuctionGUI.openFilteredAuctionGUI(player, results)
            }

            "seller" -> {
                val sellerName = args.getOrNull(1) ?: return false.also {
                    player.sendMessage("§cUsage: /ah seller <name>")
                }
                val results = FilterManager.filterBySeller(AuctionManager.getActiveAuctions(), sellerName)
                AuctionGUI.openFilteredAuctionGUI(player, results)
            }

            else -> {
                AuctionGUI.openAuctionGUI(player)
            }
        }

        return true
    }
}
