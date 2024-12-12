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

        @Config.Name("Enable Regeneration Buff")
        @Config.LangKey("config.ercore.regeneration")
        @Config.Comment("Whether to add the regeneration buff when player's maxhealth increased.")
        public static boolean enableRegeneration = false;

        @Config.Name("Enable Hardcore")
        @Config.LangKey("config.ercore.hardcore")
        @Config.Comment("Whether damage from drown, lava or lightning should kill player immediately.")
        public static boolean enableHardcore = false;

        @Config.Name("Show Overlay Bar")
        @Config.LangKey("config.ercore.overlay")
        @Config.Comment("Whether to show endurance bar or not.")
        public static boolean showOverlay = true;

        @Config.Name("Product")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.product")
        @Config.Comment("A product of a mixture of water and lava.")
        public static String product = "chisel:basalt";

        @Config.Name("Resourcepacks")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.resourcepacks")
        @Config.Comment("A list of resourcepacks which will be automatically installed.")
        public static String[] resourcepacks = new String[]{"Evolution-Reset-Resource-Pack-1.2.0.zip", "ModernityAdjunct-f3-1.2.zip", "Modernity-f3-3.10.1.zip"};

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