package com.origins_eternity.ercore.mixins.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemTinder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.origins_eternity.ercore.compat.pyrotech.RockHelper.Campfire_Rock;
import static com.origins_eternity.ercore.compat.pyrotech.RockHelper.checkSurround;

@Mixin(value = ItemTinder.class, remap = false)
public abstract class MixinItemTinder {
    @Redirect(method = "func_77659_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;func_180501_a(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z"))
    private boolean onSetBlockState(World world, BlockPos pos, IBlockState original, int flags) {
        if (checkSurround(world, pos)) {
            return world.setBlockState(pos, Campfire_Rock.getDefaultState(), flags);
        }
        return world.setBlockState(pos, original, flags);
    }
}
