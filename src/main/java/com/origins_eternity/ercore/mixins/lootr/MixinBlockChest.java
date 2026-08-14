package com.origins_eternity.ercore.mixins.lootr;

import com.origins_eternity.ercore.compat.lootr.ChestHelper;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noobanidus.mods.lootr.block.LootrChestBlock;
import noobanidus.mods.lootr.init.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = BlockChest.class)
public abstract class MixinBlockChest {
    @Inject(method = "onBlockAdded", at = @At("RETURN"))
    private void afterBlockAdded(World world, BlockPos pos, IBlockState state, CallbackInfo ci) {
        if (!world.isRemote) {
            if (ChestHelper.isPlayerPlaced(pos) || state.getBlock() instanceof LootrChestBlock) {
                return;
            }

            TileEntity oldTile = world.getTileEntity(pos);

            ResourceLocation lootTable = null;
            if (oldTile instanceof TileEntityLockableLoot) {
                lootTable = ((TileEntityLockableLoot) oldTile).getLootTable();
            }
            if (lootTable == null) {
                List<ResourceLocation> tables = ChestHelper.getChestLootTables();
                lootTable = tables.get(world.rand.nextInt(tables.size()));
            }

            LootrChestBlock replacement = state.getBlock().equals(Blocks.TRAPPED_CHEST) ? ModBlocks.TRAPPED_CHEST : ModBlocks.CHEST;
            world.setBlockState(pos, replacement.getDefaultState().withProperty(BlockChest.FACING, state.getValue(BlockChest.FACING)), 2);

            TileEntity newTile = world.getTileEntity(pos);
            if (newTile instanceof TileEntityLockableLoot) {
                ((TileEntityLockableLoot) newTile).setLootTable(lootTable, world.rand.nextLong());
            }
        }
    }
}