package com.origins_eternal.ercore.content;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.Random;

import static com.origins_eternal.ercore.ERCore.ERCORE;
import static com.origins_eternal.ercore.ERCore.MOD_ID;
import static com.origins_eternal.ercore.content.block.Blocks.BLOCKITEMS;
import static com.origins_eternal.ercore.content.block.Blocks.BLOCKS;
import static com.origins_eternal.ercore.content.block.Ores.OREITEMS;
import static com.origins_eternal.ercore.content.block.Ores.ORES;

public class BlockCreator extends Block {
    public BlockCreator(String name, int hardness, int harvestlevel, String toolclass) {
        super(Material.ROCK);
        setTranslationKey(MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setHardness(hardness);
        setHarvestLevel(toolclass, harvestlevel);
        setSoundType(SoundType.STONE);
        setCreativeTab(ERCORE);

        if (name.contains("Ore")) {
            ORES.add(this);
            OREITEMS.add(new ItemBlock(this).setRegistryName(name.toLowerCase()));
        } else {
            BLOCKS.add(this);
            BLOCKITEMS.add(new ItemBlock(this).setRegistryName(name.toLowerCase()));
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }
}