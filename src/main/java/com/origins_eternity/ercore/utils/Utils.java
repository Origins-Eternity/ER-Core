package com.origins_eternity.ercore.utils;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.message.SyncEndurance;
import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.common.damagesystem.PlayerDamageModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.origins_eternity.ercore.ERCore.packetHandler;
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
            for (String name : Configuration.resourcepacks) {
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
        endurance.setMaxEndurance((int) (2 * player.getHealth()));
        if (endurance.getEndurance() > endurance.getMaxEndurance()) {
            endurance.setEndurance(endurance.getMaxEndurance());
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
            if (endurance.getEndurance() <= 0) {
                player.attackEntityFrom(EXHAUSTED, 1f);
            } else {
                endurance.addExhaustion(0.2f);
            }
        } else if (player.isRiding()) {
            endurance.removeCoolDown(10);
            endurance.addSaturation(0.3f);
        } else {
            endurance.removeCoolDown(10);
            endurance.addSaturation(0.2f);
        }
    }

    public static void checkStatus(EntityPlayer player) {
        IEndurance endurance = player.getCapability(ENDURANCE, null);
        if (endurance.getEndurance() <= 6f) {
            if (player.world.isRemote) {
                player.motionX *= 0.5;
                player.motionZ *= 0.5;
            } else {
                player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 312, 1, false, false));
            }
        }
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