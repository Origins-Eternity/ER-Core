package com.origins_eternal.ercore.content;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import static com.origins_eternal.ercore.ERCore.MOD_ID;
import static com.origins_eternal.ercore.content.fluid.Fluids.FLUIDS;

public class FluidCreator extends Fluid {
    public FluidCreator(String name, int temperature, int viscosity, int density, int luminosity, int color) {
        super(name.toLowerCase(), new ResourceLocation(MOD_ID + ":blocks/molten_still"), new ResourceLocation(MOD_ID + ":blocks/molten_flowing"), color);
        setUnlocalizedName(MOD_ID + "." + name.toLowerCase());
        setTemperature(temperature);
        setViscosity(viscosity);
        setDensity(density);
        setLuminosity(luminosity);
        FLUIDS.add(this);
    }
}