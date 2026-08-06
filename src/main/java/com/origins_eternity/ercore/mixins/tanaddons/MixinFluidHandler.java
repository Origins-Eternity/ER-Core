package com.origins_eternity.ercore.mixins.tanaddons;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import p455w0rd.tanaddons.items.ItemThirstQuencher;

@Mixin(value = FluidHandlerItemStack.class, remap = false)
public abstract class MixinFluidHandler {
    @Accessor("container")
    public abstract ItemStack getContainerStack();

    @Inject(method = "drain(Lnet/minecraftforge/fluids/FluidStack;Z)Lnet/minecraftforge/fluids/FluidStack;", at = @At("RETURN"))
    public void syncOnDrainStack(FluidStack resource, boolean doDrain, CallbackInfoReturnable<FluidStack> cir) {
        syncFluidStored(cir.getReturnValue(), getContainerStack(), doDrain);
    }

    @Inject(method = "drain(IZ)Lnet/minecraftforge/fluids/FluidStack;", at = @At("RETURN"))
    public void syncOnDrainAmount(int maxDrain, boolean doDrain, CallbackInfoReturnable<FluidStack> cir) {
        syncFluidStored(cir.getReturnValue(), getContainerStack(), doDrain);
    }

    private static void syncFluidStored(FluidStack drained, ItemStack container, boolean doDrain) {
        if (!doDrain || drained == null || drained.amount <= 0) return;

        if (container.getItem() instanceof ItemThirstQuencher) {
            NBTTagCompound tag = container.getTagCompound();
            int stored = tag != null ? tag.getInteger("FluidStored") : 0;
            if (stored <= 0) return;

            int newStored = Math.max(0, stored - drained.amount);
            container.getTagCompound().setInteger("FluidStored", newStored);
        }
    }
}