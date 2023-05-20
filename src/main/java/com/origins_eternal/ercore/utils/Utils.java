package com.origins_eternal.ercore.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldType;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Optional;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.texture.MetalTextureTexture;
import slimeknights.tconstruct.library.materials.Material;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.origins_eternal.ercore.ERCore.MOD_ID;
import static com.origins_eternal.ercore.event.ClientEvent.EnduranceData;

public class Utils {
    public static boolean checkChinese() {
        return System.getProperty("user.language").equals("zh");
    }

    public static void moveFiles() throws IOException {
        Minecraft mc = Minecraft.getMinecraft();
        String gamepath = mc.gameDir.getPath();
        String frompath = gamepath + "\\mods\\Evolution-Reset-Resource-Pack-1.1.0.zip";
        String topath = gamepath + "\\resourcepacks\\Evolution-Reset-Resource-Pack-1.1.0.zip";
        File pack = new File(frompath);
        File resourcepack = new File(topath);
        Path resource = Paths.get(topath);
        Path backup = Paths.get(frompath);
        if (pack.exists() && !resourcepack.exists()) {
            Files.copy(backup, resource);
        } else if (!pack.exists() && resourcepack.exists()) {
            Files.copy(resource, backup);
        }
    }

    public static void installResourcepacks() {
        Minecraft mc = Minecraft.getMinecraft();
        ResourcePackRepository Repository = mc.getResourcePackRepository();
        Repository.updateRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> Packs = Repository.getRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> Resourcepacks = new ArrayList<>();
        for (ResourcePackRepository.Entry pack : Packs) {
            if (pack.getResourcePackName().equals("Evolution-Reset-Resource-Pack-1.1.0.zip")) {
                Resourcepacks.add(pack);
            }
            Repository.setRepositories(Resourcepacks);
        }
    }

    public static void setChinese() {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings gameSettings = mc.gameSettings;
        if (checkChinese()) {
            mc.getLanguageManager().setCurrentLanguage(new Language("zh_cn", "CN", "简体中文", false));
            gameSettings.language = "zh_cn";
        }
    }

    public static String checkStatus(EntityPlayer player) {
        String status = "normal";
        Set<String> tags = player.getTags();
        String registerData = "float";
        EntityDataManager dataManager = player.getDataManager();
        float maxHealth = player.getMaxHealth();
        float max = 20;
        if (maxHealth >= 20) {
            max = maxHealth;
        }
        if (tags.contains(registerData)) {
            float value = dataManager.get(EnduranceData);
            float weakness = value / max;
            if (weakness <= 0.01) {
                status = "exhausted";
            } else if (weakness < 0.25) {
                status = "tired";
            } else if (weakness < 0.99) {
                status = "normal";
            } else if (weakness >= 0.99) {
                status = "spirit";
            }

        }
        return status;
    }

    public static void addStringTags(EntityPlayer player, String tag, int second) {
        Set<String> tags = player.getTags();
        if (!tags.contains(tag)) {
            player.addTag(tag);
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    player.removeTag(tag);
                }
            };
            timer.schedule(timerTask, second * 1000L);
        }
    }

    public static void setFloatTags(EntityPlayer player, Float value) {
        EntityDataManager dataManager = player.getDataManager();
        float max = 20;
        float maxHealth = player.getMaxHealth();
        if (maxHealth >= 20) {
            max = maxHealth;
        }
        if (!player.getTags().contains("float")) {
            float origin = (float) (max * 0.25);
            dataManager.register(EnduranceData, origin);
            player.addTag("float");
        } else if (value != 0) {
            float enduranceLevel = dataManager.get(EnduranceData);
            float food = getK(player, "food");
            float health = 1 - getK(player, "health");
            float experience = getK(player, "experience");
            if (value > 0) {
                food = 1 - food;
                experience = 1 + experience;
            } else {
                food = 1 + food;
                experience = 1 - experience;
            }
            float k = (experience + food) / 2;
            float maxEndurance = max * health;
            boolean overMin = (enduranceLevel + (value * k)) < 0;
            boolean overMax = (enduranceLevel + (value * k)) > maxEndurance;
            if (overMin) {
                dataManager.set(EnduranceData, 0f);
            } else if (overMax) {
                dataManager.set(EnduranceData, maxEndurance);
            } else {
                dataManager.set(EnduranceData, enduranceLevel + (value * k));
            }
        }
    }

    public static float getK(EntityPlayer player, String type) {
        float k = 0;
        if (!player.getTags().contains("rest")) {
            float foodLevel = player.getFoodStats().getFoodLevel();
            float maxFoodLevel = 20;
            float healthLevel = player.getHealth();
            float maxHealth = player.getMaxHealth();
            float experienceLevel = player.experienceLevel;
            if (experienceLevel > 500) {
                experienceLevel = 500;
            }
            double x = -2 * (foodLevel / maxFoodLevel);
            double y = -2 * (healthLevel / maxHealth);
            double z = 2 * experienceLevel + 1;
            switch (type) {
                case "health":
                    k = (float) (Math.pow(2, y) - 0.25);
                    break;
                case "food":
                    k = (float) (Math.pow(2, x) - 0.25);
                    break;
                case "experience":
                    k = (float) (Math.log(z) * 0.25);
                    break;
            }
        }
        return k;
    }

    public static IBlockState getBlockstate(String id, Block origin) {
        Block block;
        ResourceLocation location = new ResourceLocation(id);
        block = Block.REGISTRY.getObject(location);
        if (block.equals(Blocks.AIR)) {
            block = origin;
        }
        return block.getDefaultState();
    }

    @Optional.Method(modid = "rtg")
    public static void defaultWorldtype() {
        for (int i = 0; i < WorldType.WORLD_TYPES.length; i++) {
            if (WorldType.WORLD_TYPES[i] == WorldType.byName("RTG")) {
                WorldType defaultype = WorldType.WORLD_TYPES[0];
                WorldType.WORLD_TYPES[0] = WorldType.WORLD_TYPES[i];
                WorldType.WORLD_TYPES[i] = defaultype;
                break;
            }
        }
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