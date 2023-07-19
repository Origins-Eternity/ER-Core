package com.origins_eternity.ercore.utils;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.message.SyncEndurance;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import static com.origins_eternity.ercore.ERCore.packetHandler;
import static com.origins_eternity.ercore.content.capability.Capabilities.ENDURANCE;

public class Utils {
    public static IBlockState getBlockstate(String id, Block origin) {
        Block block;
        ResourceLocation location = new ResourceLocation(id);
        block = Block.REGISTRY.getObject(location);
        if (block.equals(Blocks.AIR)) {
            block = origin;
        }
        return block.getDefaultState();
    }

    public static void syncEndurance(EntityPlayer player) {
        IEndurance endurance = player.getCapability(ENDURANCE, null);
        Capability<IEndurance> capability = ENDURANCE;
        SyncEndurance message = new SyncEndurance(capability.getStorage().writeNBT(capability, endurance, null));
        packetHandler.sendTo(message, (EntityPlayerMP) player);
    }

    public static void addDebuff(EntityPlayer player) {
        if (!player.world.isRemote) {
            IEndurance endurance = player.getCapability(ENDURANCE, null);
            if (endurance.isTired()) {
                player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1, 1, false, false));
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1, 1, false, false));
                player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 1, 1, false, false));
            }
            if (endurance.isExhausted()) {
                player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 1, 1, false, false));
                player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 1, 1, false, false));
                player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 1, 1, false, false));
            }
        }
    }

    public static void tickUpdate(EntityPlayer player) {
        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
        if (endurance.isMove()) {
            if ((player.isRiding()) && (!(player.getRidingEntity() instanceof EntityBoat))) {
                if (endurance.getCoolDown() > 0) {
                    endurance.removeCoolDown(1);
                } else {
                    endurance.addSaturation(0.03f);
                }
            } else {
                endurance.addCoolDown(10);
                if (player.getRidingEntity() instanceof EntityBoat) {
                    endurance.addExhaustion(0.01f);
                } else if (player.isInWater()) {
                    endurance.addExhaustion(0.02f);
                } else if ((player.isElytraFlying()) || (player.isOnLadder())) {
                    endurance.addExhaustion(0.01f);
                } else if (player.isSprinting()) {
                    endurance.addExhaustion(0.03f);
                } else if (player.onGround) {
                    if (endurance.isTired()) {
                        endurance.addExhaustion(0.01f);
                    }
                }
            }
        } else {
            if (endurance.getCoolDown() > 0) {
                endurance.removeCoolDown(1);
            } else {
                if (player.isInWater()) {
                    endurance.addSaturation(0.02f);
                } else if (player.onGround) {
                    endurance.addSaturation(0.01f);
                } else if (player.isRiding()) {
                    endurance.addSaturation(0.03f);
                }
            }
        }
    }

    public static void checkStatus(EntityPlayer player) {
        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
        if (endurance.isTired()) {
            player.setSprinting(false);
            addDebuff(player);
        }
        if (endurance.isExhausted()) {
            addDebuff(player);
            if (Configuration.forceRest) {
                player.addTag("rest");
                if (!player.isPlayerSleeping()) {
                    player.trySleep(new BlockPos(player));
                }
            }
        }
        if ((!endurance.isExhausted()) && (player.getTags().contains("rest")) && (Configuration.forceRest)) {
            if (player.isPlayerSleeping()) {
                player.wakeUpPlayer(false, false, false);
            }
            player.removeTag("rest");
        }
    }
}