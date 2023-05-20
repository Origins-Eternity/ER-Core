package com.origins_eternal.ercore.content;

import net.minecraft.item.Item;

import static com.origins_eternal.ercore.ERCore.ERCORE;
import static com.origins_eternal.ercore.ERCore.MOD_ID;
import static com.origins_eternal.ercore.content.item.Blueprints.PRINTS;
import static com.origins_eternal.ercore.content.item.Items.ITEMS;

public class ItemCreator extends Item
{
    public ItemCreator(String name, int maxdamage)
    {
        setTranslationKey(MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setMaxDamage(maxdamage);
        setNoRepair();
        setCreativeTab(ERCORE);

        if (name.contains("Blueprint")) {
            PRINTS.add(this);
        } else {
            ITEMS.add(this);
        }
    }
}