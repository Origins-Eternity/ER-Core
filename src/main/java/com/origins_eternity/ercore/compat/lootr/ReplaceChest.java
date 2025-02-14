package com.origins_eternity.ercore.compat.lootr;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noobanidus.mods.lootr.init.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class ReplaceChest {
    private static boolean preventDrops = false;

    private static List<ResourceLocation> tables = new ArrayList<>(LootTableList.getAll());

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinWorldEvent event) {
        if (preventDrops && event.getEntity() instanceof EntityItem) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
        if (!event.getWorld().isRemote) {
            if (event.getPlacedBlock().getBlock().equals(Blocks.CHEST)) {
                event.getWorld().getTileEntity(event.getPos()).getTileData().setBoolean("place", true);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            IBlockState state = world.getBlockState(event.getPos());
            EnumFacing facing = EnumFacing.SOUTH;
            if (state.getPropertyKeys().contains(BlockChest.FACING)) {
                facing = state.getValue(BlockChest.FACING);
            }
            if (state.getBlock().equals(Blocks.CHEST) && !world.getTileEntity(event.getPos()).getTileData().getBoolean("place")) {
                preventDrops = true;
                world.setBlockState(event.getPos(), ModBlocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing), 2);
                preventDrops = false;
                ((TileEntityLockableLoot) world.getTileEntity(event.getPos())).setLootTable(tables.get(world.rand.nextInt(tables.size())), world.rand.nextLong());
            }
        }
    }
}