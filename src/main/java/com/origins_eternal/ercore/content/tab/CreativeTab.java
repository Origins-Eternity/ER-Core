package com.origins_eternal.ercore.content.tab;

import com.origins_eternal.ercore.config.Config;
import com.origins_eternal.ercore.content.block.Blocks;
import com.origins_eternal.ercore.content.block.Ores;
import com.origins_eternal.ercore.content.fluid.Fluids;
import com.origins_eternal.ercore.content.item.Blueprints;
import com.origins_eternal.ercore.content.item.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
    public CreativeTab() {
        super("ercore");
    }

    @Override
    public ItemStack createIcon() {
        ItemStack icon = new ItemStack(Fluids.Tungsten_Steel.getBlock(), 1);
        if (Config.items) {
            icon = new ItemStack(Items.Sulphur, 1);
        } else if (Config.ores) {
            icon = new ItemStack(Ores.Copper_Ore, 1);
        } else if (Config.blocks) {
            icon = new ItemStack(Blocks.Obsidian_Magic, 1);
        } else if (Config.blueprints) {
            icon = new ItemStack(Blueprints.Anther_Table, 1);
        }
        return icon;
    }
}