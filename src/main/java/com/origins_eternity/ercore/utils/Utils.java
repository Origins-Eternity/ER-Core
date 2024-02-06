package com.origins_eternity.ercore.utils;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.message.SyncEndurance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

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
        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
        if (endurance.isMove()) {
            if ((player.isRiding()) && (!(player.getRidingEntity() instanceof EntityBoat))) {
                if (!player.isHandActive()) {
                    if (endurance.getCoolDown() > 0) {
                        endurance.removeCoolDown(1);
                    } else {
                        endurance.addSaturation(0.03f);
                    }
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
                    if ((endurance.isTired()) || (endurance.isExhausted())) {
                        endurance.addExhaustion(0.01f);
                    }
                }
            }
        } else {
            if (!player.isHandActive()) {
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
        if (player.isHandActive()) {
            Item item = player.getHeldItem(player.getActiveHand()).getItem();
            if (item.equals(Items.SHIELD)) {
                endurance.addExhaustion(0.01f);
            } else if (item.equals(Items.BOW)) {
                endurance.addExhaustion(0.02f);
            }
            endurance.addCoolDown(10);
        }
    }

    public static void checkStatus(EntityPlayer player) {
        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
        if (endurance.isTired()) {
            player.setSprinting(false);
            if (endurance.isExhausted()) {
                player.setSprinting(false);
                player.addTag("rest");
                if (!player.isPlayerSleeping()) {
                    player.trySleep(new BlockPos(player));
                }
            }
        }
        if ((player.getTags().contains("rest")) && (!endurance.isExhausted())) {
            if (player.isPlayerSleeping()) {
                player.wakeUpPlayer(false, false, false);
            }
            player.removeTag("rest");
        }
    }
}