package com.origins_eternity.ercore.campat.firstaid;

import com.origins_eternity.ercore.ERCore;
import ichttt.mods.firstaid.api.damagesystem.AbstractPartHealer;
import ichttt.mods.firstaid.api.item.ItemHealing;
import ichttt.mods.firstaid.common.damagesystem.PartHealer;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

import static com.origins_eternity.ercore.content.tab.CreativeTab.ERCORE;

public class Items extends ItemHealing {
    public static final ItemHealing Herbs = new Items("Herbs", stack -> new PartHealer(23 * 20, 2, stack), stack -> 2000);

    public Items(String name, Function<ItemStack, AbstractPartHealer> healerFunction, Function<ItemStack, Integer> time) {
        super(healerFunction, time);
        setTranslationKey(ERCore.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setMaxStackSize(16);
        setCreativeTab(ERCORE);
    }
}