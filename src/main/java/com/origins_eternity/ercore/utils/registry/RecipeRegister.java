package com.origins_eternity.ercore.utils.registry;

import com.origins_eternity.ercore.content.block.Blocks;
import com.origins_eternity.ercore.content.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeRegister {
    public static void addFuranceRecipes(Item input, Item output, float xp) {
        GameRegistry.addSmelting(input, new ItemStack(output), xp);
    }
    public static void registerRecipes() {
        addFuranceRecipes(Item.getItemFromBlock(Blocks.Tungsten_Ore), Items.Tungsten_Ingot, 1f);
        addFuranceRecipes(Items.Tungsten_Dust, Items.Tungsten_Ingot, 1f);
    }
}
