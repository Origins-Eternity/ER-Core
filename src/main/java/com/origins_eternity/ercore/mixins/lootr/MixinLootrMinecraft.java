package com.origins_eternity.ercore.mixins.lootr;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LootrChestMinecartEntity.class, remap = false)
public abstract class MixinLootrMinecraft {
    @Shadow
    public abstract void addLoot(EntityPlayer player, IInventory inventory, ResourceLocation overrideTable, long seed);

    @Unique
    private boolean fillingLoot = false;

    @Inject(method = "func_184288_f", at = @At("HEAD"), cancellable = true)
    private void addLoot(EntityPlayer player, CallbackInfo ci) {
        if (this.fillingLoot) {
            ci.cancel();
            return;
        }
        this.fillingLoot = true;
        try {
            this.addLoot(player, (IInventory) this, null, player.world.rand.nextLong());
        } finally {
            this.fillingLoot = false;
        }

    }
}