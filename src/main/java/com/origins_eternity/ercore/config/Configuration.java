package com.origins_eternity.ercore.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.ercore.ERCore.MOD_ID;

@Config(modid = MOD_ID)
public class Configuration {
        @Config.Name("Compat")
        @Config.Comment("Compat Options")
        @Config.LangKey("config.sanity.compat")
        public static ConfigCompat Compat = new ConfigCompat();

        @Config.Name("Contents")
        @Config.Comment("Content Options")
        @Config.LangKey("config.sanity.contents")
        public static ConfigContents Contents = new ConfigContents();

        @Config.Name("Features")
        @Config.Comment("Features Options")
        @Config.LangKey("config.sanity.features")
        public static ConfigFeatures Features = new ConfigFeatures();

        public static class ConfigCompat {
                @Config.Name("Enable Material")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.material")
                @Config.Comment("Whether to add a new tinker's material or not. (Require enable items, fluids and ores)")
                public boolean enableMaterial = false;

                @Config.Name("Enable Herbs")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.herbs")
                @Config.Comment("Whether to add herbs or not. (Require the First Aid mod)")
                public boolean enableHerbs = false;

                @Config.Name("Enable Lootr Fix")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.lootr")
                @Config.Comment("Whether to fix the empty lootr minecart issue and replace naturally generated chests in world chests with Lootr chests or not.")
                public boolean enableLootr = true;

                @Config.Name("Enable MaxHealth Fix")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.maxhealth")
                @Config.Comment("Whether to fix the issue that player's health changes when login with First Aid mod.")
                public boolean enableMaxHealth = true;

                @Config.Name("Enable Thirst Overlay Fix")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.thirst_overlay")
                @Config.Comment("Whether to fix the issue that thirst overlay in Tough As Nails mod sometimes displays abnormal.")
                public boolean enableThirstOverlay = true;

                @Config.Name("Enable Thirst Quencher Fix")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.thirst_quencher")
                @Config.Comment("Whether to fix the issue that the thirst quencher in TanExpansion mod can produce unlimited fluids.")
                public boolean enableThirstQuencher = true;

                @Config.Name("Enable Grave Experience")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.grave")
                @Config.Comment("Whether to allow the gravestone store player's experience or not.")
                public boolean enableGrave = true;

                @Config.Name("Better ArcFurnace Recipes")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.arc_furnace")
                @Config.Comment("Whether to make arc furnace prefer to match the one with additives when the two recipe inputs are the same.")
                public boolean betterArcFurnace = true;

                @Config.Name("Better Ocean Crops Replace")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.ocean_crops")
                @Config.Comment("Whether to restore the previous fluid state after the ocean crops(kelp, coral and sea pickle) added by Oceanic Expanse are destroyed, instead of creating a water source at the position.")
                public boolean betterOceanCrops = true;

                @Config.Name("Tank Stack Limit")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.tank")
                @Config.Comment("Limit the stack size of tanks with liquids in Pyrotech mod. Set this to 0 to disable.")
                public int tank = 1;

                @Config.Name("Torch Lighters")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.lighters")
                @Config.Comment("The list of lighters that can relight torches in the Realistic Torches mod. ('item;damage')")
                public String[] lighters = new String[]{"pyrotech:flint_and_tinder;1", "pyrotech:bow_drill;1", "pyrotech:bow_drill_durable_spindle;1", "pyrotech:matchstick;1"};
        }

        public static class ConfigContents {
                @Config.Name("Enable Ores")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.ores")
                @Config.Comment("Whether to add ores or not.")
                public boolean enableOres = false;

                @Config.Name("Enable Items")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.items")
                @Config.Comment("Whether to add items or not.")
                public boolean enableItems = false;

                @Config.Name("Enable Blueprints")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.blueprints")
                @Config.Comment("Whether to add blueprints or not.")
                public boolean enableBlueprints = false;

                @Config.Name("Enable Fluids")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.fluids")
                @Config.Comment("Whether to add fluids or not.")
                public boolean enableFluids = false;
        }
        
        public static class ConfigFeatures {
                @Config.Name("Enable TPA")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.tpa")
                @Config.Comment("Whether to add TPA commands or not.")
                public boolean enableTPA = true;

                @Config.Name("Enable Endurance")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.endurance")
                @Config.Comment("Whether to add endurance system or not.")
                public boolean enableEndurance = true;

                @Config.Name("Enable No Infinite Fluids")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.infinite")
                @Config.Comment("Whether to ban infinite fluids or not.")
                public boolean enableNoInfinite = true;

                @Config.Name("Enable Resourcepack Auto Installation")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.installation")
                @Config.Comment("Whether to install the resourspacks in list automatically or not.")
                public boolean enableInstallation = true;

                @Config.Name("Enable Food Tooltip")
                @Config.LangKey("config.ercore.food")
                @Config.Comment("Whether to add a tooltip for foods or not.")
                public boolean enableTooltip = true;

                @Config.Name("Better Slowness")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.slowness")
                @Config.Comment("Whether to prevent the slowness effect from changing player's FOV or not.")
                public boolean betterSlowness = true;

                @Config.Name("Blacklist")
                @Config.LangKey("config.ercore.blacklist")
                @Config.Comment("Whether to make the list of dimensions blacklist or not.")
                public boolean blacklist = false;

                @Config.Name("Dimensions")
                @Config.LangKey("config.ercore.dimensions")
                @Config.Comment("The list of dimensions which can use TPA commands.")
                public int[] dimensions = new int[]{1, 0, -1};

                @Config.Name("Product")
                @Config.LangKey("config.ercore.product")
                @Config.Comment("A product of a mixture of water and lava.")
                public String product = "minecraft:cobblestone";

                @Config.Name("Resourcepacks")
                @Config.RequiresMcRestart()
                @Config.LangKey("config.ercore.resourcepacks")
                @Config.Comment("A list of resourcepacks which will be automatically installed.")
                public String[] resourcepacks = new String[]{};

                @Config.Name("Tools")
                @Config.LangKey("config.ercore.tools")
                @Config.Comment("Use these types of tools will consume endurance.")
                public String[] tools = new String[]{"pickaxe", "axe", "shovel"};
        }

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