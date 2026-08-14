package com.origins_eternity.ercore.compat.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class TileCampfireRock extends TileCampfire {
    public TileCampfireRock() {
        super();
    }

    @Override
    protected int combustionGetRainDeactivateTime() {
        return -1;
    }

    @Override
    public void workerSetActive(boolean active) {
        if (!this.isDead()) {
            if (this.workerIsActive() && !active) {
                IBlockState state = this.world.getBlockState(this.pos);
                if (state.getBlock() instanceof BlockCampfireRock) {
                    this.world.setBlockState(this.pos, state.withProperty(BlockCampfireRock.VARIANT, BlockCampfireRock.EnumType.NORMAL));
                }
            } else if (!this.workerIsActive() && active) {
                IBlockState state = this.world.getBlockState(this.pos);
                if (state.getBlock() instanceof BlockCampfireRock) {
                    this.world.setBlockState(this.pos, state.withProperty(BlockCampfireRock.VARIANT, BlockCampfireRock.EnumType.LIT));
                }
            }
        }
        super.workerSetActive(active);
    }

    @Override
    public boolean workerDoWork() {
        if (!super.workerDoWork()) {
            return this.getAshLevel() >= 8;
        }
        return true;
    }

    @Override
    protected int combustionGetBurnTimeForFuel(ItemStack fuel) {
        int original = super.combustionGetBurnTimeForFuel(fuel);
        return (int) (original * 2 * getAshFactor());
    }

    @Override
    public int getEffectRadius() {
        int bonus = Math.round(5 * getAshFactor());
        return Math.min(15, super.getEffectRadius() + bonus);
    }

    private float getAshFactor() {
        int ash = this.getAshLevel();
        return 1.0f - (ash / 8.0f * 0.5f);
    }
}