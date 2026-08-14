package com.origins_eternity.ercore.mixins.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockCampfire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.origins_eternity.ercore.compat.pyrotech.RockHelper.isConverting;

@Mixin(value = BlockCampfire.class, remap = false)
public abstract class MixinBlockCampfire {
    @Inject(method = "func_180663_b", at = @At("HEAD"), cancellable = true)
    private void onBreakBlock(World world, BlockPos pos, IBlockState state, CallbackInfo ci) {
        if (isConverting(pos)) {
            ci.cancel();
        }
    }
}