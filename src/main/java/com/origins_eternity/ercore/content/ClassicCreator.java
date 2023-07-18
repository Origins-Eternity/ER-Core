package com.origins_eternity.ercore.content;

import com.origins_eternity.ercore.ERCore;
import com.origins_eternity.ercore.content.block.FluidBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import static com.origins_eternity.ercore.content.tab.CreativeTab.ERCORE;

public class ClassicCreator extends BlockFluidClassic {
    public ClassicCreator(Fluid fluid) {
        super(fluid, Material.LAVA);
        String fluidname = fluid.getUnlocalizedName().substring(13);
        setTranslationKey(ERCore.MOD_ID + ".molten_" + fluidname);
        setRegistryName("molten_" + fluidname);
        setCreativeTab(ERCORE);
        FluidBlocks.FLUIDBLOCKS.add(this);
        FluidBlocks.FLUIDITEMS.add(new ItemBlock(this).setRegistryName("molten_" + fluidname));
    }
}
