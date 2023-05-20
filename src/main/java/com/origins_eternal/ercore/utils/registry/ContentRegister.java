package com.origins_eternal.ercore.utils.registry;

import com.origins_eternal.ercore.config.Config;
import com.origins_eternal.ercore.content.block.Blocks;
import com.origins_eternal.ercore.content.block.FluidBlocks;
import com.origins_eternal.ercore.content.block.Ores;
import com.origins_eternal.ercore.content.item.Blueprints;
import com.origins_eternal.ercore.content.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.origins_eternal.ercore.ERCore.MOD_ID;
import static com.origins_eternal.ercore.content.block.Blocks.BLOCKITEMS;
import static com.origins_eternal.ercore.content.block.FluidBlocks.FLUIDBLOCKS;
import static com.origins_eternal.ercore.content.block.Ores.OREITEMS;

@Mod.EventBusSubscriber
public class ContentRegister {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if (Config.blocks) {
            for (Block block : Blocks.BLOCKS) {
                event.getRegistry().register(block);
            }
        }
        if (Config.ores) {
            for (Block ore : Ores.ORES) {
                event.getRegistry().register(ore);
                String blockname = ore.getTranslationKey().replace("_ore", "");
                String oredictname = blockname.substring(12, 13).toUpperCase() + blockname.substring(13);
                OreDictionary.registerOre("ore" + oredictname, ore);
            }
        }
        for (Block fluidblock : FluidBlocks.FLUIDBLOCKS) {
            event.getRegistry().register(fluidblock);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if (Config.items) {
            for (Item item : Items.ITEMS) {
                String itemname = item.getTranslationKey();
                event.getRegistry().register(item);
                if (itemname.contains("ingot")) {
                    String ingotname = itemname.replaceAll("_", "").replace("ingot", "");
                    String oredictname = ingotname.substring(12, 13).toUpperCase() + ingotname.substring(13);
                    OreDictionary.registerOre("ingot" + oredictname, item);
                } else if (itemname.contains("nugget")) {
                    String nuggetname = itemname.replaceAll("_", "").replace("nugget", "");
                    String oredictname = nuggetname.substring(12, 13).toUpperCase() + nuggetname.substring(13);
                    OreDictionary.registerOre("nugget" + oredictname, item);
                } else if (!(itemname.contains("_"))) {
                    OreDictionary.registerOre(itemname, item);
                }
            }
        }
        if (Config.ores) {
            for (Item oreitem : OREITEMS) {
                event.getRegistry().register(oreitem);
            }
        }
        if (Config.blocks) {
            for (Item blockitem : BLOCKITEMS) {
                event.getRegistry().register(blockitem);
            }
        }
        if (Config.blueprints) {
            for (Item print : Blueprints.PRINTS) {
                event.getRegistry().register(print);
                String itemname = print.getTranslationKey();
                OreDictionary.registerOre("blueprint", print);
                if (itemname.contains("workshop")) {
                    OreDictionary.registerOre("blueprintWorkshop", print);
                } else if (itemname.contains("workstation")) {
                    OreDictionary.registerOre("blueprintWorkstation", print);
                }
            }
        }
        for (Item fluiditem : FluidBlocks.FLUIDITEMS) {
            event.getRegistry().register(fluiditem);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        if (Config.items) {
            for (Item item : Items.ITEMS) {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
            }
        }
        if (Config.ores) {
            for (Item oreitem : OREITEMS) {
                ModelLoader.setCustomModelResourceLocation(oreitem, 0, new ModelResourceLocation(Objects.requireNonNull(oreitem.getRegistryName()), "inventory"));
            }
        }
        if (Config.blocks) {
            for (Item blockitem : BLOCKITEMS) {
                ModelLoader.setCustomModelResourceLocation(blockitem, 0, new ModelResourceLocation(Objects.requireNonNull(blockitem.getRegistryName()), "inventory"));
            }
        }
        if (Config.blueprints) {
            for (Item print : Blueprints.PRINTS) {
                ModelLoader.setCustomModelResourceLocation(print, 0, new ModelResourceLocation(MOD_ID + ":" + "blueprint", "inventory"));
            }
        }
        if (Config.blocks) {
            for (Block block : Blocks.BLOCKS) {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), "inventory"));
            }
        }
        if (Config.ores) {
            for (Block ore : Ores.ORES) {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ore), 0, new ModelResourceLocation(Objects.requireNonNull(ore.getRegistryName()), "inventory"));
            }
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
        registerModels();
        registerFluidModels();
    }
}