package com.origins_eternity.ercore.content.tab;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.block.Blocks;
import com.origins_eternity.ercore.content.block.Ores;
import com.origins_eternity.ercore.content.fluid.Fluids;
import com.origins_eternity.ercore.content.item.Blueprints;
import com.origins_eternity.ercore.content.item.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import static com.origins_eternity.ercore.ERCore.MOD_ID;

public class CreativeTab {
    public static final CreativeTabs ERCORE = new CreativeTabs(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            ItemStack icon = new ItemStack(net.minecraft.init.Items.RABBIT_FOOT, 1);
            if (Configuration.enableItems) {
                icon = new ItemStack(Items.Tungsten_Ingot, 1);
            } else if (Configuration.enableOres) {
                icon = new ItemStack(Ores.Copper_Ore, 1);
            } else if (Configuration.enableBlocks) {
                icon = new ItemStack(Blocks.Basalt, 1);
            } else if (Configuration.enableBlueprints) {
                icon = new ItemStack(Blueprints.Anther_Table, 1);
            } else if (Configuration.enableFluids) {
                icon = new ItemStack(Fluids.Tungsten.getBlock());
            }
            return icon;
        }
    };
}
