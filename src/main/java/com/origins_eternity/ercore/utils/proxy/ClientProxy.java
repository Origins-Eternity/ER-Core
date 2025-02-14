package com.origins_eternity.ercore.utils.proxy;

import com.origins_eternity.ercore.content.gui.Overlay;
import com.origins_eternity.ercore.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.*;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.texture.MetalTextureTexture;
import slimeknights.tconstruct.library.materials.Material;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.utils.Utils.installResourcepacks;

public class ClientProxy extends CommonProxy {
    @Override
    public void construction(FMLConstructionEvent event) {
        super.construction(event);
        installResourcepacks();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        if (Loader.isModLoaded("rtg")) {
            Utils.defaultWorldtype();
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new Overlay());
    }

    @Override
    public void serverStart(FMLServerStartingEvent event) {
        super.serverStart(event);
    }

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    @Optional.Method(modid = "tconstruct")
    public static void setRenderInfo(Material material, Fluid fluid) {
        material.setRenderInfo(new MaterialRenderInfo.AbstractMaterialRenderInfo() {
            @Override
            public TextureAtlasSprite getTexture(ResourceLocation resourceLocation, String location) {
                return new MetalTextureTexture(new ResourceLocation(MOD_ID + ":materials/" + material.getIdentifier()), resourceLocation, location, fluid.getColor(), 2f, 3f, 0f);
            }
        });
    }
}