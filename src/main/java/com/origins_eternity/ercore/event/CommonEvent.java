package com.origins_eternity.ercore.event;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.Endurance;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.message.CheckMove;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.ERCore.packetHandler;
import static com.origins_eternity.ercore.utils.Utils.*;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommonEvent {
    @SubscribeEvent
    public static void onCreateFluidSource(BlockEvent.CreateFluidSourceEvent event) {
        event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public static void onCreateSpawnPosition(WorldEvent.CreateSpawnPosition event) {
        World world = event.getWorld();
        int sea = world.getSeaLevel();
        BlockPos blockPos = new BlockPos(0, sea, 0);
        event.setCanceled(true);
        world.setSpawnPoint(blockPos);
    }

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(MOD_ID, "endurance"), new Endurance.EnduranceProvider(Capabilities.ENDURANCE));
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (!player.isCreative()) {
            World world = event.getWorld();
            float hardness = event.getState().getBlockHardness(world, event.getPos());
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            if (!world.isRemote) {
                endurance.addCoolDown(20);
                endurance.addExhaustion(hardness / 10);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.isCreative()) {
                IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                if (endurance.isTired()) {
                    event.getEntityLiving().motionY -= 1F;
                } else if (!player.world.isRemote) {
                    endurance.addCoolDown(60);
                    endurance.addExhaustion(0.2f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isCreative()) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            if (endurance.isExhausted()) {
                 event.setCanceled(true);
            }
            if (!player.world.isRemote) {
                endurance.addCoolDown(10);
                endurance.addExhaustion(0.005f);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent event) {
        String toolClass = event.getItemStack().getTranslationKey();
        for (String tool : Configuration.tools) {
            if (toolClass.contains(tool)) {
                EntityPlayer player = event.getEntityPlayer();
                if (!player.isCreative()) {
                    IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                    if (!player.world.isRemote) {
                        endurance.addCoolDown(20);
                        endurance.addExhaustion(0.1f);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isCreative()) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            if (endurance.isExhausted()) {
                event.setCanceled(true);
            }
            if (!player.world.isRemote) {
                endurance.addCoolDown(40);
                endurance.addExhaustion(0.2f);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            if (endurance.isSprite()) {
                if (player.isPlayerSleeping()) {
                    player.wakeUpPlayer(false, false, false);
                }
            } else if (endurance.isTired()) {
                player.setSprinting(false);
                addDebuff(player);
                if (endurance.isExhausted()) {
                    if (!player.isPlayerSleeping()) {
                        player.trySleep(new BlockPos(player));
                    }
                }
            }
            World world = player.world;
            if (world.isRemote) {
                boolean move = (player.moveForward != 0) || (player.moveStrafing != 0);
                packetHandler.sendToServer(new CheckMove(move));
            } else {
                syncEndurance(player);
                tickUpdate(player);
            }
        }
    }
}