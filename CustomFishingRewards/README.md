# CustomFishingRewards

CustomFishingRewards is a Minecraft plugin designed to enhance the fishing experience by introducing custom loot tables, biome-specific rewards, rare fishing events, and detailed player stats tracking. It is highly configurable and supports live configuration updates.

---

## Features

- **Custom Loot Tables**: Define unique loot items with configurable chances and rarities.
- **Biome-Specific Rewards**: Tailor fishing rewards to different biomes.
- **Rare Events**: Trigger exciting events like mob spawns or player buffs upon rare catches.
- **Player Stats**: Track stats like total catches and rare catches for each player.
- **Dynamic Configuration**: Apply configuration changes in real-time without restarting the server.

---

## Commands

| Command              | Description                              | Permission               |
|----------------------|------------------------------------------|--------------------------|
| `/fishstats`         | Displays your fishing statistics.       | `customfishing.stats`    |
| `/fishreload`        | Reloads the plugin configuration.       | `customfishing.reload`   |
| `/customfishreload`  | Reloads and applies changes to config.  | `customfishing.reload`   |
| `/addloot`           | Add a loot item to a biome.             | `customfishing.manage`   |
| `/removeloot`        | Remove a loot item from a biome.        | `customfishing.manage`   |
| `/listloot`          | Lists all loot for a specific biome.    | `customfishing.manage`   |

---

## Configuration (`config.yml`)

The plugin is fully customizable through the `config.yml` file, which allows you to:
- Define global and biome-specific loot tables.
- Configure rare fishing events and their effects.
- Set general plugin behavior and messaging.

### Example Configuration

```yaml
general:
  broadcast_on_rare_catch: true
  rare_broadcast_message: "{player} just caught a rare item: {item}!"

biomes:
  ocean:
    loot:
      - "TRIDENT:1:LEGENDARY"
      - "HEART_OF_THE_SEA:5:EPIC"
      - "TROPICAL_FISH:50:COMMON"
  plains:
    loot:
      - "IRON_INGOT:20:COMMON"
      - "DIAMOND:5:RARE"

events:
  KrakenEvent:
    chance: 5
    mob_spawn: "ELDER_GUARDIAN"
    message: "&cA Kraken has appeared! Fight it off!"
  BlessingEvent:
    chance: 10
    buffs:
      - "LUCK:300"
      - "SPEED:120"
    message: "&aYou feel the ocean's blessing! Luck and speed are on your side."
