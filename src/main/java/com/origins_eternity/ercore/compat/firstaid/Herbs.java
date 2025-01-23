package com.origins_eternity.ercore.compat.firstaid;

import com.origins_eternity.ercore.ERCore;
import ichttt.mods.firstaid.api.damagesystem.AbstractPartHealer;
import ichttt.mods.firstaid.api.item.ItemHealing;
import ichttt.mods.firstaid.common.damagesystem.PartHealer;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

import static com.origins_eternity.ercore.content.tab.CreativeTab.ERCORE;

public class Herbs extends ItemHealing {
    public static final ItemHealing Herbs = new Herbs("Herbs", stack -> new PartHealer(15 * 20, 2, stack), stack -> 2000);

    public Herbs(String name, Function<ItemStack, AbstractPartHealer> healerFunction, Function<ItemStack, Integer> time) {
        super(healerFunction, time);
        setTranslationKey(ERCore.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setMaxStackSize(16);
        setCreativeTab(ERCORE);
    }
}