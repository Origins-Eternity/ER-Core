package com.origins_eternity.ercore.utils;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.content.potion.Potions;
import com.origins_eternity.ercore.message.SyncEndurance;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.origins_eternity.ercore.ERCore.packetHandler;
import static com.origins_eternity.ercore.config.Configuration.Features;
import static com.origins_eternity.ercore.content.capability.Capabilities.ENDURANCE;
import static com.origins_eternity.ercore.content.damage.Damages.EXHAUSTED;

public class Utils {
    public static void installResourcepacks() {
        Minecraft mc = Minecraft.getMinecraft();
        ResourcePackRepository Repository = mc.getResourcePackRepository();
        Repository.updateRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> Packs = Repository.getRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> Resourcepacks = new ArrayList<>();
        Resourcepacks.addAll(Repository.getRepositoryEntries());
        for (ResourcePackRepository.Entry pack : Packs) {
            for (String name : Features.resourcepacks) {
                if (pack.getResourcePackName().equals(name)) {
                    if (!Resourcepacks.contains(pack)) {
                        Resourcepacks.add(pack);
                    }
                }
            }
            Repository.setRepositories(Resourcepacks);
        }
    }

    public static void syncEndurance(EntityPlayer player) {
        IEndurance endurance = player.getCapability(ENDURANCE, null);
        Capability<IEndurance> capability = ENDURANCE;
        SyncEndurance message = new SyncEndurance(capability.getStorage().writeNBT(capability, endurance, null));
        packetHandler.sendTo(message, (EntityPlayerMP) player);
    }

    public static void tickUpdate(EntityPlayer player) {
        IEndurance endurance = player.getCapability(ENDURANCE, null);
        if (endurance.getEnergetic()) {
            endurance.setMaxEndurance(40);
            if (!player.isPotionActive(Potions.Energy)) {
                player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 600, 0));
                endurance.setEnergetic(false);
            }
            return;
        }
        int maxEndurance = (int) (player.getHealth() * 2);
        if (endurance.getMaxEndurance() > maxEndurance) {
            endurance.setMaxEndurance(Math.max(20, maxEndurance));
        } else if (endurance.getMaxEndurance() < maxEndurance) {
            endurance.setMaxEndurance(maxEndurance);
        }
        if (player.isHandActive()) {
            Item item = player.getHeldItem(player.getActiveHand()).getItem();
            if (item instanceof ItemShield || item instanceof ItemBow) {
                if (endurance.getEndurance() <= 0) {
                    player.attackEntityFrom(EXHAUSTED, 1f);
                } else {
                    endurance.addExhaustion(0.1f);
                }
            }
            endurance.addCoolDown(40);
        }
        if (player.isSprinting()) {
            endurance.addCoolDown(100);
            endurance.addExhaustion(0.2f);
        } else if (player.isRiding()) {
            endurance.removeCoolDown(10);
            endurance.addSaturation(0.3f);
        } else {
            endurance.removeCoolDown(10);
            endurance.addSaturation(0.2f);
        }
    }

    public static String getOreDictName(String type, String origin) {
        String name = origin.replaceAll("_", "").replace(type, "");
        return type + name.substring(12, 13).toUpperCase() + name.substring(13);
    }

    public static int stackMatched(ItemStack item, String[] list) {
        for (int i = 0; i < list.length; i++) {
            String[] parts = list[i].split(";");
            String[] name = parts[0].split(":");
            if (name.length == 2) {
                if (item.getItem().equals(Item.REGISTRY.getObject(new ResourceLocation(parts[0])))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static IBlockState getBlock(String id) {
        IBlockState block = Blocks.AIR.getDefaultState();
        String[] name = id.split(":");
        if (name.length == 2) {
            for (IBlockState state : Block.REGISTRY.getObject(new ResourceLocation(id)).getBlockState().getValidStates()) {
                if (state.getBlock().getMetaFromState(state) == 0) {
                    block = state;
                    break;
                }
            }
        } else if (name.length == 3) {
            for (IBlockState state : Block.REGISTRY.getObject(new ResourceLocation(name[0], name[1])).getBlockState().getValidStates()) {
                if (state.getBlock().getMetaFromState(state) == Integer.parseInt(name[2])) {
                    block = state;
                    break;
                }
            }
        }
        return block;
    }

    public static void playSound(EntityPlayerMP player, SoundEvent sound) {
        if (player != null && !player.hasDisconnected()) {
            SPacketSoundEffect packet = new SPacketSoundEffect(sound, SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, 1.0F, 1.0F);
            player.connection.sendPacket(packet);
        }
    }

    public static void checkTimeout(NBTTagCompound nbtTagCompound, EntityPlayerMP player) {
        for (String name : nbtTagCompound.getKeySet()) {
            if (MinecraftServer.getCurrentTimeMillis() - nbtTagCompound.getLong(name) > 30000) {
                nbtTagCompound.removeTag(name);
                TextComponentTranslation denied = new TextComponentTranslation("commands.tpa.timeout", name);
                player.sendMessage(denied.setStyle(denied.getStyle().setColor(TextFormatting.RED)));
                playSound(player, SoundEvents.ENTITY_ITEM_BREAK);
            }
        }
    }

    public static String[] getFoodValues(ItemStack stack, EntityPlayer player) {
        ItemFood food = (ItemFood) stack.getItem();
        int hunger = food.getHealAmount(stack);
        float saturation = food.getSaturationModifier(stack);
        if (player != null) {
            if (Loader.isModLoaded("applecore")) {
                FoodValues foodValues = AppleCoreAPI.accessor.getFoodValuesForPlayer(stack, player);
                hunger = foodValues.hunger;
                saturation = foodValues.saturationModifier;
            }
            if (Loader.isModLoaded("pyrotech")) {
                Potion comfort = Potion.REGISTRY.getObject(new ResourceLocation("pyrotech", "comfort"));
                if (player.isPotionActive(comfort)) {
                    hunger += (int) (food.getHealAmount(stack) * ModuleTechBasicConfig.CAMPFIRE_EFFECTS.COMFORT_HUNGER_MODIFIER);
                    saturation += (float) (food.getSaturationModifier(stack) * ModuleTechBasicConfig.CAMPFIRE_EFFECTS.COMFORT_SATURATION_MODIFIER);
                }
            }
        }
        return new String[]{String.format("%d", hunger), NumberFormat.getPercentInstance().format(saturation)};
    }
}