package com.origins_eternity.ercore.content;

import com.origins_eternity.ercore.ERCore;
import com.origins_eternity.ercore.content.block.Blocks;
import com.origins_eternity.ercore.content.block.Ores;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.Random;

public class BlockCreator extends Block {
    public BlockCreator(String name, int hardness, int harvestlevel, String toolclass) {
        super(Material.ROCK);
        setTranslationKey(ERCore.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setHardness(hardness);
        setHarvestLevel(toolclass, harvestlevel);
        setSoundType(SoundType.STONE);
        setCreativeTab(ERCore.ERCORE);

        if (name.contains("Ore")) {
            Ores.ORES.add(this);
            Ores.OREITEMS.add(new ItemBlock(this).setRegistryName(name.toLowerCase()));
        } else {
            Blocks.BLOCKS.add(this);
            Blocks.BLOCKITEMS.add(new ItemBlock(this).setRegistryName(name.toLowerCase()));
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }
}