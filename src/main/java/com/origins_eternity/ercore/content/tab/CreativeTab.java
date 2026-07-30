package com.origins_eternity.ercore.content.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import static com.origins_eternity.ercore.ERCore.MOD_ID;

public class CreativeTab {
    public static CreativeTabs ERCORE;

    public static void addCreativeTab(ItemStack stack) {
        if (ERCORE != null) return;
        ERCORE = new CreativeTabs(MOD_ID) {
            @Override
            public ItemStack createIcon() {
                return stack;
            }
        };
    }
}
