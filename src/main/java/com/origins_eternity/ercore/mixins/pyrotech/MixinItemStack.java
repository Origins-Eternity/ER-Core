package com.origins_eternity.ercore.mixins.pyrotech;

import com.codetaylor.mc.pyrotech.modules.storage.block.item.ItemBlockTank;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.origins_eternity.ercore.config.Configuration.Compat;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Inject(method = "getMaxStackSize()I", at = @At("HEAD"), cancellable = true)
    private void onGetMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof ItemBlockTank) {
            NBTTagCompound nbt = ItemBlockTank.getTankTag(stack);
            if (nbt != null && !nbt.hasKey("Empty") && Compat.tank > 0) {
               cir.setReturnValue(Compat.tank);
            }
        }
    }
}