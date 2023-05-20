package com.origins_eternal.ercore.content.block;

import com.origins_eternal.ercore.content.BlockCreator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Blocks {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<Item> BLOCKITEMS = new ArrayList<>();

    public static final Block Basalt_Coaltar = new BlockCreator("Basalt_Coaltar", 4, 4, "pickaxe");
    public static final Block Slakedlime_Block = new BlockCreator("Slakedlime_Block", 3, 3, "shovel");
    public static final Block Obsidian_Magic = new BlockCreator("Obsidian_Magic", 5, 4, "pickaxe");
}