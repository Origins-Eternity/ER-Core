package com.origins_eternity.ercore.content.tab;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.block.Blocks;
import com.origins_eternity.ercore.content.block.Ores;
import com.origins_eternity.ercore.content.fluid.Fluids;
import com.origins_eternity.ercore.content.item.Blueprints;
import com.origins_eternity.ercore.content.item.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
    public CreativeTab() {
        super("ercore");
    }

    @Override
    public ItemStack createIcon() {
        ItemStack icon = new ItemStack(net.minecraft.init.Items.RABBIT_FOOT, 1);
        if (Configuration.enableItems) {
            icon = new ItemStack(Items.Sulphur, 1);
        } else if (Configuration.enableOres) {
            icon = new ItemStack(Ores.Copper_Ore, 1);
        } else if (Configuration.enableBlocks) {
            icon = new ItemStack(Blocks.Obsidian_Magic, 1);
        } else if (Configuration.enableBlueprints) {
            icon = new ItemStack(Blueprints.Anther_Table, 1);
        } else if (Configuration.enableFluids) {
            icon = new ItemStack(Fluids.Obsidian_Magic.getBlock());
        }
        return icon;
    }
}