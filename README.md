[![Discord](https://img.shields.io/discord/1321045735055163402?logo=discord&color=949af1)](https://discord.gg/JdrzWQvT3v)
[![This is an image](https://cf.way2muchnoise.eu/versions/702132.svg)](https://www.curseforge.com/minecraft/mc-mods/er-core/files)
[![This is an image](https://cf.way2muchnoise.eu/full_702132_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/er-core)
## Introduction
**Add minerals, materials, and useful features needed for technological development to the [Evolution: Reset](https://github.com/Origins-Eternity/Evolution-Reset) modpack.This mod mainly provides services for the modpack, but you can also freely add it to your game if you like some of its features. Utilities are enabled by default, but all items, blocks, fluids and other contents need to be enabled in the configuration file.**  
## TPA Commands
**Add commands for teleportation request and no permissions are required to use these commands. (support tab completion, cross dimension teleportation, notification sound and timeout)**
![This is an image](https://s21.ax1x.com/2025/03/08/pEtatfA.png)
### Usage
- **/tpa \<player>**
- **/tpahere \<player>**
- **/tpaccept [player]**
- **/tpadeny [player]**
## Endurance
**Inspired by the StaminaPlus mod, each survival/adventure player has their own endurance level. Players can use endurance to roll(press R on ground) or perform a double jump(press Space again after jump).**
![This is an image](https://s21.ax1x.com/2025/01/17/pEFjDr8.png)
**Players will get hurt while doing strenuous exercise after exhaustion, like sprint, jump, etc. This can cause to death!**
![This is an image](https://s21.ax1x.com/2025/01/17/pEFjTZF.png)
### Potion
**Add energy potion effect, which can recover 1 sanity every `20 >> amplifier` ticks. Brewed from sugar with potion of jump boost or rabbit foot with potion of speed. During the effect, players won't consume any endurance, except for rolling or double jumping. But once the effect goes, players will get weakness as side effects.**
### Botania
**A new brew type of Energy has been added, which players can brew it in the Botanical Brewery. Using it grants effects of Energy, Strength, and Resistance.**
### CraftTweaker
**Add ZenExpansions for [IPlayer](https://docs.blamejared.com/1.12/en/Vanilla/Players/IPlayer). Developers can call this method on any [IPlayer](https://docs.blamejared.com/1.12/en/Vanilla/Players/IPlayer) object, including its subtypes.**
- **`getEndurance()`: returns player's endurance value as int.**
- **`addEndurance(int value)`: add or remove player's endurance value.**
```zenscript
import crafttweaker.event.PlayerRightClickItemEvent;

events.onPlayerRightClickItem(function(event as PlayerRightClickItemEvent) {
    if(!event.world.isRemote() && event.item.definition.id == "minecraft:fishing_rod") {
        event.player.addEndurance(-1);
    }
});
```
**This *example* script causes player to lose 1 endurance when using a fishing rod.**
## Feature  
**The compatibility of these features is unknown, and some features are designed specifically for the modpack described above. Configurations are already available for some features, which can be highly customizable. If you encounter any problems, please feel free to give feedback.**
### Food Tooltip
**Add a tooltip for foods, which can display food points as integers and saturation as percentages.**
![This is an image](https://s21.ax1x.com/2025/02/06/pEe42rD.png)
### Better Slowness
**Slowness effects can not change the player's FOV now.**
### No Infinite Fluids
**All fluids in the game cannot create new fluid resources, but players can still bottle water with water.**
![This is an image](https://s1.ax1x.com/2022/12/28/pSSP7Sx.png)
### Resourcepack Auto Installation
**Try installing resoucepacks and turn on automatically. Support custom configuration and options.txt!**
### Replace the Products of Lava and Water
**When lava and water meets, the original obsidian will be replaced with basalt blocks. Support custom products.**
### Lootr Fix
**Fix for Lootr. Fixed the empty lootr minecart issue and replace naturally generated chests in the world with Lootr chests. Compatible with most of the structures that added by mods like Ruins.**
### MaxHealth Fix  
**Fix for First Aid. Prevent the maximum health of each part of the player from being reassigned when entering the world, and correctly restore the health saved when the player last logged out.**
### Thirst Overlay Fix
**Fix for Tough As Nails. Enable blending before display thirst overlay, so that prevent the overlay displays strangely in some cases.**
### Thirst Quencher Fix
**Fix for Tough Expansion. When the thirst quencher transfers fluid to other storage tanks, it does not consume the fluid stored within itself, thereby producing infinite fluid. This fix make the thirst quencher reduce its liquids correctly after transferring.**
### Grave Experience
**Allow the grave in GraveStone mod store player's experience.**
### Better ArcFurnace Recipes
**Make the ArcFurnace in Immersive Engineering to match the recipe with additives first when the two recipe inputs are the same, instead of only one of them being able to work properly.**
### Better Ocean Crops Replace
**Restore the previous fluid state after the destruction of ocean crops (kelp, coral and sea pickle) in Oceanic Expanse mod, instead of always creating a water source at the position.**
### Tank Stack Limit
**Limit the stack size of tanks with liquids in Pyrotech mod.**
### Torch Lighters
**Add a custom list for lighters that can use to relight torches in Realistic Torches mod.**
## Content  
**All content is also designed for the above modpack, you can customize what should be added in the configuration file, and all content will not be added by default.**  
### Ores  
**A total of four ores have been added, tin, copper, nickel and tungsten. All ores are only spawned in the Overworld.**
![This is an image](https://s41.ax1x.com/2026/07/27/pmRH1Y9.png)
### Herbs
**The herb is a healing item, which is used in the same way as bandage and plaster in First Aid. Recover half a heart every 23 seconds, recover one heart in total. Only added when First Aid is installed at the same time. Note that this mod does not add any recipes to it.**
### Blueprints  
**Blueprints are the tools needed to craft some items in the above modpack and this mod doesn't add recipes to these blueprints. The textures are come from the Artisan Worktables.**
### Rock Campfire
**Add a rock campfire as an upgraded version of the campfire in Pyrotech mod. Compared to the original campfire, the rock campfire does not turn into ash when the fuel runs out. Its brightness, recipe speed, and effect range have all been improved, and it will not be extinguished by rain.**
### Ingot clay cast  
**Ingot clay cast is the clay version of ingot cast. Only bronze material can use this to make a bronze ingot. Use clay fluid and seared brick to craft it. Like other clay cast, use and craft it will consume the cast. Only added when Tinkers' Construct 2 is installed.**  
### Tinker's material  
**A new tinker's material, Tungsten Steel, has been added, only when Tinkers' Construct 2 is installed. If you enable this, be sure to enable Items, Fluids, and Ores.**
![This is an image](https://s41.ax1x.com/2026/07/27/pmWpLF0.png)
## About  
**This mod has been open source under the Apache License 2.0, you can use, modify, port and distribute this mod under the license. And there are currently no plans to make a mod like this for a new Minecraft version.**