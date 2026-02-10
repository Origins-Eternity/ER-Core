package com.origins_eternity.ercore.compat.firstaid;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.common.damagesystem.PlayerDamageModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;

import static com.origins_eternity.ercore.utils.Utils.syncHealth;

public class FirstAidEvents {
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
            Capability<IEndurance> capability = Capabilities.ENDURANCE;
            IEndurance origin = old.getCapability(capability, null);
            IEndurance present = clone.getCapability(capability, null);
            present.setMaxHealth(origin.getMaxHealth());
            if (!event.isWasDeath()) {
                syncHealth(old, clone);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isCreative()) {
            if (!player.world.isRemote) {
                IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                double maxHealth = player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").getAttributeValue();
                endurance.setMaxHealth(maxHealth);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingEntityUseItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (!player.isCreative()) {
                if (event.getItem().getItem() instanceof ItemFood) {
                    IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                    double maxHealth = player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").getAttributeValue();
                    if (maxHealth > endurance.getMaxHealth()) {
                        if (Configuration.enableRegeneration) {
                            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 2));
                        }
                        endurance.setMaxHealth(maxHealth);
                    }
                }
            }
        }
    }
}