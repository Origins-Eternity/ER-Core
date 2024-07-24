package com.origins_eternity.ercore.event;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.Endurance;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.message.CheckMove;
import com.origins_eternity.ercore.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import squeek.applecore.api.food.FoodEvent;

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
    public static void onFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
        Block block = event.getState().getBlock();
        if (block.equals(Blocks.COBBLESTONE)) {
            event.setNewState(Utils.getBlockstate("chisel:basalt", Blocks.COBBLESTONE));
        }
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
                endurance.addCoolDown(30);
                endurance.addExhaustion(hardness / 10);
            }
        }
    }

    @SubscribeEvent
    public static void onItemCraftedEvent(ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        if (!player.isCreative()) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            if (!player.world.isRemote) {
                endurance.addCoolDown(30);
                endurance.addExhaustion(0.5f);
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
                if (endurance.isExhausted()) {
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
            if (endurance.isTired()) {
                float origin = event.getOriginalSpeed();
                event.setNewSpeed((float) (origin * 0.32));
                if (endurance.isExhausted()) {
                    event.setCanceled(true);
                }
            }
            if (!player.world.isRemote) {
                endurance.addCoolDown(20);
                endurance.addExhaustion(0.005f);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Item item = event.getItemStack().getItem();
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isCreative()) {
            if ((item.equals(Items.BOW)) || (item.equals(Items.SHIELD))) {
                IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                if (endurance.isExhausted()) {
                    event.setCanceled(true);
                }
                if (!player.world.isRemote) {
                    if (!event.isCanceled()) {
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
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            EntityPlayer player = event.getOriginal();
            EntityPlayer clone = event.getEntityPlayer();
            Capability<IEndurance> capability = Capabilities.ENDURANCE;
            IEndurance origin = player.getCapability(capability, null);
            IEndurance present = clone.getCapability(capability, null);
            capability.getStorage().readNBT(capability, present, null, capability.getStorage().writeNBT(capability, origin, null));
        }
    }

    static int counter;

    static float maxHealth;

    @Optional.Method(modid = "solcarrot")
    @SubscribeEvent
    public static void onFoodEaten(FoodEvent.FoodEaten event) {
        maxHealth = event.player.getMaxHealth();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            counter++;
            if (counter > 10) {
                World world = player.world;
                checkStatus(player);
                if (world.isRemote) {
                    boolean move = (player.motionX != 0) || (player.motionY != 0);
                    packetHandler.sendToServer(new CheckMove(move, player.getCachedUniqueIdString()));
                } else {
                    tickUpdate(player);
                    syncEndurance(player);
                }
                counter = 0;
            }
            if (player.getMaxHealth() > maxHealth) {
                float extra = player.getMaxHealth() - maxHealth;
                maxHealth = player.getMaxHealth();
                player.setHealth(player.getHealth() + extra);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isCreative()) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            if (endurance.isExhausted()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (!player.world.isRemote) {
                if (event.getSource() == DamageSource.DROWN) {
                    event.setAmount(player.getHealth());
                } else if (event.getSource() == DamageSource.LAVA) {
                    event.setAmount(player.getHealth());
                } else if (event.getSource() == DamageSource.LIGHTNING_BOLT) {
                    event.setAmount(player.getHealth());
                }
            }
        } else if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (!player.isCreative()) {
                IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                if (endurance.isTired()) {
                    event.setAmount((float) (event.getAmount() * 0.32));
                }
            }
        }
    }
}