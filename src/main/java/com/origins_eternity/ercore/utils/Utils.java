package com.origins_eternity.ercore.utils;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.message.SyncEndurance;
import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.common.damagesystem.PlayerDamageModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.origins_eternity.ercore.ERCore.packetHandler;
import static com.origins_eternity.ercore.content.capability.Capabilities.ENDURANCE;

public class Utils {
    public static void syncEndurance(EntityPlayer player) {
        IEndurance endurance = player.getCapability(ENDURANCE, null);
        Capability<IEndurance> capability = ENDURANCE;
        SyncEndurance message = new SyncEndurance(capability.getStorage().writeNBT(capability, endurance, null));
        packetHandler.sendTo(message, (EntityPlayerMP) player);
    }

    public static void tickUpdate(EntityPlayer player) {
        IEndurance endurance = player.getCapability(ENDURANCE, null);
        endurance.setHealth(player.getHealth());
        if (player.isHandActive()) {
            Item item = player.getHeldItem(player.getActiveHand()).getItem();
            if (item instanceof ItemShield) {
                endurance.addExhaustion(0.1f);
            } else if (item instanceof ItemBow) {
                endurance.addExhaustion(0.2f);
            }
            endurance.addCoolDown(40);
        }
        if (endurance.isMove()) {
            if (player.isSprinting()) {
                endurance.addCoolDown(100);
                endurance.addExhaustion(0.3f);
            } else if (!endurance.isExhausted()) {
                if (player.isRiding()) {
                    endurance.removeCoolDown(10);
                    endurance.addSaturation(0.3f);
                } else {
                    endurance.removeCoolDown(10);
                    endurance.addSaturation(0.1f);
                }
            }
        } else {
            endurance.removeCoolDown(10);
            endurance.addSaturation(0.2f);
        }
        if (Loader.isModLoaded("firstaid")) {
            double maxHealth = player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").getAttributeValue();
            if (maxHealth > endurance.getMaxHealth()) {
                if (Configuration.enableRegeneration) {
                    player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 2));
                }
                endurance.setMaxHealth(maxHealth);
            }
        }
    }

    private static void addPotions(EntityPlayer player, Potion potion) {
        if (!player.isPotionActive(potion)) {
            player.addPotionEffect(new PotionEffect(potion, 312, 2, false, false));
        }
    }

    public static void addTiredDebuff(EntityPlayer player) {
        if (!player.world.isRemote) {
            addPotions(player, MobEffects.SLOWNESS);
        }
    }

    public static void addExhaustedDebuff(EntityPlayer player) {
        if (!player.world.isRemote) {
            addPotions(player, MobEffects.BLINDNESS);
            addPotions(player, MobEffects.HUNGER);
        }
    }

    public static void checkStatus(EntityPlayer player) {
        IEndurance endurance = player.getCapability(ENDURANCE, null);
        if (endurance.isTired()) {
            player.setSprinting(false);
            addTiredDebuff(player);
            if (endurance.isExhausted()) {
                endurance.addCoolDown(312);
                addExhaustedDebuff(player);
            }
        }
    }

    public static Block getBlock(String id, Block origin) {
        ResourceLocation location = new ResourceLocation(id);
        Block block = Block.REGISTRY.getObject(location);
        if (block.equals(Blocks.AIR)) {
            block = origin;
        }

        return block;
    }

    @Optional.Method(modid = "rtg")
    public static void defaultWorldtype() {
        for (int i = 0; i < WorldType.WORLD_TYPES.length; ++i) {
            if (WorldType.WORLD_TYPES[i] == WorldType.byName("RTG")) {
                WorldType defaultype = WorldType.WORLD_TYPES[0];
                WorldType.WORLD_TYPES[0] = WorldType.WORLD_TYPES[i];
                WorldType.WORLD_TYPES[i] = defaultype;
                break;
            }
        }
    }

    @Optional.Method(modid = "firstaid")
    public static void syncHealth(EntityPlayer old, EntityPlayer clone) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                PlayerDamageModel past = (PlayerDamageModel) old.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
                PlayerDamageModel current = (PlayerDamageModel) clone.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
                ArrayList<AbstractDamageablePart> oldParts = new ArrayList<>();
                ArrayList<AbstractDamageablePart> newParts = new ArrayList<>();
                past.forEach(oldParts::add);
                current.forEach(newParts::add);
                for (int i = 0; i < newParts.size(); i++) {
                    newParts.get(i).currentHealth = oldParts.get(i).currentHealth;
                }
                current.scheduleResync();
            }
        };
        timer.schedule(task, 50);
    }

    @Optional.Method(modid = "firstaid")
    public static void syncHealth(ArrayList<Float> health, PlayerDamageModel model) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<AbstractDamageablePart> parts = new ArrayList<>();
                model.forEach(parts::add);
                for (int i = 0; i < parts.size(); i++) {
                    parts.get(i).currentHealth = health.get(i);
                }
                model.scheduleResync();
            }
        };
        timer.schedule(task, 50);
    }
}