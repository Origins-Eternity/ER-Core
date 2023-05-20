package com.origins_eternal.ercore.utils.proxy;

import com.origins_eternal.ercore.content.gui.Overlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.IOException;

import static com.origins_eternal.ercore.utils.Utils.*;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) { super.preInit(event); }

    @Override
    public void construct(FMLConstructionEvent event) throws IOException {
        setChinese();
        moveFiles();
        installResourcepacks();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        if (Loader.isModLoaded("rtg")) {
            defaultWorldtype();
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new Overlay());
    }
}