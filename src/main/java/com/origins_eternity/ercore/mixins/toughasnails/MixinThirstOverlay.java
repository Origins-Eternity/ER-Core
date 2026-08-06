package com.origins_eternity.ercore.mixins.toughasnails;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.handler.thirst.ThirstOverlayHandler;

@Mixin(value = ThirstOverlayHandler.class, remap = false)
public class MixinThirstOverlay {
    @Inject(method = "onPreRenderOverlay", at = @At("HEAD"))
    private void onPreRenderOverlay(RenderGameOverlayEvent.Pre event, CallbackInfo ci) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
            GlStateManager.enableBlend();
        }
    }
}