package com.origins_eternity.ercore.mixins.firstaid;

import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.common.EventHandler;
import ichttt.mods.firstaid.common.damagesystem.PlayerDamageModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = EventHandler.class, remap = false)
public abstract class MixinEventHandler {
    @Unique
    private static final Map<Integer, List<Float>> SAVED_HEALTH = new HashMap<>();

    @Inject(method = "onLogin", at = @At("HEAD"))
    private static void onLogin(PlayerEvent.PlayerLoggedInEvent event, CallbackInfo ci) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            player.addTag("Login");
            PlayerDamageModel model = (PlayerDamageModel) player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
            if (model != null) {
                List<Float> health = new ArrayList<>();
                model.forEach(part -> health.add(part.currentHealth));
                SAVED_HEALTH.put(player.getEntityId(), health);
            }
        }
    }

    @Inject(method = "tickPlayers", at = @At("HEAD"))
    private static void tickPlayers(TickEvent.PlayerTickEvent event, CallbackInfo ci) {
        EntityPlayer player = event.player;
        if (player.world.isRemote) return;

        if (player.ticksExisted == 3) {
            List<Float> saved = SAVED_HEALTH.remove(player.getEntityId());
            if (saved != null) {
                PlayerDamageModel model = (PlayerDamageModel) player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
                if (model != null) {
                    List<AbstractDamageablePart> parts = new ArrayList<>();
                    model.forEach(parts::add);
                    for (int i = 0; i < parts.size() && i < saved.size(); i++) {
                        parts.get(i).currentHealth = saved.get(i);
                    }
                    model.scheduleResync();
                }
            }
        }
    }
}