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
            int exp = ((IGraveExperience) te).getGraveExperience();
            while (exp > 0) {
                int drop = Math.min(exp, 50);
                exp -= drop;
                worldIn.spawnEntity(new EntityXPOrb(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
            }
            ((IGraveExperience) te).setGraveExperience(0);
        }
    }
}