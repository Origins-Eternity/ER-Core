package com.origins_eternity.ercore.mixins.oe;

import com.sirsquidly.oe.blocks.BlockKelp;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.origins_eternity.ercore.compat.oe.WaterHelper.getReplacement;

@Mixin(value = BlockKelp.class, remap = false)
public abstract class MixinBlockKelp extends BlockBush {
    @Inject(method = "func_176475_e", at = @At("HEAD"), cancellable = true)
    private void onCheckAndDropBlock(World worldIn, BlockPos pos, IBlockState state, CallbackInfo ci) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, getReplacement(worldIn, pos), 3);
        }
        ci.cancel();
    }

    @Inject(method = "func_176206_d", at = @At("HEAD"), cancellable = true)
    private void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state, CallbackInfo ci) {
        worldIn.setBlockState(pos, getReplacement(worldIn, pos), 3);
        ci.cancel();
    }
}