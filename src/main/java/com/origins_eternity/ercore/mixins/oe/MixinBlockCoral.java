package com.origins_eternity.ercore.mixins.oe;

import com.sirsquidly.oe.blocks.BlockCoral;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.origins_eternity.ercore.compat.oe.WaterReplacer.getCoralReplacement;

@Mixin(value = BlockCoral.class, remap = false)
public abstract class MixinBlockCoral {
    @Shadow
    public abstract boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state);

    @Inject(method = "func_176206_d", at = @At("HEAD"), cancellable = true)
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state, CallbackInfo ci) {
        worldIn.setBlockState(pos, getCoralReplacement(worldIn, pos, state, state.getValue(BlockCoral.IN_WATER)), 3);
        ci.cancel();
    }

    @Inject(method = "checkForDrop", at = @At("HEAD"), cancellable = true)
    private void checkForDrop(World worldIn, BlockPos pos, IBlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (this.canBlockStay(worldIn, pos, state)) {
            cir.setReturnValue(true);
            return;
        }
        worldIn.setBlockState(pos, getCoralReplacement(worldIn, pos, state, state.getValue(BlockCoral.IN_WATER)), 3);
        cir.setReturnValue(false);
    }
}