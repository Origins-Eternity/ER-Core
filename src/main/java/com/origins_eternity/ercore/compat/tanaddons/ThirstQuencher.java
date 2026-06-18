package com.origins_eternity.ercore.compat.tanaddons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static p455w0rd.tanaddons.init.ModConfig.Options.THIRST_QUENCHER_WATER_CAPACITY;
import static p455w0rd.tanaddons.init.ModItems.THIRST_QUENCHER;

public class ThirstQuencher {
    private static final String TAG_FLUID_STORED = "FluidStored";

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem().equals(THIRST_QUENCHER)) {
            TileEntity te = event.getWorld().getTileEntity(event.getPos());
            if (te != null) {
                IFluidHandler tank = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, event.getFace());
                if (tank != null) {
                    event.setCanceled(true);
                    event.setUseItem(Event.Result.DENY);
                    event.setUseBlock(Event.Result.DENY);
                    EntityPlayer player = event.getEntityPlayer();
                    if (!event.getWorld().isRemote) {
                        int stored = getFluidStored(stack);
                        int accept = THIRST_QUENCHER_WATER_CAPACITY * 1000 - stored;
                        FluidStack drained = tank.drain(new FluidStack(FluidRegistry.WATER, accept), false);
                        if (drained != null && drained.amount > 0) {
                            tank.drain(drained, true);
                            setFluidStored(stack, stored + drained.amount);
                            player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    private static int getFluidStored(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getInteger(TAG_FLUID_STORED) : 0;
    }

    private static void setFluidStored(ItemStack stack, int amount) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger(TAG_FLUID_STORED, Math.max(0, amount));
    }
}