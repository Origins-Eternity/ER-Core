package com.origins_eternity.ercore.utils.proxy;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.gen.GenOres;
import com.origins_eternity.ercore.message.CheckMove;
import com.origins_eternity.ercore.message.SyncEndurance;
import com.origins_eternity.ercore.utils.registry.RecipeRegister;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static com.origins_eternity.ercore.ERCore.packetHandler;
import static com.origins_eternity.ercore.campat.TConstruct.*;
import static com.origins_eternity.ercore.utils.registry.ContentRegister.registerFluids;

public class CommonProxy {
    public void construction(FMLConstructionEvent event) {

    }

    public void preInit(FMLPreInitializationEvent event) {
        if (Configuration.enableFluids) {
            registerFluids();
            if (Loader.isModLoaded("tconstruct")) {
                if ((Configuration.enableOres) && (Configuration.enableItems) && (Configuration.enableMaterial) && (Configuration.enableFluids)) {
                    preTinker();
                    addTinkerMelting();
                    addTinkerAlloying();
                    addTinkerCasting();
                }
            }
        }
        if (Configuration.enableOres) {
            GameRegistry.registerWorldGenerator(new GenOres(), 0);
        }
        registerMessage();
    }

    public void init(FMLInitializationEvent event) {
        if (Configuration.enableOres) {
            RecipeRegister.registerRecipes();
        }
        Capabilities.registerCapability(CapabilityManager.INSTANCE);
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    private static void registerMessage() {
        packetHandler.registerMessage(SyncEndurance.Handler.class, SyncEndurance.class, 0, Side.CLIENT);
        packetHandler.registerMessage(CheckMove.Handler.class, CheckMove.class, 1, Side.SERVER);
    }
}