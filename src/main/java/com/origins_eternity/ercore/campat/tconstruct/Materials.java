package com.origins_eternity.ercore.campat.tconstruct;

import com.origins_eternity.ercore.content.block.Ores;
import com.origins_eternity.ercore.content.fluid.Fluids;
import com.origins_eternity.ercore.content.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

import static com.origins_eternity.ercore.utils.proxy.ClientProxy.setRenderInfo;
import static slimeknights.tconstruct.library.TinkerRegistry.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

public class Materials {
    private static Material Tungsten_Steel = new Material("tungsten_steel", TextFormatting.DARK_GRAY).addTrait(heavy).addTrait(sharp, "head").addTrait(sharp, "bowstring").addTrait(dense, "handle").addTrait(stiff, "extra").addTrait(dense, "extra");

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

    public static void preTinker() {
        preInitMaterials(false, true, 144, Items.Tungsten_Steel_Ingot, Tungsten_Steel, Fluids.Tungsten_Steel, 2000, 8f, 10f, 3f, 100, 100, 5, 3f, 4f, 7f);
    }

    public static void addTinkerMelting() {
        registerMelting(Items.Tungsten_Nugget, Fluids.Tungsten, 72);
        registerMelting(Items.Tungsten_Ingot, Fluids.Tungsten, 144);
        registerMelting(Ores.Tungsten_ore, Fluids.Tungsten, 144);
        registerMelting(Items.Tungsten_Carbide_Nugget, Fluids.Tungsten_Carbide, 72);
        registerMelting(Items.Tungsten_Carbide_Ingot, Fluids.Tungsten_Carbide, 144);
        registerMelting(Items.Tungsten_Steel_Nugget, Fluids.Tungsten_Steel, 72);
        registerMelting(Items.Tungsten_Steel_Ingot, Fluids.Tungsten_Steel, 144);
    }

    public static void addTinkerAlloying() {
        registerAlloy(new FluidStack(Fluids.Tungsten_Steel, 1), new FluidStack(Fluids.Tungsten, 1), new FluidStack(Fluids.Tungsten_Carbide, 3));
    }

    public static void addTinkerCasting() {
        registerBasinCasting(new ItemStack(Items.Tungsten_Ingot), ItemStack.EMPTY, Fluids.Tungsten, 144);
        registerBasinCasting(new ItemStack(Items.Tungsten_Carbide_Ingot), ItemStack.EMPTY, Fluids.Tungsten_Carbide, 144);
    }
}