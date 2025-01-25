package xyz.talecraft.playercard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerCardPlugin extends JavaPlugin implements TabCompleter {

    private final Map<UUID, PlayerCard> playerCards = new HashMap<>();
    private File playerDataFile;
    private FileConfiguration playerDataConfig;

    @Override
    public void onEnable() {
        // Initialize player data file
        initPlayerDataFile();

        // Load all player data from file
        loadPlayerData();

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new PlayerChatHoverListener(playerCards), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCardGUIListener(), this);

        // Register the command and its tab completer
        if (getCommand("playercard") != null) {
            getCommand("playercard").setExecutor(this);
            getCommand("playercard").setTabCompleter(this);
        }

        getLogger().info(ChatColor.GREEN + "PlayerCard Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Save all player data to file
        savePlayerData();
        getLogger().info(ChatColor.RED + "PlayerCard Plugin has been disabled!");
    }

    /**
     * Initialize the player data file.
     */
    private void initPlayerDataFile() {
        playerDataFile = new File(getDataFolder(), "playercards.yml");
        if (!playerDataFile.exists()) {
            getDataFolder().mkdirs();
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    /**
     * Save all player data to the YAML file.
     */
    private void savePlayerData() {
        for (UUID uuid : playerCards.keySet()) {
            PlayerCard card = playerCards.get(uuid);
            String path = "players." + uuid + ".";

            playerDataConfig.set(path + "name", card.getName());
            playerDataConfig.set(path + "age", card.getAge());
            playerDataConfig.set(path + "discord", card.getDiscord());
            playerDataConfig.set(path + "nation", card.getNation());
            playerDataConfig.set(path + "pronouns", card.getPronouns());
            playerDataConfig.set(path + "race", card.getRace());
            playerDataConfig.set(path + "religion", card.getReligion());
            playerDataConfig.set(path + "wiki", card.getWiki());
        }

        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            getLogger().severe("Could not save player data to file!");
            e.printStackTrace();
        }
    }

    /**
     * Load all player data from the YAML file.
     */
    private void loadPlayerData() {
        if (!playerDataConfig.contains("players")) return;

        for (String uuidStr : playerDataConfig.getConfigurationSection("players").getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            PlayerCard card = new PlayerCard();

            String path = "players." + uuid + ".";
            card.setName(playerDataConfig.getString(path + "name", "Unknown"));
            card.setAge(playerDataConfig.getString(path + "age", "Unknown"));
            card.setDiscord(playerDataConfig.getString(path + "discord", "Unknown"));
            card.setNation(playerDataConfig.getString(path + "nation", "Unknown"));
            card.setPronouns(playerDataConfig.getString(path + "pronouns", "Unknown"));
            card.setRace(playerDataConfig.getString(path + "race", "Unknown"));
            card.setReligion(playerDataConfig.getString(path + "religion", "Unknown"));
            card.setWiki(playerDataConfig.getString(path + "wiki", "Unknown"));

            playerCards.put(uuid, card);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Open the Player Card GUI for self
            UUID uuid = player.getUniqueId();
            PlayerCard card = playerCards.computeIfAbsent(uuid, u -> new PlayerCard());
            PlayerCardGUI.openPlayerCardGUI(player, card);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("lookup")) {
            // Lookup another player's PlayerCard
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }

            UUID targetUUID = target.getUniqueId();
            PlayerCard targetCard = playerCards.computeIfAbsent(targetUUID, u -> new PlayerCard());
            PlayerCardGUI.openPlayerCardGUI(player, targetCard);
            return true;
        }

        if (args.length < 3 || !args[0].equalsIgnoreCase("set")) {
            sender.sendMessage(ChatColor.RED + "Usage: /playercard set <field> <value> or /playercard lookup <username>");
            return true;
        }

        // Handle /playercard set <field> <value>
        UUID uuid = player.getUniqueId();
        String field = args[1].toLowerCase();
        String value = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        PlayerCard card = playerCards.computeIfAbsent(uuid, u -> new PlayerCard());
        switch (field) {
            case "name":
                card.setName(value);
                break;
            case "age":
                card.setAge(value);
                break;
            case "discord":
                card.setDiscord(value);
                break;
            case "nation":
                card.setNation(value);
                break;
            case "pronouns":
                card.setPronouns(value);
                break;
            case "race":
                card.setRace(value);
                break;
            case "religion":
                card.setReligion(value);
                break;
            case "wiki":
                card.setWiki(value);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Invalid field: " + field);
                return true;
        }

        player.sendMessage(ChatColor.GREEN + "Updated " + field + " to: " + value);

        // Save data immediately after updating
        savePlayerData();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Stream.of("set", "lookup")
                    .filter(option -> option.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            return Stream.of("name", "age", "discord", "nation", "pronouns", "race", "religion", "wiki")
                    .filter(field -> field.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("lookup")) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
