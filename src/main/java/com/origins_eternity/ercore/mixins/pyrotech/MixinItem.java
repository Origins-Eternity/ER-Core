package com.origins_eternity.ercore.mixins.pyrotech;

import com.codetaylor.mc.pyrotech.modules.storage.block.item.ItemBlockTank;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.origins_eternity.ercore.config.Configuration.Compat;

@Mixin(value = Item.class, remap = false)
public class MixinItem {
    @Inject(method = "getItemStackLimit(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void getStackLimit(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() instanceof ItemBlockTank) {
            NBTTagCompound nbt = ItemBlockTank.getTankTag(stack);
            if (nbt != null && !nbt.hasKey("Empty") && Compat.tank > 0) {
                cir.setReturnValue(Compat.tank);
            }
        }
    }
}