package com.origins_eternal.ercore.content.block;

import com.origins_eternal.ercore.content.BlockCreator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Ores {
    public static final List<Block> ORES = new ArrayList<>();
    public static final List<Item> OREITEMS = new ArrayList<>();

    public static final Block Copper_Ore = new BlockCreator("Copper_Ore", 5, 2, "pickaxe");
    public static final Block Iridium_Ore = new BlockCreator("Iridium_Ore", 8, 9, "pickaxe");
    public static final Block Rutile_Ore = new BlockCreator("Rutile_Ore", 6, 5, "pickaxe");
    public static final Block Sulphur_Ore = new BlockCreator("Sulphur_Ore", 5, 3, "pickaxe");
    public static final Block Tin_Ore = new BlockCreator("Tin_Ore", 5, 2, "pickaxe");
    public static final Block Tungsten_ore = new BlockCreator("Tungsten_Ore", 5, 8, "pickaxe");
}
