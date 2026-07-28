package com.origins_eternity.ercore.content.block;

import com.origins_eternity.ercore.content.BlockCreator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Blocks {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<Item> BLOCKITEMS = new ArrayList<>();

    public static final Block Copper_Ore = new BlockCreator("Copper_Ore", 5, 2, "pickaxe");
    public static final Block Tin_Ore = new BlockCreator("Tin_Ore", 5, 2, "pickaxe");
    public static final Block Tungsten_Ore = new BlockCreator("Tungsten_Ore", 5, 5, "pickaxe");
    public static final Block Nickel_Ore = new BlockCreator("Nickel_Ore", 5, 3, "pickaxe");
}
