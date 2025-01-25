# One-Block Skyblock

**One-Block Skyblock** is a Minecraft plugin for creating a unique survival experience where players start with a single regenerating block that progresses through tiers of resources. Over time, the block evolves, introducing new materials, mobs, and eventually, a fully random block pool.

## Features

- **Regenerating Blocks**: A single block that regenerates into a new block when broken.
- **Progression System**: Players progress through tiers, starting with grass and moving to wood, stone, ores, and more.
- **Random Blocks**: After completing progression, the block becomes fully random.
- **Mob Spawning**: Mobs can spawn randomly when breaking blocks, adding an extra challenge.
- **Dynamic Block Pool**: Includes every breakable block in Minecraft, excluding unbreakable blocks (e.g., Bedrock, Barrier).
- **Dynamic Mob Pool**: Includes every spawnable mob in Minecraft.
- **Player Progress Tracking**: Tracks how many blocks players have broken and notifies them about their progress.
- **Configurable Settings**: Control progression steps and mob spawn chances via `config.yml`.

---

## Commands

| Command                  | Description                                      | Permission               |
|--------------------------|--------------------------------------------------|--------------------------|
| `/startoneblock`         | Start your One-Block Skyblock adventure.         | `oneblockskyblock.use`   |
| `/leaveoneblock`         | Leave the One-Block Skyblock world.              | `oneblockskyblock.use`   |
| `/resetoneblock`         | Reset your island.                               | `oneblockskyblock.use`   |
| `/adminresetoneblock <player>` | Reset another player's island (with confirmation). | `oneblockskyblock.admin` |

---

## Permissions

- **`oneblockskyblock.use`**: Allows players to use basic commands (`/startoneblock`, `/leaveoneblock`, `/resetoneblock`).
- **`oneblockskyblock.admin`**: Grants access to admin commands (`/adminresetoneblock`).

---

## Configuration

The `config.yml` file allows you to customize key settings:

```yaml
# General settings
progression:
  blocks_to_next_level: 50  # Number of blocks to break before progressing to the next level
mob_spawn_chance: 5         # Percentage chance to spawn a mob on block break

# World settings
island:
  distance: 100  # Distance between islands
  height: 64     # Y-coordinate for the islands
  default_block: GRASS_BLOCK  # Starting block
```

---

## Progression System

1. **Starting Tier**: Players begin with grass and dirt blocks.
2. **Advancing Tiers**: After breaking a set number of blocks, new materials (e.g., wood, stone, ores) are introduced.
3. **Random Tier**: Once all tiers are completed, the block pool becomes fully random.
4. **Player Notifications**: Players are notified when they progress to a new tier and how many blocks remain until the next.

---

## Technical Details

### **Unbreakable Blocks**
The following blocks are excluded from the block pool:
- `BEDROCK`
- `END_PORTAL_FRAME`
- `END_PORTAL`
- `NETHER_PORTAL`
- `COMMAND_BLOCK`
- `CHAIN_COMMAND_BLOCK`
- `REPEATING_COMMAND_BLOCK`
- `STRUCTURE_BLOCK`
- `STRUCTURE_VOID`
- `JIGSAW`
- `BARRIER`

### **Spawnable Mobs**
All spawnable, living mobs are included. Rare mobs like the `ENDER_DRAGON` and `WITHER` are excluded for gameplay balance.

---

## Known Issues

- None reported at this time.
