package com.origins_eternity.ercore.utils.proxy;

import com.origins_eternity.ercore.content.render.Overlay;
import com.origins_eternity.ercore.event.ClientEvents;
import com.origins_eternity.ercore.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.*;
import org.lwjgl.input.Keyboard;

import static com.origins_eternity.ercore.config.Configuration.Compat;
import static com.origins_eternity.ercore.config.Configuration.Features;
import static com.origins_eternity.ercore.utils.Utils.installResourcepacks;

public class ClientProxy extends CommonProxy {
    public static KeyBinding rollKey;

    @Override
    public void construction(FMLConstructionEvent event) {
        super.construction(event);
        if (Features.enableInstallation) {
            installResourcepacks();
        }
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        if (Features.enableEndurance) {
            rollKey = new KeyBinding("key.ercore.roll", Keyboard.KEY_R, "key.categories.ercore");
            ClientRegistry.registerKeyBinding(rollKey);
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        if (Loader.isModLoaded("rtg") && Compat.enableWorldType) {
            Utils.defaultWorldtype();
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (Features.enableEndurance) {
            MinecraftForge.EVENT_BUS.register(new Overlay());
        }
    }

    @Override
    public void serverStart(FMLServerStartingEvent event) {
        super.serverStart(event);
    }

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }
}