package com.origins_eternal.ercore.event;

import com.origins_eternal.ercore.config.Config;
import com.origins_eternal.ercore.message.TiredMessage;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static com.origins_eternal.ercore.ERCore.MOD_ID;
import static com.origins_eternal.ercore.ERCore.packetHandler;
import static com.origins_eternal.ercore.event.ClientEvent.EnduranceData;
import static com.origins_eternal.ercore.utils.Utils.*;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommonEvent {

    @SubscribeEvent
    public static void onCreateFluidSource(BlockEvent.CreateFluidSourceEvent event) {
        event.setResult(Event.Result.DENY);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
        Block block = event.getState().getBlock();
        if (block.equals(Blocks.STONE)) {
            event.setNewState(getBlockstate("taiga:basalt_block", Blocks.STONE));
        } else if (block.equals(Blocks.COBBLESTONE)) {
            event.setNewState(getBlockstate("chisel:basalt", Blocks.COBBLESTONE));
        } else if (block.equals(Blocks.OBSIDIAN)) {
            event.setNewState(getBlockstate("advancedrocketry:basalt", Blocks.OBSIDIAN));
        }
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
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (Config.enableEndurance) {
            World world = event.getWorld();
            if (!world.isRemote) {
                float hardness = event.getState().getBlockHardness(world, event.getPos());
                if (hardness > 0.5) {
                    EntityPlayer player = event.getPlayer();
                    Set<String> tags = player.getTags();
                    if (tags.contains("float")) {
                        addStringTags(player, "break", 1);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (Config.enableEndurance) {
            EntityPlayer player = event.getEntityPlayer();
            if ((!player.isCreative()) && (!player.isSpectator())) {
                if (player.world.isRemote) {
                    setFloatTags(player, 0f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (Config.enableEndurance) {
            Entity entity = event.getEntity();
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if ((!player.isCreative()) && (!player.isSpectator())) {
                    Set<String> tags = player.getTags();
                    if (tags.contains("float")) {
                        addStringTags(player, "jump", 3);
                        if (checkStatus(player).equals("tired")) {
                            event.getEntityLiving().motionY -= 1F;
                        } else {
                            setFloatTags(player, -0.2f);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (Config.enableEndurance) {
            EntityPlayer player = event.player;
            World world = player.world;
            if (world.isRemote) {
                if ((!player.isCreative()) && (!player.isSpectator())) {
                    Set<String> tags = player.getTags();
                    if (tags.contains("float")) {
                        EntityDataManager dataManager = player.getDataManager();
                        float max = 20;
                        float maxHealth = player.getMaxHealth();
                        if (maxHealth >= 20) {
                            max = maxHealth;
                        }
                        float value = dataManager.get(EnduranceData);
                        if ((checkStatus(player).equals("spirit")) && (player.isPlayerSleeping())) {
                            player.wakeUpPlayer(false, false, false);
                            player.removeTag("rest");
                        } else if ((checkStatus(player).equals("tired")) && (!player.isPlayerSleeping())) {
                            player.setSprinting(false);
                            if ((!player.isPotionActive(MobEffects.MINING_FATIGUE)) || (!player.isPotionActive(MobEffects.WEAKNESS)) || (!player.isPotionActive(MobEffects.SLOWNESS))) {
                                packetHandler.sendToServer(new TiredMessage());
                            }
                        } else if (checkStatus(player).equals("exhausted")) {
                            if (!tags.contains("rest")) {
                                player.addTag("rest");
                                BlockPos blockPos = new BlockPos(player);
                                player.trySleep(blockPos);
                            }
                        }
                        if (player.isOnLadder()) {
                            if (player.motionY > 0) {
                                setFloatTags(player, -0.005f);
                            }
                        }
                        if ((player.moveForward != 0) || (player.moveStrafing != 0)) {
                            if (player.isRiding()) {
                                if (player.isOverWater()) {
                                    setFloatTags(player, -0.005f);
                                }
                            } else {
                                if (player.isSprinting()) {
                                    addStringTags(player, "sprint", 7);
                                    setFloatTags(player, -0.03f);
                                } else if (player.isInWater()) {
                                    addStringTags(player, "swim", 5);
                                    setFloatTags(player, -0.01f);
                                } else if (checkStatus(player).equals("tired")) {
                                    setFloatTags(player, -0.02f);
                                } else if ((player.getAir() == 300) && (!tags.contains("jump")) && (!tags.contains("break")) && (!tags.contains("swim")) && (!tags.contains("sprint"))) {
                                    setFloatTags(player, 0.01f);
                                }
                            }
                        } else if ((value != max) && (!tags.contains("jump")) && (!tags.contains("break")) && (!tags.contains("swim")) && (!tags.contains("sprint"))) {
                            if (player.onGround) {
                                setFloatTags(player, 0.03f);
                            } else if (player.isPlayerSleeping()) {
                                setFloatTags(player, 0.01f);
                            } else if (player.isInWater()) {
                                setFloatTags(player, 0.02f);
                            } else if (player.isRiding()) {
                                setFloatTags(player, 0.05f);
                            }
                        }
                    } else {
                        setFloatTags(player, 0f);
                    }
                }
            }
        }
    }
}