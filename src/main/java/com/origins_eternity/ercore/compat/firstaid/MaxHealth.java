package com.origins_eternity.ercore.compat.firstaid;

import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.common.damagesystem.PlayerDamageModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;

import static com.origins_eternity.ercore.utils.Utils.syncHealth;

public class MaxHealth {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.world.isRemote) {
            EntityPlayer player = event.player;
            PlayerDamageModel model = (PlayerDamageModel) player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
            ArrayList<Float> health = new ArrayList<>();
            model.forEach(abstractDamageablePart -> health.add(abstractDamageablePart.currentHealth));
            syncHealth(health, model);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        EntityPlayer old = event.getOriginal();
        EntityPlayer clone = event.getEntityPlayer();
        if (!clone.world.isRemote) {
            if (!event.isWasDeath()) {
                syncHealth(old, clone);
            }
        }
    }
}