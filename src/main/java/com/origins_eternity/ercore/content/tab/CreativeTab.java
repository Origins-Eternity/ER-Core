package com.origins_eternity.ercore.content.tab;

import com.origins_eternity.ercore.content.block.Blocks;
import com.origins_eternity.ercore.content.fluid.Fluids;
import com.origins_eternity.ercore.content.item.Blueprints;
import com.origins_eternity.ercore.content.item.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.compat.firstaid.Herbs.Herbs;
import static com.origins_eternity.ercore.config.Configuration.Compat;
import static com.origins_eternity.ercore.config.Configuration.Contents;

public class CreativeTab {
    public static final CreativeTabs ERCORE = new CreativeTabs(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            ItemStack icon = new ItemStack(net.minecraft.init.Items.RABBIT_FOOT, 1);
            if (Contents.enableItems) {
                icon = new ItemStack(Items.Tungsten_Ingot, 1);
            } else if (Contents.enableOres) {
                icon = new ItemStack(Blocks.Copper_Ore, 1);
            } else if (Contents.enableBlueprints) {
                icon = new ItemStack(Blueprints.Crafting_Table, 1);
            } else if (Contents.enableFluids) {
                icon = new ItemStack(Fluids.Tungsten.getBlock());
            } else if ((Compat.enableHerbs) && (Loader.isModLoaded("firstaid"))) {
                icon = new ItemStack(Herbs, 1);
            }
            return icon;
        }
    };
}
