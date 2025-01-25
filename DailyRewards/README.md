# Daily Rewards Plugin

**Daily Rewards** is a Minecraft plugin for Paper servers that implements a streak-based daily login reward system. Players can claim their daily rewards through a GUI, with rewards improving as their streak progresses.

---

## Features

- **Daily Rewards GUI**: Players can access their rewards via `/dailyrewards`.
- **Streak-Based Rewards**: Rewards improve each day as players maintain their login streak.
- **Configurable Cooldowns**: The time interval between claims is customizable.
- **Customizable Loot Tables**: Configure rewards for each streak level in `config.yml`.


---

## Commands & Permissions

| Command      | Description                      | Permission          | Default |
|--------------|----------------------------------|---------------------|---------|
| `/dailyrewards`   | Opens the Daily Rewards GUI.     | `dailyrewards.use`  | `true`  |

---

## Configuration

The plugin uses a `config.yml` file to manage settings. Below is the default configuration:

```yaml
rewards:
  1: "IRON_INGOT:5,COAL:10"
  2: "IRON_INGOT:10,EMERALD:2"
  3: "DIAMOND:2,GOLD_INGOT:5"
  4: "DIAMOND:5,NETHERITE_SCRAP:1"
cooldown: 86400 # Time in seconds (24 hours)
