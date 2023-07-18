package com.origins_eternity.ercore.utils.registry;

import com.origins_eternity.ercore.content.block.Ores;
import com.origins_eternity.ercore.content.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeRegister {
    public static void addFuranceRecipes(Block input, Item output, float xp) {
        GameRegistry.addSmelting(input, new ItemStack(output), xp);
    }
    public static void registerRecipes() {
        addFuranceRecipes(Ores.Tungsten_ore, Items.Tungsten_Ingot, 1f);
    }
}
