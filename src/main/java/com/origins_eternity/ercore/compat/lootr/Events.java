package com.origins_eternity.ercore.compat.lootr;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
import noobanidus.mods.lootr.block.LootrChestBlock;
import noobanidus.mods.lootr.init.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class Events {
    private static boolean preventDrops = false;

    private static List<ResourceLocation> tables = new ArrayList<>();

    private static ResourceLocation getLootTable(World world) {
        if (tables.isEmpty()) {
            for (ResourceLocation location : LootTableList.getAll()) {
                if (location.toString().contains("chests")) {
                    tables.add(location);
                }
            }
        }
        return tables.get(world.rand.nextInt(tables.size()));
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinWorldEvent event) {
        if (preventDrops && event.getEntity() instanceof EntityItem) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
            Block block = event.getPlacedBlock().getBlock();
            if (block instanceof BlockChest && !(block instanceof LootrChestBlock)) {
                event.getWorld().getTileEntity(event.getPos()).getTileData().setBoolean("place", true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            IBlockState state = world.getBlockState(event.getPos());
            EnumFacing facing = EnumFacing.SOUTH;
            if (state.getPropertyKeys().contains(BlockChest.FACING)) {
                facing = state.getValue(BlockChest.FACING);
            }
            if (state.getBlock() instanceof BlockChest && !(state.getBlock() instanceof LootrChestBlock)) {
                Block chest = state.getBlock().equals(Blocks.TRAPPED_CHEST) ? ModBlocks.TRAPPED_CHEST : ModBlocks.CHEST;
                if (!world.getTileEntity(event.getPos()).getTileData().getBoolean("place")) {
                    preventDrops = true;
                    world.setBlockState(event.getPos(), chest.getDefaultState().withProperty(BlockChest.FACING, facing), 2);
                    preventDrops = false;
                    ((TileEntityLockableLoot) world.getTileEntity(event.getPos())).setLootTable(getLootTable(world), world.rand.nextLong());
                }
            }
        }
    }
}