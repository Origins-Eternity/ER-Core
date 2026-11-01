package com.origins_eternity.ercore.mixins.gravestone;

import com.origins_eternity.ercore.compat.gravestone.IGraveExperience;
import de.maxhenkel.gravestone.blocks.BlockGraveStone;
import de.maxhenkel.gravestone.tileentity.TileEntityGraveStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockGraveStone.class, remap = false)
public abstract class MixinBlockGrave {
    @Inject(method = "func_180663_b", at = @At("HEAD"))
    private void onBreakBlock(World worldIn, BlockPos pos, IBlockState state, CallbackInfo ci) {
        if (worldIn.isRemote) return;
        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof TileEntityGraveStone) {
            int totalExp = ((IGraveExperience) te).getGraveExperience();
            if (totalExp <= 0) return;

            int orbs = Math.min(14, totalExp);
            int perOrb = totalExp / orbs;
            int remainder = totalExp % orbs;

            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;

            for (int i = 0; i < orbs; i++) {
                int value = perOrb + (i < remainder ? 1 : 0);
                if (value <= 0) continue;
                worldIn.spawnEntity(new EntityXPOrb(worldIn, x, y, z, value));
            }
            ((IGraveExperience) te).setGraveExperience(0);
        }
    }
}