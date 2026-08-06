package com.origins_eternity.ercore.mixins.gravestone;

import com.origins_eternity.ercore.compat.gravestone.IGraveExperience;
import de.maxhenkel.gravestone.GraveProcessor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = GraveProcessor.class, remap = false)
public abstract class MixinGraveProcessor {
    @Shadow
    private EntityLivingBase entity;

    @Shadow
    private BlockPos gravePosition;

    @Inject(method = "placeGraveStone", at = @At("RETURN"), remap = false)
    private void onPlaceGraveStone(List<EntityItem> drops, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && this.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) this.entity;
            TileEntity te = this.entity.world.getTileEntity(this.gravePosition);
            if (te instanceof IGraveExperience) {
                ((IGraveExperience) te).setGraveExperience(player.experienceTotal);

                player.experienceTotal = 0;
                player.experienceLevel = 0;
                player.experience = 0.0F;
            }
        }
    }
}