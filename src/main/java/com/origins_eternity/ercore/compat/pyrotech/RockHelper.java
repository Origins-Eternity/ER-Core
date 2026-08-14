package com.origins_eternity.ercore.compat.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockCampfire;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static com.origins_eternity.ercore.ERCore.MOD_ID;

public class RockHelper {
    private static final List<BlockPos> CONVERTING = new ArrayList<>();

    public static final int[][] OFFSETS = {
            { 0, -1},
            {-1, -1},
            {-1,  0},
            {-1,  1},
            { 0,  1},
            { 1,  1},
            { 1,  0},
            { 1, -1}
    };

    public static final AxisAlignedBB[] AABBS = new AxisAlignedBB[]{
            new AxisAlignedBB(4/16f, 0/16f, 2/16f, 12/16f, 4/16f, 8/16f),
            new AxisAlignedBB(0/16f, 0/16f, 0/16f, 6/16f, 4/16f, 6/16f),
            new AxisAlignedBB(2/16f, 0/16f, 4/16f, 8/16f, 4/16f, 12/16f),
            new AxisAlignedBB(0/16f, 0/16f, 10/16f, 6/16f, 4/16f, 16/16f),
            new AxisAlignedBB(4/16f, 0/16f, 8/16f, 12/16f, 4/16f, 14/16f),
            new AxisAlignedBB(10/16f, 0/16f, 10/16f, 16/16f, 4/16f, 16/16f),
            new AxisAlignedBB(8/16f, 0/16f, 4/16f, 14/16f, 4/16f, 12/16f),
            new AxisAlignedBB(10/16f, 0/16f, 0/16f, 16/16f, 4/16f, 6/16f),
    };

    public static BlockRock Rock = new BlockRock();

    public static BlockCampfireRock Campfire_Rock = new BlockCampfireRock();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(Rock);
        event.getRegistry().register(Campfire_Rock);
        GameRegistry.registerTileEntity(TileCampfireRock.class, new ResourceLocation(MOD_ID, "campfire_rock"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(Rock).setRegistryName("rock"));
        event.getRegistry().register(new ItemBlock(Campfire_Rock).setRegistryName("campfire_rock"));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Rock), 0, new ModelResourceLocation(Rock.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Campfire_Rock), 0, new ModelResourceLocation(Campfire_Rock.getRegistryName(), "ash=0,variant=item"));
    }

    public static boolean checkSurround(World world, BlockPos center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                BlockPos pos = center.add(dx, 0, dz);
                IBlockState state = world.getBlockState(pos);
                if (!(state.getBlock() instanceof BlockRock)) {
                    return false;
                } else if (!state.getValue(BlockRock.BOUND)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void convertCampfire(World world, BlockPos center) {
        TileEntity oldTile = world.getTileEntity(center);
        IBlockState oldState = world.getBlockState(center);
        NBTTagCompound nbt = oldTile.writeToNBT(new NBTTagCompound());

        BlockCampfire replace = oldTile instanceof TileCampfireRock ? ModuleTechBasic.Blocks.CAMPFIRE : Campfire_Rock;
        IBlockState newState = replace.getDefaultState().withProperty(BlockCampfire.VARIANT, oldState.getValue(BlockCampfire.VARIANT)).withProperty(BlockCampfire.ASH, oldState.getValue(BlockCampfire.ASH));

        CONVERTING.add(center);
        world.setBlockState(center, newState, 3);
        CONVERTING.remove(center);

        TileEntity newTile = world.getTileEntity(center);
        if (newTile instanceof TileCampfire) {
            newTile.readFromNBT(nbt);
        }
    }

    public static boolean isConverting(BlockPos pos) {
        return CONVERTING.contains(pos);
    }
}
