package com.origins_eternal.ercore.content;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import static com.origins_eternal.ercore.ERCore.ERCORE;
import static com.origins_eternal.ercore.ERCore.MOD_ID;
import static com.origins_eternal.ercore.content.block.FluidBlocks.FLUIDBLOCKS;
import static com.origins_eternal.ercore.content.block.FluidBlocks.FLUIDITEMS;

public class ClassicCreator extends BlockFluidClassic {
    public ClassicCreator(Fluid fluid) {
        super(fluid, Material.LAVA);
        String fluidname = fluid.getUnlocalizedName().substring(13);
        setTranslationKey(MOD_ID + ".molten_" + fluidname);
        setRegistryName("molten_" + fluidname);
        setCreativeTab(ERCORE);
        FLUIDBLOCKS.add(this);
        FLUIDITEMS.add(new ItemBlock(this).setRegistryName("molten_" + fluidname));
    }
}
