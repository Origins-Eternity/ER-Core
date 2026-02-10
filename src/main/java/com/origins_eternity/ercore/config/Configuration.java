package com.origins_eternity.ercore.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.ercore.ERCore.MOD_ID;

@Config(modid = MOD_ID)
public class Configuration {
        @Config.Name("Enable Ores")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.ores")
        @Config.Comment("Whether to add ores or not.")
        public static boolean enableOres = false;

        @Config.Name("Enable Blocks")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.blocks")
        @Config.Comment("Whether to add blocks or not.")
        public static boolean enableBlocks = false;

        @Config.Name("Enable Items")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.items")
        @Config.Comment("Whether to add items or not.")
        public static boolean enableItems = false;

        @Config.Name("Enable Material")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.material")
        @Config.Comment("Whether to add material or not.(Require enable items, fluids and ores!)")
        public static boolean enableMaterial = false;

        @Config.Name("Enable Blueprints")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.blueprints")
        @Config.Comment("Whether to add blueprints or not.")
        public static boolean enableBlueprints = false;

        @Config.Name("Enable Fluids")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.fluids")
        @Config.Comment("Whether to add fluids or not.")
        public static boolean enableFluids = false;

        @Config.Name("Enable Herbs")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.herbs")
        @Config.Comment("Whether to add herbs or not. Only work for Fisrt Aid!")
        public static boolean enableHerbs = false;

        @Config.Name("Enable Hardcore")
        @Config.LangKey("config.ercore.hardcore")
        @Config.Comment("Whether damage from drown, lava or lightning should kill player immediately.")
        public static boolean enableHardcore = false;

        @Config.Name("Enable Commands")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.commands")
        @Config.Comment("Whether to add tpa commands or not.")
        public static boolean enableCommands = true;

        @Config.Name("Enable Endurance")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.endurance")
        @Config.Comment("Whether to add endurance system or not.")
        public static boolean enableEndurance = true;

        @Config.Name("Enable Default World Type")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.worldtype")
        @Config.Comment("Whether to change default world type to RTG or not.")
        public static boolean enableWorldType = true;

        @Config.Name("Enable No Infinite Fluids")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.infinite")
        @Config.Comment("Whether to ban infinite fluids or not.")
        public static boolean enableNoInfinite = true;

        @Config.Name("Enable Regeneration Buff")
        @Config.LangKey("config.ercore.regeneration")
        @Config.Comment("Whether to add the regeneration buff when player's maxhealth increased or not.")
        public static boolean enableRegeneration = true;

        @Config.Name("Enable Resourcepack Auto Installation")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.installation")
        @Config.Comment("Whether to install the resourspacks in list automatically or not.")
        public static boolean enableInstallation = true;

        @Config.Name("Enable Food Tooltip")
        @Config.LangKey("config.ercore.food")
        @Config.Comment("Whether to add a tooltip for foods or not.")
        public static boolean enableTooltip = true;

        @Config.Name("Enable Replace Chest")
        @Config.LangKey("config.ercore.replace")
        @Config.Comment("Whether to replace naturally generated chests in world chests with loot chests or not.")
        public static boolean enableReplace = true;

        @Config.Name("Enable Maxhealth Fix")
        @Config.LangKey("config.ercore.maxhealth")
        @Config.Comment("Whether to fix the maxheath issues for Fisrt Aid or not.")
        public static boolean enableMaxHealth = true;

        @Config.Name("Product")
        @Config.LangKey("config.ercore.product")
        @Config.Comment("A product of a mixture of water and lava.")
        public static String product = "minecraft:cobblestone";

        @Config.Name("Resourcepacks")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.resourcepacks")
        @Config.Comment("A list of resourcepacks which will be automatically installed.")
        public static String[] resourcepacks = new String[]{};

        @Config.Name("Tools")
        @Config.LangKey("config.ercore.tools")
        @Config.Comment("Use these types of tools will consume endurance.")
        public static String[] tools = new String[]{"pickaxe", "axe", "shovel"};

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MOD_ID)) {
                ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}