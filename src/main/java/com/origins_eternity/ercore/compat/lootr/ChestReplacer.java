package com.origins_eternity.ercore.compat.lootr;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ChestReplacer {
    private static final List<ResourceLocation> lootTables = new ArrayList<>();

    private static final Deque<BlockPos> blockPos = new ArrayDeque<>();

    public static void track(BlockPos pos) {
        blockPos.push(pos);
    }

    public static void untrack() {
        if (!blockPos.isEmpty()) blockPos.pop();
    }

    public static boolean isPlayerPlaced(BlockPos pos) {
        return blockPos.contains(pos);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            Item item = event.getItemStack().getItem();
            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                if (block instanceof BlockChest) {
                    BlockPos pos = event.getPos();
                    IBlockState state = world.getBlockState(pos);

                    BlockPos target;
                    if (state.getBlock().isReplaceable(world, pos)) {
                        target = pos;
                    } else {
                        target = pos.offset(event.getFace());
                    }

                    track(target);
                }
            }
        }
    }

    public static List<ResourceLocation> getChestLootTables() {
        if (lootTables.isEmpty()) {
            for (ResourceLocation location : LootTableList.getAll()) {
                if (location.toString().contains("chests")) {
                    lootTables.add(location);
                }
            }
        }
        return lootTables;
    }
}