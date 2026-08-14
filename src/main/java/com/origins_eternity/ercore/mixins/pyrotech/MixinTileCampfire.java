package com.origins_eternity.ercore.mixins.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import com.origins_eternity.ercore.compat.pyrotech.TileCampfireRock;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileCampfire.class, remap = false)
public abstract class MixinTileCampfire {
    @Shadow
    public abstract int getAshLevel();

    @Shadow
    protected abstract void setDead();

    @Redirect(method = "workerConsumeFuel", at = @At(value = "INVOKE", target = "Lcom/codetaylor/mc/pyrotech/modules/tech/basic/tile/TileCampfire;setDead()V"))
    private void onWorkerConsumeFuel(TileCampfire tile) {
        if (tile instanceof TileCampfireRock) {
            return;
        }
        this.setDead();
    }

    @Inject(method = "getCookTime", at = @At("RETURN"), cancellable = true)
    private void getCookTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if ((Object) this instanceof TileCampfireRock) {
            int original = cir.getReturnValue();
            if (original <= 0) return;

            int ash = this.getAshLevel();
            float factor = 0.1f + (ash / 8.0f * 0.5f);

            cir.setReturnValue((int) Math.max(1, original * factor));
        }
    }
}