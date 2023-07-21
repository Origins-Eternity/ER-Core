package com.origins_eternity.ercore.utils.registry;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import static com.origins_eternity.ercore.ERCore.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class OredictRegister {
    @SubscribeEvent
    public static void registerOres(RegistryEvent.Register<Item> event) {
        OreDictionary.registerOre("toolHoe", Items.DIAMOND_HOE);
        OreDictionary.registerOre("toolHoe", Items.GOLDEN_HOE);
        OreDictionary.registerOre("toolHoe", Items.IRON_HOE);
        OreDictionary.registerOre("toolHoe", Items.STONE_HOE);
        OreDictionary.registerOre("toolHoe", Items.WOODEN_HOE);
        OreDictionary.registerOre("toolShovel", Items.DIAMOND_SHOVEL);
        OreDictionary.registerOre("toolShovel", Items.GOLDEN_SHOVEL);
        OreDictionary.registerOre("toolShovel", Items.IRON_SHOVEL);
        OreDictionary.registerOre("toolShovel", Items.STONE_SHOVEL);
        OreDictionary.registerOre("toolShovel", Items.WOODEN_SHOVEL);
    }
}