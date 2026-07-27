package com.origins_eternity.ercore.content.item;

import com.origins_eternity.ercore.content.ItemCreator;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Blueprints {
    public static final List<Item> PRINTS = new ArrayList<>();

    public static final Item Basic_Workshop = new ItemCreator("Basic_Workshop_Blueprint", 10);
    public static final Item Crafting_Table = new ItemCreator("Crafting_Table_Blueprint", 10);
    public static final Item Engineer_Workstation = new ItemCreator("Engineer_Workstation_Blueprint", 10);
    public static final Item Furnace_Core = new ItemCreator("Furnace_Core_Blueprint", 10);
    public static final Item Engineer_Workshop = new ItemCreator("Engineer_Workshop_Blueprint", 10);
    public static final Item Wither_Forge = new ItemCreator("Wither_Forge_Blueprint", 10);
    public static final Item Potter_Workshop = new ItemCreator("Potter_Workshop_Blueprint", 10);
    public static final Item Bag = new ItemCreator("Bag_Blueprint", 10);
    public static final Item Gunpowder = new ItemCreator("Gunpowder_Blueprint", 10);
    public static final Item Tool = new ItemCreator("Tool_Blueprint", 10);
    public static final Item Petal_Apothecary = new ItemCreator("Petal_Apothecary_Blueprint", 10);
    public static final Item Filter = new ItemCreator("Filter_Blueprint", 10);
}