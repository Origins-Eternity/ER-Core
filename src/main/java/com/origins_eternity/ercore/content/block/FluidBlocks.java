package com.origins_eternity.ercore.content.block;

import com.origins_eternity.ercore.content.ClassicCreator;
import com.origins_eternity.ercore.content.fluid.Fluids;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class FluidBlocks {
    public static final List<Block> FLUIDBLOCKS = new ArrayList<>();
    public static final List<Item> FLUIDITEMS = new ArrayList<>();

    public static final Block Molten_Sugarcane = new ClassicCreator(Fluids.Sugarcane);
    public static final Block Molten_Tungsten = new ClassicCreator(Fluids.Tungsten);
    public static final Block Molten_Slaked_Lime = new ClassicCreator(Fluids.Slaked_Lime);
    public static final Block Molten_Tungsten_Carbide = new ClassicCreator(Fluids.Tungsten_Carbide);
    public static final Block Molten_Tungsten_Steel = new ClassicCreator(Fluids.Tungsten_Steel);
}
