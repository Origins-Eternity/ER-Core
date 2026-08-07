package com.origins_eternity.ercore.mixins.lootr;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;
import noobanidus.mods.lootr.util.ChestUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LootrChestMinecartEntity.class, remap = false)
public abstract class MixinLootrMinecart {
    @Inject(method = "processInitialInteract", at = @At("HEAD"), cancellable = true)
    private void onInitialInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        LootrChestMinecartEntity cart = (LootrChestMinecartEntity) (Object) this;

        if (player.isSneaking()) {
            ChestUtil.handleLootCartSneak(player.world, cart, player);
        } else {
            ChestUtil.handleLootCart(player.world, cart, player);
        }
        
        cir.setReturnValue(true);
    }
}