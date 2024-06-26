package com.origins_eternity.ercore.utils;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.message.SyncEndurance;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;

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

    private static void addPotions(EntityPlayer player, Potion potion) {
        if (!player.isPotionActive(potion)) {
            player.addPotionEffect(new PotionEffect(potion, 100, 1, false, false));
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
        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
        if (endurance.isTired()) {
            player.setSprinting(false);
            addTiredDebuff(player);
            if (endurance.isExhausted()) {
                addExhaustedDebuff(player);
            }
        }
    }

    public static IBlockState getBlockstate(String id, Block origin) {
        ResourceLocation location = new ResourceLocation(id);
        Block block = Block.REGISTRY.getObject(location);
        if (block.equals(Blocks.AIR)) {
            block = origin;
        }

        return block.getDefaultState();
    }

    @Optional.Method(modid = "rtg")
    public static void defaultWorldtype() {
        for(int i = 0; i < WorldType.WORLD_TYPES.length; ++i) {
            if (WorldType.WORLD_TYPES[i] == WorldType.byName("RTG")) {
                WorldType defaultype = WorldType.WORLD_TYPES[0];
                WorldType.WORLD_TYPES[0] = WorldType.WORLD_TYPES[i];
                WorldType.WORLD_TYPES[i] = defaultype;
                break;
            }
        }
    }
}