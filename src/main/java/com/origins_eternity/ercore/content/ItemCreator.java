package com.origins_eternity.ercore.content;

import com.origins_eternity.ercore.ERCore;
import com.origins_eternity.ercore.content.item.Blueprints;
import com.origins_eternity.ercore.content.item.Items;
import net.minecraft.item.Item;

import static com.origins_eternity.ercore.content.tab.CreativeTab.ERCORE;

public class ItemCreator extends Item
{
    public ItemCreator(String name, int maxdamage)
    {
        setTranslationKey(ERCore.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setMaxDamage(maxdamage);
        setNoRepair();
        setCreativeTab(ERCORE);

        if (name.contains("Blueprint")) {
            Blueprints.PRINTS.add(this);
        } else {
            Items.ITEMS.add(this);
        }
    }
}