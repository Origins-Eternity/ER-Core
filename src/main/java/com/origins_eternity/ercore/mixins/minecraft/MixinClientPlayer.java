package com.origins_eternity.ercore.mixins.minecraft;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(value = AbstractClientPlayer.class)
public abstract class MixinClientPlayer {
    private static final UUID SLOWNESS = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");

    @Redirect(method = "getFovModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/IAttributeInstance;getAttributeValue()D"))
    private double onGetFovModifier(IAttributeInstance instance) {
        double base = instance.getBaseValue();
        for (AttributeModifier modifier : instance.getModifiersByOperation(0)) {
            if (modifier.getID().equals(SLOWNESS)) continue;
            base += modifier.getAmount();
        }

        double value = base;
        for (AttributeModifier modifier : instance.getModifiersByOperation(1)) {
            if (modifier.getID().equals(SLOWNESS)) continue;
            value += base * modifier.getAmount();
        }
        for (AttributeModifier modifier : instance.getModifiersByOperation(2)) {
            if (modifier.getID().equals(SLOWNESS)) continue;
            value *= (1.0D + modifier.getAmount());
        }

        return instance.getAttribute().clampValue(value);
    }
}