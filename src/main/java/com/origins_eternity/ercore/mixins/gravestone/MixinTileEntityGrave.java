package com.origins_eternity.ercore.mixins.gravestone;

import com.origins_eternity.ercore.compat.gravestone.IGraveExperience;
import de.maxhenkel.gravestone.tileentity.TileEntityGraveStone;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityGraveStone.class, remap = false)
public abstract class MixinTileEntityGrave implements IGraveExperience {
    @Unique
    private int graveExperience = 0;

    @Override
    public int getGraveExperience() {
        return this.graveExperience;
    }

    @Override
    public void setGraveExperience(int exp) {
        this.graveExperience = exp;
    }

    @Inject(method = "func_189515_b", at = @At("RETURN"))
    public void onWrite(NBTTagCompound compound, CallbackInfoReturnable<NBTTagCompound> cir) {
        compound.setInteger("GraveExperience", this.graveExperience);
    }

    @Inject(method = "func_145839_a", at = @At("RETURN"))
    public void onRead(NBTTagCompound compound, CallbackInfo ci) {
        this.graveExperience = compound.getInteger("GraveExperience");
    }
}