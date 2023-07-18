package com.origins_eternity.ercore.content;

import com.origins_eternity.ercore.ERCore;
import com.origins_eternity.ercore.content.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidCreator extends Fluid {
    public FluidCreator(String name, int temperature, int viscosity, int density, int luminosity, int color) {
        super(name.toLowerCase(), new ResourceLocation(ERCore.MOD_ID + ":blocks/molten_still"), new ResourceLocation(ERCore.MOD_ID + ":blocks/molten_flowing"), color);
        setUnlocalizedName(ERCore.MOD_ID + "." + name.toLowerCase());
        setTemperature(temperature);
        setViscosity(viscosity);
        setDensity(density);
        setLuminosity(luminosity);
        Fluids.FLUIDS.add(this);
    }
}