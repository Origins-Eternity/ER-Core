package com.origins_eternal.ercore.utils.registry;

import com.origins_eternal.ercore.config.Config;
import com.origins_eternal.ercore.content.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

import static com.origins_eternal.ercore.content.item.Items.Obsidian_Magic_Shard;
import static com.origins_eternal.ercore.content.item.Items.Tungsten_Steel_Ingot;
import static com.origins_eternal.ercore.utils.Utils.setRenderInfo;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

public class MaterialRegister {
    @Optional.Method(modid = "tconstruct")
    private static Material Obsidian_Magic() {
        return new Material("obsidian_magic", TextFormatting.DARK_PURPLE).addTrait(duritos, "bowstring").addTrait(duritos, "head").addTrait(duritos, "handle").addTrait(duritos, "extra");
    }

    @Optional.Method(modid = "tconstruct")
    private static Material Tungsten_Steel() {
        return new Material("tungsten_steel", TextFormatting.DARK_GRAY).addTrait(heavy).addTrait(sharp, "head").addTrait(sharp, "bowstring").addTrait(dense, "handle").addTrait(stiff, "extra").addTrait(dense, "extra");
    }

    @Optional.Method(modid = "tconstruct")
    private static void preInitMaterials(boolean craft, boolean cast, int volume, Item item, Material material, Fluid fluid, int headDura, float headSpeed, float headAttack, float handleMod, int handleDura, int extra, int headLevel, float drawSpeed, float range, float bonusDamage) {
        TinkerRegistry.addMaterialStats(material, new HeadMaterialStats(headDura, headSpeed, headAttack, headLevel));
        TinkerRegistry.addMaterialStats(material, new HandleMaterialStats(handleMod, handleDura));
        TinkerRegistry.addMaterialStats(material, new ExtraMaterialStats(extra));
        TinkerRegistry.addMaterialStats(material, new BowMaterialStats(drawSpeed, range, bonusDamage));
        material.setFluid(fluid).setCraftable(craft).setCastable(cast).addItem(item, 1, volume);
        material.setRepresentativeItem(item);
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            setRenderInfo(material, fluid);
        }
        TinkerRegistry.integrate(new MaterialIntegration(material, fluid)).toolforge().preInit();
    }

    @Optional.Method(modid = "tconstruct")
    public static void preTinker() {
        preInitMaterials(false, true, 72, Obsidian_Magic_Shard, Obsidian_Magic(), Fluids.Obsidian_Magic, 200, 7.07f, 4.2f, 0.9f, -100, 90, 5, 5f, 0.4f, -1f);
        preInitMaterials(false, true, 144, Tungsten_Steel_Ingot, Tungsten_Steel(), Fluids.Tungsten_Steel, 2000, 8f, 10f, 3f, 100, 100, 9, 3f, 4f, 7f);
    }
}