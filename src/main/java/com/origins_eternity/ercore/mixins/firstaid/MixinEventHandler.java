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
    private static final Map<Integer, List<Float>> partsHealth = new HashMap<>();

    @Inject(method = "onLogin", at = @At("HEAD"))
    private static void onLogin(PlayerEvent.PlayerLoggedInEvent event, CallbackInfo ci) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            player.addTag("Login");
            PlayerDamageModel  model = (PlayerDamageModel) player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);

            List<Float> health = new ArrayList<>();
            model.forEach(part -> {
                health.add(part.currentHealth);
            });

            partsHealth.put(player.getEntityId(), health);
        }
    }

    @Inject(method = "tickPlayers", at = @At("HEAD"))
    private static void tickPlayers(TickEvent.PlayerTickEvent event, CallbackInfo ci) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote && player.ticksExisted == 3) {
            int id = player.getEntityId();
            if (partsHealth.containsKey(id)) {
                PlayerDamageModel model = (PlayerDamageModel) player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);

                List<AbstractDamageablePart> parts = new ArrayList<>();
                model.forEach(parts::add);

                List<Float> health = partsHealth.get(id);

                for (int i = 0; i < parts.size() && i < health.size(); i++) {
                    parts.get(i).currentHealth = health.get(i);
                }

                model.scheduleResync();
                partsHealth.remove(id);
            }
        }
    }
}