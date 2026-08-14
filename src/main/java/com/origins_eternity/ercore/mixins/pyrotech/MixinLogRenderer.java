package com.origins_eternity.ercore.mixins.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.client.render.CampfireInteractionLogRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import com.origins_eternity.ercore.compat.pyrotech.TileCampfireRock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CampfireInteractionLogRenderer.class, remap = false)
public abstract class MixinLogRenderer {
    @Unique
    private static boolean isStoneCampfire = false;

    @Inject(method = "renderSolidPass(Lcom/codetaylor/mc/pyrotech/modules/tech/basic/tile/TileCampfire$InteractionLog;Lnet/minecraft/world/World;Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;F)V", at = @At("HEAD"))
    private void onRenderSolidPass(TileCampfire.InteractionLog interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks, CallbackInfo ci) {
        TileEntity tile = world.getTileEntity(pos);
        isStoneCampfire = tile instanceof TileCampfireRock;
    }

    @Inject(method = "renderAdditivePass(Lcom/codetaylor/mc/pyrotech/modules/tech/basic/tile/TileCampfire$InteractionLog;Lnet/minecraft/world/World;Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/item/ItemStack;F)Z", at = @At("HEAD"))
    private void onRenderAdditivePass(TileCampfire.InteractionLog interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks, CallbackInfoReturnable<Boolean> cir) {
        TileEntity tile = world.getTileEntity(hitPos);
        isStoneCampfire = tile instanceof TileCampfireRock;
    }

    @Inject(method = "renderLogPre", at = @At("HEAD"), cancellable = true)
    private void onRenderLogPre(int i, CallbackInfo ci) {
        if (isStoneCampfire) {
            GlStateManager.pushMatrix();
            if (i < 4) {
                GlStateManager.translate(0.5, 0.25, 0.5);
                GlStateManager.rotate(90 * (i % 4), 0, 1, 0);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0.25 + 0.125, 0, 0);
                GlStateManager.rotate(45, 0, 0, 1);
            } else {
                GlStateManager.translate(0.5, 0.20, 0.5);
                GlStateManager.rotate(90 * (i % 4) + 45, 0, 1, 0);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0.25 + 0.125 + 0.0625, 0, 0);
                GlStateManager.rotate(90 - 22.5f, 0, 0, 1);
            }
            GlStateManager.scale(4.0 / 16.0, 8.0 / 16.0, 4.0 / 16.0);
            ci.cancel();
        }
    }
}