package com.origins_eternity.ercore.mixins.lootr;

import com.origins_eternity.ercore.compat.lootr.ChestReplacer;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemBlock.class, remap = false)
public abstract class MixinItemBlock {
    @Inject(method = "placeBlockAt", at = @At("HEAD"))
    public void beforePlace(net.minecraft.item.ItemStack stack, EntityPlayer player, World worldIn,
                                BlockPos pos, net.minecraft.util.EnumFacing side,
                                float hitX, float hitY, float hitZ,
                                net.minecraft.block.state.IBlockState newState,
                                CallbackInfoReturnable<Boolean> cir) {
        if (newState.getBlock() instanceof BlockChest) {
            ChestReplacer.track(pos);
        }
    }

    @Inject(method = "placeBlockAt", at = @At("RETURN"))
    public void afterPlace(net.minecraft.item.ItemStack stack, EntityPlayer player, World worldIn,
                               BlockPos pos, net.minecraft.util.EnumFacing side,
                               float hitX, float hitY, float hitZ,
                               net.minecraft.block.state.IBlockState newState,
                               CallbackInfoReturnable<Boolean> cir) {
        if (newState.getBlock() instanceof BlockChest) {
            ChestReplacer.untrack();
        }
    }
}