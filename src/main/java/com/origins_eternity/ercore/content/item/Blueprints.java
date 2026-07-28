package com.origins_eternity.ercore.content.item;

import com.origins_eternity.ercore.content.ItemCreator;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Blueprints {
    public static final List<Item> PRINTS = new ArrayList<>();

    public static final Item Bag = new ItemCreator("Bag_Blueprint", 12);
    public static final Item Tool = new ItemCreator("Tool_Blueprint", 12);
    public static final Item Gunpowder = new ItemCreator("Gunpowder_Blueprint", 12);
    public static final Item Workstation = new ItemCreator("Workstation_Blueprint", 12);
    public static final Item Workshop = new ItemCreator("Workshop_Blueprint", 12);
    public static final Item Crafting_Table = new ItemCreator("Crafting_Table_Blueprint", 10);
    public static final Item Furnace_Core = new ItemCreator("Furnace_Core_Blueprint", 8);
    public static final Item Brewing_Stand = new ItemCreator("Brewing_Stand_Blueprint", 8);
    public static final Item Wither_Forge = new ItemCreator("Wither_Forge_Blueprint", 8);
    public static final Item Altar = new ItemCreator("Altar_Blueprint", 8);
    public static final Item Filter = new ItemCreator("Filter_Blueprint", 8);
}