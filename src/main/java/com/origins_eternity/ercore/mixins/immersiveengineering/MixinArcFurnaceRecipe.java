package com.origins_eternity.ercore.mixins.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe.recipeList;

@Mixin(value = ArcFurnaceRecipe.class, remap = false)
public abstract class MixinArcFurnaceRecipe {
    /**
     * @reason Make the ArcFurnace match the recipe with additives first when the two recipe inputs are the same.
     * @author Origins_Eternity
     */
    @Overwrite
    public static ArcFurnaceRecipe findRecipe(ItemStack input, NonNullList<ItemStack> additives) {
        boolean hasAdditive = false;
        for (ItemStack add : additives) {
            if (!add.isEmpty()) {
                hasAdditive = true;
                break;
            }
        }

        if (hasAdditive) {
            for (ArcFurnaceRecipe recipe : recipeList) {
                if (recipe != null && recipe.additives.length > 0 && recipe.matches(input, additives)) {
                    return recipe;
                }
            }
        }

        for (ArcFurnaceRecipe recipe : recipeList) {
            if (recipe != null && recipe.additives.length == 0 && recipe.input.matches(input)) {
                return recipe;
            }
        }
        return null;
    }
}