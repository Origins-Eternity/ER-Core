package com.origins_eternity.ercore.content.block;

import com.origins_eternity.ercore.content.BlockCreator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Blocks {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<Item> BLOCKITEMS = new ArrayList<>();
    public static final Block Slakedlime_Block = new BlockCreator("Slakedlime_Block", 3, 3, "shovel");
    public static final Block Tungsten_Steel_Block = new BlockCreator("Tungsten_Steel_Block", 5, 5, "pickaxe");
}