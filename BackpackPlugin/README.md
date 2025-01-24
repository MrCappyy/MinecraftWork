# BackpackPlugin

A fully functional Minecraft plugin that provides players with portable inventory backpacks. The backpacks are configurable, upgradable, and persist between sessions.

## Features

- **Portable Backpack**: Players can use the `/backpack` command to open their personal virtual storage.
- **Configurable Size**: The default and maximum sizes of backpacks can be customized via the `config.yml`.
- **Upgradable Backpacks**: Players can upgrade their backpack size by spending experience levels.
- **Persistent Storage**: Backpacks' contents are saved and restored even after server restarts.

---

## Commands

| Command               | Description                          | Permission          |
|-----------------------|--------------------------------------|---------------------|
| `/backpack`           | Opens the player's backpack         | `backpack.use`      |
| `/backpack upgrade`   | Upgrades the player's backpack size | `backpack.upgrade`  |

---

## Permissions

- **`backpack.use`**: Allows players to open their backpacks. (Default: `true`)
- **`backpack.upgrade`**: Allows players to upgrade their backpacks. (Default: `op`)

---

## Configuration (`config.yml`)

```yaml
default-backpack-size: 9
max-backpack-size: 54
upgrade-cost:
  18: 10   # Upgrade to 18 slots costs 10 levels
  27: 20   # Upgrade to 27 slots costs 20 levels
  36: 30   # Upgrade to 36 slots costs 30 levels
  45: 40   # Upgrade to 45 slots costs 40 levels
  54: 50   # Upgrade to 54 slots costs 50 levels
```

- **`default-backpack-size`**: The size of a new player's backpack (in slots).
- **`max-backpack-size`**: The maximum backpack size a player can upgrade to.
- **`upgrade-cost`**: The experience levels required to upgrade to each backpack size.

---

## Installation

1. Download the `BackpackPlugin.jar` file.
2. Place the `.jar` file into your server's `plugins` folder.
3. Start or restart your server to generate the configuration files.
4. Customize the `config.yml` file as needed.
5. Reload or restart the server to apply changes.

---

## How to Use

1. **Open Backpack**:
   - Type `/backpack` to open your personal backpack.
2. **Upgrade Backpack**:
   - Type `/backpack upgrade` to upgrade your backpack size. Make sure you have enough experience levels.
3. **Configuration**:
   - Adjust the backpack size and upgrade costs via `config.yml`.

---

## Code Overview

### Main Classes
- **`BackpackPlugin`**: The main plugin class responsible for initialization.
- **`BackpackManager`**: Handles backpack creation, management, and persistence.
- **`ConfigManager`**: Reads configuration settings.
- **`UpgradeHandler`**: Handles the logic for upgrading backpacks.
- **`BackpackCommand`**: Implements `/backpack` and `/backpack upgrade`.
- **`BackpackListener`**: Listens for inventory events (e.g., saving backpacks on close).

---

## Future Enhancements

- Add file or database-based persistence for backpacks.
- Implement admin commands for managing player backpacks.
- Add GUI for upgrading backpacks.
