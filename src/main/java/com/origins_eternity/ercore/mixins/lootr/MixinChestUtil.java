package com.origins_eternity.ercore.mixins.lootr;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.IInteractionObject;
import noobanidus.mods.lootr.util.ChestUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChestUtil.class)
public class MixinChestUtil {
    @Redirect(method = "handleLootCart", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;displayGui(Lnet/minecraft/world/IInteractionObject;)V"))
    private static void onHandleLootCart(EntityPlayer player, IInteractionObject guiOwner) {
        if (guiOwner instanceof IInventory) {
            player.displayGUIChest((IInventory) guiOwner);
        } else if (guiOwner != null) {
            player.displayGui(guiOwner);
        }
    }
}
