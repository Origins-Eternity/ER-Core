package com.origins_eternity.ercore.compat.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockCampfire;
import com.origins_eternity.ercore.ERCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCampfireRock extends BlockCampfire {
    public static final String name = "campfire_rock";

    public BlockCampfireRock() {
        super();
        setRegistryName(name);
        setTranslationKey(ERCore.MOD_ID + "." + name);
    }

    @Override
    @Nonnull
    public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB original = super.func_185496_a(state, source, pos);
        TileEntity tile = source.getTileEntity(pos);
        if (tile instanceof TileCampfireRock) {
            if (((TileCampfireRock) tile).getFuelRemaining() > 0) {
                original = new AxisAlignedBB(original.minX, original.minY, original.minZ,
                        original.maxX, original.maxY + 0.2, original.maxZ
                );
            }
        }
        return original;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World worldIn, IBlockState state) {
        return new TileCampfireRock();
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        int original = super.getLightValue(state, world, pos);
        if (original > 0) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileCampfireRock) {
                int ash = ((TileCampfireRock) tile).getAshLevel();
                float ashFactor = 1.0f - (ash / 8.0f * 0.5f);
                original = Math.round(original * 2 * ashFactor);
            }
        }
        return MathHelper.clamp(original, 0, 15);
    }
}