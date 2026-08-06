package com.origins_eternity.ercore.compat.lootr;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootTableList;

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