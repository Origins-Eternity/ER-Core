package com.origins_eternity.ercore.event;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.Endurance;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.content.damage.Damages.EXHAUSTED;
import static com.origins_eternity.ercore.utils.Utils.*;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommonEvent {
    @SubscribeEvent
    public static void onCreateFluidSource(BlockEvent.CreateFluidSourceEvent event) {
        event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public static void onFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
        if (!event.getWorld().isRemote) {
            Block block = event.getState().getBlock();
            if (block.equals(Blocks.COBBLESTONE)) {
                if (getBlock(Configuration.product).isFullBlock()) {
                    event.setNewState(getBlock(Configuration.product));
                }
            }
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
                if (endurance.getEndurance() <= 0) {
                    player.attackEntityFrom(EXHAUSTED, 1f);
                } else {
                    endurance.addExhaustion(hardness / 10);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemCraftedEvent(ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        if (!player.isCreative()) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            if (!player.world.isRemote) {
                endurance.addCoolDown(50);
                if (endurance.getEndurance() <= 0) {
                    player.attackEntityFrom(EXHAUSTED, 1f);
                } else {
                    endurance.addExhaustion(0.5f);
                }
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
                if (!player.world.isRemote) {
                    endurance.addCoolDown(60);
                    if (endurance.getEndurance() <= 0) {
                        player.attackEntityFrom(EXHAUSTED, 1f);
                    } else {
                        endurance.addExhaustion(0.2f);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isCreative()) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            endurance.addCoolDown(30);
            endurance.addExhaustion(0.005f);
            if (endurance.getEndurance() <= 6f) {
                float origin = event.getOriginalSpeed();
                event.setNewSpeed((float) (origin * 0.5));
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isCreative()) {
            if (!player.world.isRemote) {
                IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                if (Loader.isModLoaded("firstaid")) {
                    double maxHealth = player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").getAttributeValue();
                    endurance.setMaxHealth(maxHealth);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingEntityUseItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (!player.isCreative()) {
                if (Loader.isModLoaded("firstaid")) {
                    if (event.getItem().getItem() instanceof ItemFood) {
                        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                        double maxHealth = player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").getAttributeValue();
                        if (maxHealth > endurance.getMaxHealth()) {
                            if (Configuration.enableRegeneration) {
                                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 2));
                            }
                            endurance.setMaxHealth(maxHealth);
                        }
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
            if (!player.world.isRemote) {
                endurance.addCoolDown(50);
                if (endurance.getEndurance() <= 0) {
                    player.attackEntityFrom(EXHAUSTED, 1f);
                } else {
                    endurance.addExhaustion(0.2f);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onClone(PlayerEvent.Clone event) {
        EntityPlayer old = event.getOriginal();
        EntityPlayer clone = event.getEntityPlayer();
        if (!clone.world.isRemote) {
            Capability<IEndurance> capability = Capabilities.ENDURANCE;
            IEndurance origin = old.getCapability(capability, null);
            IEndurance present = clone.getCapability(capability, null);
            if (!event.isWasDeath()) {
                capability.getStorage().readNBT(capability, present, null, capability.getStorage().writeNBT(capability, origin, null));
            } else if (Loader.isModLoaded("firstaid")) {
                present.setMaxHealth(origin.getMaxHealth());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        EntityPlayer player = event.player;
        if (!event.player.world.isRemote) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            endurance.setEndurance(player.getMaxHealth());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            checkStatus(player);
            if (player.ticksExisted % 10 == 0) {
                if (!player.world.isRemote) {
                    tickUpdate(player);
                    syncEndurance(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        if ((!player.isCreative()) && (!player.world.isRemote)) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            Set<String> tools = event.getItemStack().getItem().getToolClasses(event.getItemStack());
            for (String tool : Configuration.tools) {
                if (tools.contains(tool)) {
                    endurance.addCoolDown(60);
                    if (endurance.getEndurance() <= 0) {
                        player.attackEntityFrom(EXHAUSTED, 1f);
                    } else {
                        endurance.addExhaustion(0.2f);
                    }
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onUseHoe(UseHoeEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if ((!player.isCreative()) && (!player.world.isRemote)) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            endurance.addCoolDown(60);
            if (endurance.getEndurance() <= 0) {
                player.attackEntityFrom(EXHAUSTED, 1f);
            } else {
                endurance.addExhaustion(0.2f);
            }
        }
    }

    @SubscribeEvent
    public static void onPotionApplicable(PotionEvent.PotionApplicableEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (player.isPotionActive(event.getPotionEffect().getPotion())) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if ((!player.world.isRemote) && (Configuration.enableHardcore)) {
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
                if (endurance.getEndurance() <= 6) {
                    event.setAmount((float) (event.getAmount() * 0.5));
                }
            }
        }
    }
}