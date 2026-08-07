package com.origins_eternity.ercore.mixins.firstaid;

import ichttt.mods.firstaid.common.damagesystem.PlayerDamageModel;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerDamageModel.class, remap = false)
public abstract class MixinDamageModel {
    @Shadow
    private float prevScaleFactor;

    @Inject(method = "runScaleLogic", at = @At("HEAD"), cancellable = true)
    private void runLoginScaleLogic(EntityPlayer player, CallbackInfo ci) {
        if (player.getTags().contains("Login")) {
            this.prevScaleFactor = player.getMaxHealth() / 20f;
            player.removeTag("Login");
            ci.cancel();
        }
    }
}