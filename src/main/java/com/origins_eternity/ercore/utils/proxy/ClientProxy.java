package com.origins_eternity.ercore.utils.proxy;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.gui.Overlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ClientProxy extends CommonProxy {
    @Override
    public void construction(FMLConstructionEvent event) {
        super.construction(event);
        setLang();
        moveResources();
        installResources();
        if (Configuration.enableBackup) {
            backupResource();
        }
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        if (Loader.isModLoaded("rtg")) {
            defaultWorldtype();
        };
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new Overlay());
    }

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public static String gameDir() {
        return mc().gameDir.getPath();
    }

    @Optional.Method(modid = "rtg")
    private static void defaultWorldtype() {
        for (int i = 0; i < WorldType.WORLD_TYPES.length; i++) {
            if (WorldType.WORLD_TYPES[i] == WorldType.byName("RTG")) {
                WorldType defaultype = WorldType.WORLD_TYPES[0];
                WorldType.WORLD_TYPES[0] = WorldType.WORLD_TYPES[i];
                WorldType.WORLD_TYPES[i] = defaultype;
                break;
            }
        }
    }

    private static void moveResources() {
        for (String resourcepack : Configuration.resourcepacks) {
            File pack = new File(gameDir() + "\\resourcepacks\\" + resourcepack);
            File backup = new File(gameDir() + "\\backup\\" + resourcepack);
            File origin = new File(gameDir() + "\\mods\\" + resourcepack + ".disabled");
            if ((origin.exists()) && (!pack.exists())) {
                origin.renameTo(pack);
            } else if ((backup.exists()) && (!pack.exists())) {
                backup.renameTo(pack);
            }
        }
    }

    private static void installResources() {
        ResourcePackRepository Repository = mc().getResourcePackRepository();
        Repository.updateRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> Packs = Repository.getRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> Resourcepacks = new ArrayList<>();
        for (ResourcePackRepository.Entry pack : Packs) {
            for (String resourcepack : Configuration.resourcepacks) {
                if (pack.getResourcePackName().equals(resourcepack)) {
                    Resourcepacks.add(pack);
                }
            }
            Repository.setRepositories(Resourcepacks);
        }
    }

    private static void backupResource() {
        for (String resourcepack : Configuration.resourcepacks) {
            File pack = new File(gameDir() + "\\resourcepacks\\" + resourcepack);
            File backup = new File(gameDir() + "\\backup\\" + resourcepack);
            File folder = new File(gameDir() + "\\backup\\");
            if ((pack.exists()) && (!backup.exists())) {
                boolean dir = folder.mkdir();
                if (dir) {
                    try {
                        Files.copy(pack.toPath(), backup.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
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