package com.origins_eternity.ercore.compat.botania;

import com.origins_eternity.ercore.content.potion.Potions;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.recipe.RecipeBrew;

public class EnergyBrew extends Brew {
    public static EnergyBrew energy;
    public static RecipeBrew energyBrew;

    public EnergyBrew(String key, int color, int cost, PotionEffect... effects) {
        super(key, key, color, cost, effects);
        BotaniaAPI.registerBrew(this);
    }

    @Override
    public String getUnlocalizedName() {
        return "ercore.brew." + super.getUnlocalizedName();
    }

    public static void addEnergyBrew() {
        energy = new EnergyBrew("energy", 0xFF52ef8e, 6000, new PotionEffect(Potions.Energy, 1200, 2), new PotionEffect(MobEffects.STRENGTH, 800, 1), new PotionEffect(MobEffects.RESISTANCE, 600, 1));
        energyBrew = BotaniaAPI.registerBrewRecipe(energy, new ItemStack(Items.NETHER_WART),new ItemStack(Items.SUGAR), new ItemStack(Items.RABBIT_FOOT), new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.GHAST_TEAR));
    }
}