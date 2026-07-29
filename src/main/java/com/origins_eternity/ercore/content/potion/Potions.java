package com.origins_eternity.ercore.content.potion;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;

import java.util.ArrayList;

public class Potions {
    public static final ArrayList<Potion> POTIONS = new ArrayList<>();
    public static final Potion Energy = new Energy("energy");
    public static final PotionType Energy_Potion = new PotionType("energy", new PotionEffect(Energy, 400)).setRegistryName("energy");
    public static final PotionType Long_Energy_Potion = new PotionType("energy", new PotionEffect(Energy, 1200)).setRegistryName("long_energy");
    public static final PotionType Strong_Energy_Potion = new PotionType("energy", new PotionEffect(Energy, 200, 1)).setRegistryName("strong_energy");
    public static final PotionType[] types = {Energy_Potion, Long_Energy_Potion, Strong_Energy_Potion};

    public static void registerPotionRecipes() {
        PotionHelper.addMix(PotionTypes.SWIFTNESS, Items.RABBIT_FOOT, Energy_Potion);
        PotionHelper.addMix(PotionTypes.LEAPING, Items.SUGAR, Energy_Potion);
        PotionHelper.addMix(Energy_Potion, Items.REDSTONE, Long_Energy_Potion);
        PotionHelper.addMix(Energy_Potion, Items.GLOWSTONE_DUST, Strong_Energy_Potion);

        for (PotionType type : types) {
            PotionHelper.addMix(type, Items.GUNPOWDER, type);
            PotionHelper.addMix(type, Items.DRAGON_BREATH, type);
        }
    }
}