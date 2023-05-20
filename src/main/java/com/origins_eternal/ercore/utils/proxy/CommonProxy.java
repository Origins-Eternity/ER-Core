package com.origins_eternal.ercore.utils.proxy;

import com.origins_eternal.ercore.config.Config;
import com.origins_eternal.ercore.gen.GenOres;
import com.origins_eternal.ercore.handler.TiredHandler;
import com.origins_eternal.ercore.message.TiredMessage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

import static com.origins_eternal.ercore.ERCore.packetHandler;
import static com.origins_eternal.ercore.utils.registry.FluidRegister.registerFluids;
import static com.origins_eternal.ercore.utils.registry.MaterialRegister.preTinker;
import static com.origins_eternal.ercore.utils.registry.RecipeRegister.registerRecipes;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        registerFluids();
        Config.registerConfig(event);
        if (Loader.isModLoaded("tconstruct")) {
            preTinker();
        }
        GameRegistry.registerWorldGenerator(new GenOres(), 0);
        packetHandler.registerMessage(TiredHandler.class, TiredMessage.class, 0, Side.SERVER);
    }

    public void construct(FMLConstructionEvent event) throws IOException {

    }

    public void init(FMLInitializationEvent event) {
        registerRecipes();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}