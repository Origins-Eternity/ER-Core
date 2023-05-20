package com.origins_eternal.ercore.config;

import com.origins_eternal.ercore.ERCore;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class Config {

    public static Configuration config;
    public static int copperVeinSize;
    public static int copperChance;
    public static int copperMinHeight;
    public static int copperMaxHeight;

    public static int tinVeinSize;
    public static int tinChance;
    public static int tinMinHeight;
    public static int tinMaxHeight;

    public static int iridiumVeinSize;
    public static int iridiumChance;
    public static int iridiumMinHeight;
    public static int iridiumMaxHeight;

    public static int rutileVeinSize;
    public static int rutileChance;
    public static int rutileMinHeight;
    public static int rutileMaxHeight;

    public static int sulphurVeinSize;
    public static int sulphurChance;
    public static int sulphurMinHeight;
    public static int sulphurMaxHeight;

    public static int tungstenVeinSize;
    public static int tungstenChance;
    public static int tungstenMinHeight;
    public static int tungstenMaxHeight;
    public static boolean enableEndurance;
    public static boolean showbar;
    public static boolean ores;
    public static boolean blocks;
    public static boolean items;
    public static boolean blueprints;

    public static void init(File file) {

        config = new Configuration(file);

        String endurancecategory = "Endurance";
        config.addCustomCategoryComment(endurancecategory, "Here are endurance options, it can be closed if you feel terrible!");

        enableEndurance = config.getBoolean("Enable Endurance", endurancecategory, true, "Enable or Disable Endurance");
        showbar = config.getBoolean("Show Overlay Bar", endurancecategory, true, "Whether to Show Endurance Bar or Not");

        String contentcategory = "Content";
        config.addCustomCategoryComment(contentcategory, "Custom the blocks or items which are added by the mod, they will not be added by default.");

        ores = config.getBoolean("Enable Ores", contentcategory, false, "Whether To Add Ores or Not");
        blocks = config.getBoolean("Enable Blocks", contentcategory, false, "Whether To Add Blocks or Not");
        items = config.getBoolean("Enable Items", contentcategory, false, "Whether To Add Items or Not");
        blueprints = config.getBoolean("Enable Blueprints", contentcategory, false, "Whether To Add Blueprints or Not");

        String orecategory = "Ore Generation";
        config.addCustomCategoryComment(orecategory, "Custom your ores generation, But do not go out of range!");

        copperVeinSize = config.getInt("Copper Vein Size", orecategory, 6, 0, 10, "Set Copper Vein Size");
        copperChance = config.getInt("Copper Spawn Chance",orecategory, 35, 0, 50, "Set Copper Spawn Chance");
        copperMinHeight = config.getInt("Copper Min Height", orecategory, 0,0,50, "Set Copper Min Spawn Height");
        copperMaxHeight = config.getInt("Copper Max Height", orecategory, 125, 0, 150, "Set Copper Max Spawn Height");

        tinVeinSize = config.getInt("Tin Vein Size", orecategory, 4, 0, 10, "Set Tin Vein Size");
        tinChance = config.getInt("Tin Spawn Chance",orecategory, 30, 0, 50, "Set Tin Spawn Chance");
        tinMinHeight = config.getInt("Tin Min Height", orecategory, 0,0,50, "Set Tin Min Spawn Height");
        tinMaxHeight = config.getInt("Tin Max Height", orecategory, 125, 0, 150, "Set Tin Max Spawn Height");

        iridiumVeinSize = config.getInt("Iridium Vein Size", orecategory, 1, 0, 10, "Set Iridium Vein Size");
        iridiumChance = config.getInt("Iridium Spawn Chance",orecategory, 20, 0, 50, "Set Iridium Spawn Chance");
        iridiumMinHeight = config.getInt("Iridium Min Height", orecategory, 0,0,50, "Set Iridium Min Spawn Height");
        iridiumMaxHeight = config.getInt("Iridium Max Height", orecategory, 30, 0, 150, "Set Iridium Max Spawn Height");

        rutileVeinSize = config.getInt("Rutile Vein Size", orecategory, 5, 0, 10, "Set Rutile Vein Size");
        rutileChance = config.getInt("Rutile Spawn Chance",orecategory, 20, 0, 50, "Set Rutile Spawn Chance");
        rutileMinHeight = config.getInt("Rutile Min Height", orecategory, 0,0,50, "Set Rutile Min Spawn Height");
        rutileMaxHeight = config.getInt("Rutile Max Height", orecategory, 50, 0, 150, "Set Rutile Max Spawn Height");

        sulphurVeinSize = config.getInt("Sulphur Vein Size", orecategory, 10, 0, 10, "Set Sulphur Vein Size");
        sulphurChance = config.getInt("Sulphur Spawn Chance",orecategory, 30, 0, 50, "Set Sulphur Spawn Chance");
        sulphurMinHeight = config.getInt("Sulphur Min Height", orecategory, 20,0,50, "Set Sulphur Min Spawn Height");
        sulphurMaxHeight = config.getInt("Sulphur Max Height", orecategory, 120, 0, 150, "Set Sulphur Max Spawn Height");

        tungstenVeinSize = config.getInt("Tungsten Vein Size", orecategory, 5, 0, 10, "Set Tungsten Vein Size");
        tungstenChance = config.getInt("Tungsten Spawn Chance",orecategory, 40, 0, 50, "Set Tungsten Spawn Chance");
        tungstenMinHeight = config.getInt("Tungsten Min Height", orecategory, 40,0,50, "Set Tungsten Min Spawn Height");
        tungstenMaxHeight = config.getInt("Tungsten Max Height", orecategory, 100, 0, 150, "Set Tungsten Max Spawn Height");

        config.save();
    }

    public static void registerConfig(FMLPreInitializationEvent event) {
        ERCore.config = new File(event.getModConfigurationDirectory() + "/ERCore");
        init(new File(ERCore.config.getPath(), "ERCore.cfg"));
        MinecraftForge.EVENT_BUS.register(config);
    }
}