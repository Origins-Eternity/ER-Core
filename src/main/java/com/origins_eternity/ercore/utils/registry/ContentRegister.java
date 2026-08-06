package com.origins_eternity.ercore.utils.registry;

import com.origins_eternity.ercore.content.block.Blocks;
import com.origins_eternity.ercore.content.block.FluidBlocks;
import com.origins_eternity.ercore.content.fluid.Fluids;
import com.origins_eternity.ercore.content.item.Blueprints;
import com.origins_eternity.ercore.content.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.config.Configuration.Contents;
import static com.origins_eternity.ercore.config.Configuration.Features;
import static com.origins_eternity.ercore.content.block.Blocks.BLOCKITEMS;
import static com.origins_eternity.ercore.content.block.FluidBlocks.FLUIDBLOCKS;
import static com.origins_eternity.ercore.content.potion.Potions.*;
import static com.origins_eternity.ercore.utils.Utils.getOreDictName;
import static slimeknights.tconstruct.tools.TinkerTraits.autosmelt;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ContentRegister {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if (Contents.enableOres) {
            for (Block block : Blocks.BLOCKS) {
                event.getRegistry().register(block);
            }
        }
        if (Contents.enableFluids) {
            for (Block fluidblock : FluidBlocks.FLUIDBLOCKS) {
                event.getRegistry().register(fluidblock);
            }
        }
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        if (Features.enableEndurance) {
            for (Potion potion : POTIONS) {
                event.getRegistry().register(potion);
            }
        }
    }

    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        if (Features.enableEndurance) {
            event.getRegistry().registerAll(Energy_Potion, Long_Energy_Potion, Strong_Energy_Potion);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if (Contents.enableItems) {
            for (Item item : Items.ITEMS) {
                String itemName = item.getTranslationKey();
                if (item.equals(Items.Ingot_Clay_Cast)) {
                    if (Loader.isModLoaded("tconstruct")) {
                        event.getRegistry().register(item);
                    }
                } else {
                    event.getRegistry().register(item);
                    if (itemName.contains("ingot")) {
                        OreDictionary.registerOre(getOreDictName("ingot", itemName), item);
                    } else if (itemName.contains("nugget")) {
                        OreDictionary.registerOre(getOreDictName("nugget", itemName), item);
                    } else if (itemName.contains("dust")) {
                        OreDictionary.registerOre(getOreDictName("dust", itemName), item);
                    }
                }
            }
        }
        if (Contents.enableOres) {
            for (Item blockitem : BLOCKITEMS) {
                event.getRegistry().register(blockitem);
                if (blockitem.getTranslationKey().contains("_ore")) {
                    String oreName = blockitem.getTranslationKey().replace("_ore", "");
                    String oreDictName = oreName.substring(12, 13).toUpperCase() + oreName.substring(13);
                    OreDictionary.registerOre("ore" + oreDictName, blockitem);
                }
            }
        }
        if (Contents.enableBlueprints) {
            for (Item print : Blueprints.PRINTS) {
                event.getRegistry().register(print);
                OreDictionary.registerOre("blueprint", print);
            }
        }
        if (Contents.enableFluids) {
            for (Item fluiditem : FluidBlocks.FLUIDITEMS) {
                event.getRegistry().register(fluiditem);
            }
        }
        if (Loader.isModLoaded("tconstruct") && Loader.isModLoaded("pyrotech")) {
            autosmelt.addItem(Item.REGISTRY.getObject(new ResourceLocation("pyrotech:furnace_core")));
        }
    }

    public static void registerFluids() {
        for (Fluid fluid : Fluids.FLUIDS) {
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerFluidModels() {
        for (Block fluidblock : FLUIDBLOCKS) {
            String fluidname = fluidblock.getTranslationKey().substring(12);
            ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(fluidblock), new ItemMeshDefinition() {
                @Nonnull
                @Override
                public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
                    return new ModelResourceLocation(MOD_ID + ":fluid", fluidname);
                }
            });
            ModelLoader.setCustomStateMapper(fluidblock, new StateMapperBase() {
                @Nonnull
                @Override
                protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                    return new ModelResourceLocation(MOD_ID + ":fluid", fluidname);
                }
            });
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if (Contents.enableItems) {
            for (Item item : Items.ITEMS) {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
            }
        }
        if (Contents.enableOres) {
            for (Item blockitem : BLOCKITEMS) {
                ModelLoader.setCustomModelResourceLocation(blockitem, 0, new ModelResourceLocation(Objects.requireNonNull(blockitem.getRegistryName()), "inventory"));
            }
            for (Block block : Blocks.BLOCKS) {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), "inventory"));
            }
        }
        if (Contents.enableBlueprints) {
            for (Item print : Blueprints.PRINTS) {
                ModelLoader.setCustomModelResourceLocation(print, 0, new ModelResourceLocation(MOD_ID + ":" + "blueprint", "inventory"));
            }
        }
        if (Contents.enableFluids) {
            registerFluidModels();
        }
    }
}