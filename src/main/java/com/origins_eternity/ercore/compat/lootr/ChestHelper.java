package com.origins_eternity.ercore.compat.lootr;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.ArrayList;
import java.util.List;

public class ChestHelper {
    private static final List<ResourceLocation> TABLES = new ArrayList<>();

    private static final List<BlockPos> PLACING = new ArrayList<>();

    public static void startTracking(BlockPos pos) {
        PLACING.add(pos);
    }

    public static void endTracking(BlockPos pos) {
        PLACING.remove(pos);
    }

    public static boolean isPlayerPlaced(BlockPos pos) {
        return PLACING.contains(pos);
    }

    public static List<ResourceLocation> getChestLootTables() {
        if (TABLES.isEmpty()) {
            for (ResourceLocation location : LootTableList.getAll()) {
                if (location.toString().contains("chests")) {
                    TABLES.add(location);
                }
            }
        }
        return TABLES;
    }
}