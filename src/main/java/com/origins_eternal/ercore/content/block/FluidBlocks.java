package com.origins_eternal.ercore.content.block;

import com.origins_eternal.ercore.content.ClassicCreator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

import static com.origins_eternal.ercore.content.fluid.Fluids.*;

public class FluidBlocks {
    public static final List<Block> FLUIDBLOCKS = new ArrayList<>();
    public static final List<Item> FLUIDITEMS = new ArrayList<>();

    public static final Block Molten_Obsidian_Magic = new ClassicCreator(Obsidian_Magic);
    public static final Block Molten_Iridium = new ClassicCreator(Iridium);
    public static final Block Molten_Sugarcane = new ClassicCreator(Sugarcane);
    public static final Block Molten_Tungsten = new ClassicCreator(Tungsten);
    public static final Block Molten_Slaked_Lime = new ClassicCreator(Slaked_Lime);
    public static final Block Molten_Tungsten_Carbide = new ClassicCreator(Tungsten_Carbide);
    public static final Block Molten_Tungsten_Steel = new ClassicCreator(Tungsten_Steel);
}
