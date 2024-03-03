package com.origins_eternity.ercore.utils;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.message.SyncEndurance;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Optional;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.texture.MetalTextureTexture;
import slimeknights.tconstruct.library.materials.Material;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
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
            } else if (endurance.isTired()) {
                endurance.addCoolDown(50);
                endurance.addExhaustion(0.1f);
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

    public static void addDebuff(EntityPlayer player) {
        if (!player.world.isRemote) {
            if (!player.isPotionActive(MobEffects.HUNGER)) {
                player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 100, 1, false, false));
            }
            if (!player.isPotionActive(MobEffects.SLOWNESS)) {
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 1, false, false));
            }
            if (!player.isPotionActive(MobEffects.WEAKNESS)) {
                player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 1, false, false));
            }
        }
    }

    public static void checkStatus(EntityPlayer player) {
        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
        if (endurance.isTired()) {
            player.setSprinting(false);
            if (endurance.isExhausted()) {
                addDebuff(player);
            }
        }
    }

    @Optional.Method(modid = "tconstruct")
    public static void setRenderInfo(Material material, Fluid fluid) {
        material.setRenderInfo(new MaterialRenderInfo.AbstractMaterialRenderInfo() {
            @Override
            public TextureAtlasSprite getTexture(ResourceLocation resourceLocation, String location) {
                return new MetalTextureTexture(new ResourceLocation(MOD_ID + ":materials/" + material.getIdentifier()), resourceLocation, location, fluid.getColor(), 2f, 3f, 0f);
            }
        });
    }
}