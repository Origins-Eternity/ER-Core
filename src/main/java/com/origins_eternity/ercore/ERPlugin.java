package com.origins_eternity.ercore;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.config.Configuration.Compat;
import static com.origins_eternity.ercore.config.Configuration.Features;

@IFMLLoadingPlugin.Name("ERPlugin")
public class ERPlugin implements IFMLLoadingPlugin {
    public static Logger logger = LogManager.getLogger(MOD_ID);

    public ERPlugin() {
        ArrayList<String> targetModIds = getTargetModIds();

        File modsDir = new File(Launch.minecraftHome, "mods");
        File[] files = modsDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (targetModIds.isEmpty() || files == null) return;

        for (File jar : files) {
            if (jar.isFile() && jar.getName().endsWith(".jar")) {
                String modId = getModIdFromJar(jar);
                if (targetModIds.contains(modId)) {
                    try {
                        Launch.classLoader.addURL(jar.toURI().toURL());
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to prepare target classpath!", e);
                    }
                    registerMixinConfigs(modId);
                }
            }
        }
        if (Features.enableSlowness) {
            registerMixinConfigs("minecraft");
        }
    }

    @Nonnull
    private static ArrayList<String> getTargetModIds() {
        ArrayList<String> targetModIds = new ArrayList<>();
        if (Compat.tankStack > 0) {
            targetModIds.add("pyrotech");
        }
        if (Compat.enableGrave) {
            targetModIds.add("gravestone");
        }
        if (Compat.enableReplace) {
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