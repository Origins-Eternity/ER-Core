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

        @Config.Name("Show Overlay Bar")
        @Config.LangKey("config.ercore.overlay")
        @Config.Comment("Whether to show endurance bar or not.")
        public static boolean showOverlay = true;

        @Config.Name("Resourcepacks")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.resourcepacks")
        @Config.Comment("A list of resourcepacks which will be backed up.")
        public static String[] resourcepacks = new String[]{"Evolution-Reset-Resource-Pack-1.2.0.zip"};

        @Config.Name("Languages")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.languages")
        @Config.Comment("A list of languages which can be detected to switch automatically.")
        public static String[] languages = new String[]{"zh_cn"};

        @Config.Name("Resourcespacks Backup")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.backup")
        @Config.Comment("Whether to backup resourcepacks or not.")
        public static boolean enableBackup = true;

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