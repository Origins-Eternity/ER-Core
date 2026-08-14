package com.origins_eternity.ercore.compat.oe;

import com.sirsquidly.oe.Main;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WaterHelper {
    public static IBlockState getCoralReplacement(World world, BlockPos pos, boolean inWater) {
        if (!inWater || world.provider.doesWaterVaporize()) {
            return Blocks.AIR.getDefaultState();
        }
        return getReplacement(world, pos);
    }

    public static IBlockState getReplacement(World world, BlockPos pos) {
        if (Main.proxy.fluidlogged_enable) {
            FluidState fluidState = FluidloggedUtils.getFluidState(world, pos);
            return fluidState.isEmpty() ? Blocks.AIR.getDefaultState() : fluidState.getState();
        }
        return Blocks.WATER.getDefaultState();
    }
}
