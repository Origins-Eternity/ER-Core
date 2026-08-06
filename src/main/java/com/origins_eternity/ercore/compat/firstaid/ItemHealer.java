package com.origins_eternity.ercore.compat.firstaid;

import com.origins_eternity.ercore.ERCore;
import ichttt.mods.firstaid.api.damagesystem.AbstractPartHealer;
import ichttt.mods.firstaid.api.item.ItemHealing;
import ichttt.mods.firstaid.common.damagesystem.PartHealer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.content.tab.CreativeTab.ERCORE;

public class ItemHealer extends ItemHealing {
    private static ItemHealer herbs;

    public ItemHealer(String name, Function<ItemStack, AbstractPartHealer> healerFunction, Function<ItemStack, Integer> time) {
        super(healerFunction, time);
        setTranslationKey(ERCore.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setMaxStackSize(16);
        setCreativeTab(ERCORE);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        herbs = new ItemHealer("Herbs", stack -> new PartHealer(15 * 20, 2, stack), stack -> 2000);
        event.getRegistry().register(herbs);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(herbs, 0, new ModelResourceLocation(MOD_ID + ":" + "herbs", "inventory"));
    }
 }