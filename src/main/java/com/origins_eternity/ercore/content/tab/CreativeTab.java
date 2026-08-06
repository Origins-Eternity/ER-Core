package com.origins_eternity.ercore.content.tab;

import com.origins_eternity.ercore.content.block.Blocks;
import com.origins_eternity.ercore.content.block.FluidBlocks;
import com.origins_eternity.ercore.content.item.Blueprints;
import com.origins_eternity.ercore.content.item.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.config.Configuration.Contents;

public class CreativeTab {
    public static CreativeTabs ERCORE;

    public static void addCreativeTab() {
        ItemStack stack;
        if (Contents.enableItems) {
            stack = new ItemStack(Items.Tungsten_Ingot);
        } else if (Contents.enableOres) {
            stack = new ItemStack(Blocks.Tungsten_Ore);
        } else if (Contents.enableFluids) {
            stack = new ItemStack(FluidBlocks.Molten_Tungsten);
        } else if (Contents.enableBlueprints) {
            stack = new ItemStack(Blueprints.Crafting_Table);
        } else {
            stack = ItemStack.EMPTY;
        }
        if (stack.isEmpty()) return;

        ERCORE = new CreativeTabs(MOD_ID) {
            @Override
            public ItemStack createIcon() {
                return stack;
            }
        };
    }
}
