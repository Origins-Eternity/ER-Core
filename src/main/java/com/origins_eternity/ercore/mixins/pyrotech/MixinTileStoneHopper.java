package com.origins_eternity.ercore.mixins.pyrotech;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockMechanicalHopper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileStoneHopper.class, remap = false)
public abstract class MixinTileStoneHopper {
    @Shadow
    protected abstract int getTransferAmount(ItemStack cog);

    @Inject(method = "doWork", at = @At("HEAD"), cancellable = true)
    private void doWork(ItemStack cog, CallbackInfoReturnable<Integer> cir) {
        TileEntity tile = (TileEntity) (Object) this;
        World world = tile.getWorld();
        BlockPos pos = tile.getPos();

        TileEntity tileSource = world.getTileEntity(pos.up());
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);

        if (state.getValue(BlockMechanicalHopper.TYPE) == BlockMechanicalHopper.EnumType.Down) {
            facing = EnumFacing.UP;
        }

        TileEntity tileTarget = world.getTileEntity(pos.offset(facing.getOpposite()));

        if (tileSource == null || tileTarget == null) {
            cir.setReturnValue(-1);
            return;
        }

        IItemHandler source = tileSource.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
        IItemHandler target = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

        if (source == null || target == null) {
            cir.setReturnValue(-1);
            return;
        }

        int amount = Math.min(cog.getMaxDamage() - cog.getItemDamage(), this.getTransferAmount(cog));

        for (int i = 0; i < source.getSlots(); i++) {
            ItemStack simulate = target.extractItem(i, amount, true);
            if (simulate.isEmpty()) {
                continue;
            }
            
            ItemStack rest = simulate.copy();
            for (int t = 0; t < target.getSlots() && !rest.isEmpty(); t++) {
                rest = target.insertItem(t, rest, true);
            }

            int canTransfer = simulate.getCount() - rest.getCount();
            if (canTransfer <= 0) {
                continue;
            }

            ItemStack extract = target.extractItem(i, canTransfer, false);
            if (extract.isEmpty()) {
                continue;
            }

            int count = extract.getCount();
            
            ItemStack remain = extract;
            for (int t = 0; t < target.getSlots() && !remain.isEmpty(); t++) {
                remain = target.insertItem(t, remain, false);
            }

            int transfer = count - remain.getCount();
            if (!remain.isEmpty()) {
                remain = source.insertItem(i, remain, false);

                if (!remain.isEmpty()) {
                    InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, remain);
                }
            }
            if (transfer <= 0) {
                continue;
            }

            SoundHelper.playSoundServer(world, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5f);

            int cogDamage = 1;
            if (ModuleTechMachineConfig.STONE_HOPPER.COG_DAMAGE_TYPE
                    == ModuleTechMachineConfig.StoneHopper.EnumCogDamageType.PerItem) {
                cogDamage = transfer;
            }

            cir.setReturnValue(cogDamage);
            return;
        }

        cir.setReturnValue(-1);
    }
}