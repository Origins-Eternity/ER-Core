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
            if (player.isRiding()) {
                if (!player.isHandActive()) {
                    endurance.removeCoolDown(10);
                    endurance.addSaturation(0.3f);
                }
                if (player.getRidingEntity() instanceof EntityBoat) {
                    endurance.addCoolDown(1);
                    endurance.addExhaustion(0.1f);
                }
            } else if (player.isSprinting()) {
                endurance.addCoolDown(100);
                endurance.addExhaustion(0.3f);
            }
            if (player.isHandActive()) {
                Item item = player.getHeldItem(player.getActiveHand()).getItem();
                if (item.equals(Items.SHIELD)) {
                    endurance.addExhaustion(0.1f);
                } else if (item.equals(Items.BOW)) {
                    endurance.addExhaustion(0.2f);
                }
                endurance.addCoolDown(40);
            } else {
                endurance.removeCoolDown(10);
                endurance.addSaturation(0.1f);
            }
        } else if (!player.isHandActive()) {
            endurance.removeCoolDown(10);
            endurance.addSaturation(0.2f);
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