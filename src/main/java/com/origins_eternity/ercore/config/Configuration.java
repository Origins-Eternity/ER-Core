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

        @Config.Name("Rest")
        @Config.LangKey("config.ercore.rest")
        @Config.Comment("Whether to force player to sleep when exhausted. Even if this is disabled, your debuff will not disappear until the endurance is full!")
        public static boolean forceRest = true;

        @Config.RangeInt()
        @Config.Name("The Bar Offset")
        @Config.LangKey("config.ercore.offset")
        @Config.Comment("Positive values represent upward movement, it's usually a multiple of 10.")
        public static int barOffset = 0;

        @Config.Name("Tools")
        @Config.LangKey("config.ercore.tools")
        @Config.Comment("A list of tools which should consume endurance to use.")
        public static String[] tools = new String[]{"toolHoe", "toolHoe", "toolHoe", "toolShovel", "item.flintAndSteel"};

        @Config.Name("Work Blocks")
        @Config.LangKey("config.ercore.workblocks")
        @Config.Comment("A list of blocks which should consume endurance to interact.")
        public static String[] blocks = new String[]{"grass", "dirt", "tile.grassPath", "grass", "*"};

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

        @Config.Name("Generate Copper Vein Size")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.copper.size")
        @Config.RangeInt(min = 0, max = 10)
        public static int copperVeinSize = 6;

        @Config.Name("Generate Copper Spawn Chance")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.copper.chance")
        @Config.RangeInt(min = 0, max = 50)
        public static int copperChance = 35;

        @Config.Name("Generate Copper Min Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.copper.min")
        @Config.RangeInt(min = 0, max = 256)
        public static int copperMinHeight = 0;

        @Config.Name("Generate Copper Max Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.copper.max")
        @Config.RangeInt(min = 0, max = 256)
        public static int copperMaxHeight = 125;

        @Config.Name("Generate Tin Vein Size")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.tin.size")
        @Config.RangeInt(min = 0, max = 10)
        public static int tinVeinSize = 4;

        @Config.Name("Generate Tin Spawn Chance")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.tin.chance")
        @Config.RangeInt(min = 0, max = 50)
        public static int tinChance = 30;

        @Config.Name("Generate Tin Min Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.tin.min")
        @Config.RangeInt(min = 0, max = 256)
        public static int tinMinHeight = 0;

        @Config.Name("Generate Tin Max Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.tin.max")
        @Config.RangeInt(min = 0, max = 256)
        public static int tinMaxHeight = 125;

        @Config.Name("Generate Iridium Vein Size")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.iridium.size")
        @Config.RangeInt(min = 0, max = 10)
        public static int iridiumVeinSize = 1;

        @Config.Name("Generate Iridium Spawn Chance")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.iridium.chance")
        @Config.RangeInt(min = 0, max = 50)
        public static int iridiumChance = 20;

        @Config.Name("Generate Iridium Min Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.iridium.min")
        @Config.RangeInt(min = 0, max = 256)
        public static int iridiumMinHeight = 0;

        @Config.Name("Generate Iridium Max Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.iridium.max")
        @Config.RangeInt(min = 0, max = 256)
        public static int iridiumMaxHeight = 30;

        @Config.Name("Generate Rutile Vein Size")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.rutile.size")
        @Config.RangeInt(min = 0, max = 10)
        public static int rutileVeinSize = 5;

        @Config.Name("Generate Rutile Spawn Chance")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.rutile.chance")
        @Config.RangeInt(min = 0, max = 50)
        public static int rutileChance = 20;

        @Config.Name("Generate Rutile Min Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.rutile.min")
        @Config.RangeInt(min = 0, max = 50)
        public static int rutileMinHeight = 0;

        @Config.Name("Generate Rutile Max Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.rutile.max")
        @Config.RangeInt(min = 0, max = 256)
        public static int rutileMaxHeight;

        @Config.Name("Generate Sulphur Vein Size")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.sulphur.size")
        @Config.RangeInt(min = 0, max = 10)
        public static int sulphurVeinSize = 10;

        @Config.Name("Generate Sulphur Spawn Chance")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.sulphur.chance")
        @Config.RangeInt(min = 0, max = 50)
        public static int sulphurChance = 30;

        @Config.Name("Generate Sulphur Min Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.sulphur.min")
        @Config.RangeInt(min = 0, max = 256)
        public static int sulphurMinHeight = 20;

        @Config.Name("Generate Sulphur Max Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.sulphur.max")
        @Config.RangeInt(min = 0, max = 256)
        public static int sulphurMaxHeight;

        @Config.Name("Generate Tungsten Vein Size")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.tungsten.size")
        @Config.RangeInt(min = 0, max = 10)
        public static int tungstenVeinSize  = 5;

        @Config.Name("Generate Tungsten Spawn Chance")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.tungsten.chance")
        @Config.RangeInt(min = 0, max = 50)
        public static int tungstenChance = 40;

        @Config.Name("Generate Tungsten Min Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.tungsten.min")
        @Config.RangeInt(min = 0, max = 256)
        public static int tungstenMinHeight = 40;

        @Config.Name("Generate Tungsten Max Height")
        @Config.RequiresMcRestart()
        @Config.LangKey("config.ercore.tungsten.max")
        @Config.RangeInt(min = 0, max = 256)
        public static int tungstenMaxHeight;

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