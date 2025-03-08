package com.origins_eternity.ercore.utils.proxy;

import com.origins_eternity.ercore.compat.firstaid.Events;
import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.command.TPA;
import com.origins_eternity.ercore.content.command.TPADeny;
import com.origins_eternity.ercore.content.command.TPAHere;
import com.origins_eternity.ercore.content.command.TPAccept;
import com.origins_eternity.ercore.gen.GenOres;
import com.origins_eternity.ercore.message.SyncEndurance;
import com.origins_eternity.ercore.utils.registry.RecipeRegister;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static com.origins_eternity.ercore.ERCore.packetHandler;
import static com.origins_eternity.ercore.compat.tconstruct.Materials.*;
import static com.origins_eternity.ercore.utils.registry.ContentRegister.registerFluids;

public class CommonProxy {
    public void construction(FMLConstructionEvent event) {

    }

    public void preInit(FMLPreInitializationEvent event) {
        if (Configuration.enableFluids) {
            registerFluids();
            if (Loader.isModLoaded("tconstruct")) {
                if ((Configuration.enableOres) && (Configuration.enableItems) && (Configuration.enableMaterial)) {
                    preTinker();
                }
            }
        }
        registerMessage();
    }

    public void init(FMLInitializationEvent event) {
        if (Configuration.enableOres) {
            RecipeRegister.registerRecipes();
            GameRegistry.registerWorldGenerator(new GenOres(), 0);
        }
        if (Loader.isModLoaded("tconstruct")) {
            if ((Configuration.enableOres) && (Configuration.enableItems) && (Configuration.enableMaterial) && (Configuration.enableFluids)) {
                addTinkerMelting();
                addTinkerAlloying();
                addTinkerCasting();
            }
        }
        if (Loader.isModLoaded("firstaid")) {
            MinecraftForge.EVENT_BUS.register(Events.class);
        }
        if (Loader.isModLoaded("lootr")) {
            MinecraftForge.EVENT_BUS.register(com.origins_eternity.ercore.compat.lootr.Events.class);
        }
        Capabilities.registerCapability(CapabilityManager.INSTANCE);
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new TPA());
        event.registerServerCommand(new TPAHere());
        event.registerServerCommand(new TPAccept());
        event.registerServerCommand(new TPADeny());
    }

    private static void registerMessage() {
        packetHandler.registerMessage(SyncEndurance.Handler.class, SyncEndurance.class, 0, Side.CLIENT);
    }
}