package com.origins_eternity.ercore.utils.proxy;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.gui.Overlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void construction(FMLConstructionEvent event) {
        super.construction(event);
        setLang();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new Overlay());
    }

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    private static void setLang() {
        String language = System.getProperty("user.language");
        String[] lang = Configuration.languages;
        for (String code : lang) {
            if (code.contains(language)) {
                mc().getLanguageManager().setCurrentLanguage(new Language(code, "", "", false));
            }
        }
    }
}