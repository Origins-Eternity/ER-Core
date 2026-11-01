package com.origins_eternity.ercore;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static com.origins_eternity.ercore.config.Configuration.Compat;
import static com.origins_eternity.ercore.config.Configuration.Features;

@IFMLLoadingPlugin.Name("ERPlugin")
public class ERPlugin implements IFMLLoadingPlugin {
    public ERPlugin() {
        ArrayList<String> targetModIds = getTargetModIds();

        File modsDir = new File(Launch.minecraftHome, "mods");
        File[] files = modsDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (targetModIds.isEmpty() || files == null) return;

        for (File jar : files) {
            if (jar.isFile() && jar.getName().endsWith(".jar")) {
                String modId = getModIdFromJar(jar);
                if (targetModIds.contains(modId)) {
                    markAsReparseable(jar.getName());
                    try {
                        Launch.classLoader.addURL(jar.toURI().toURL());
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to prepare target classpath!", e);
                    }
                    registerMixinConfigs(modId);
                }
            }
        }
        if (Features.betterSlowness) {
            registerMixinConfigs("minecraft");
        }
    }

    private static void markAsReparseable(String fileName) {
        try {
            List<String> list = CoreModManager.getReparseableCoremods();
            if (!list.contains(fileName)) {
                list.add(fileName);
            }
        } catch (Throwable t) {
            System.out.println("[ERCore] Failed to mark reparseable: " + fileName + " - " + t);
        }
    }

    @Nonnull
    private static ArrayList<String> getTargetModIds() {
        ArrayList<String> targetModIds = new ArrayList<>();
        if (Compat.betterArcFurnace) {
            targetModIds.add("immersiveengineering");
        }
        if (Compat.betterOceanCrops) {
            targetModIds.add("oe");
        }
        if (Compat.enableGrave) {
            targetModIds.add("gravestone");
        }
        if (Compat.enableLootr) {
            targetModIds.add("lootr");
        }
        if (Compat.enableMaxHealth) {
            targetModIds.add("firstaid");
        }
        if (Compat.enableThirstOverlay) {
            targetModIds.add("toughasnails");
        }
        if (Compat.enableThirstQuencher) {
            targetModIds.add("tanaddons");
        }
        if (Compat.enableRock) {
            targetModIds.add("pyrotech");
        }
        return targetModIds;
    }

    private String getModIdFromJar(File jar) {
        try (JarFile jf = new JarFile(jar)) {
            ZipEntry entry = jf.getEntry("mcmod.info");
            if (entry == null) return null;
            JsonElement root = new Gson().fromJson(new InputStreamReader(jf.getInputStream(entry)), JsonElement.class);
            JsonArray modList = root.isJsonArray() ? root.getAsJsonArray()
                    : root.getAsJsonObject().getAsJsonArray("modList");
            if (modList != null && modList.size() > 0) {
                return modList.get(0).getAsJsonObject().get("modid").getAsString();
            }
        } catch (Exception ignored) {}
        return null;
    }

    private void registerMixinConfigs(String modId) {
        Mixins.addConfiguration("assets/ercore/mixins/mixins." + modId + ".json");
    }

    @Override public String[] getASMTransformerClass() { return new String[0]; }

    @Override public String getModContainerClass() { return null; }

    @Override public String getSetupClass() { return null; }

    @Override public void injectData(Map<String, Object> data) {}

    @Override public String getAccessTransformerClass() { return null; }
}