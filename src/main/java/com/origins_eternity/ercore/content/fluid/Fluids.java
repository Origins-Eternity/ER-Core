package com.origins_eternity.ercore.content.fluid;

import com.origins_eternity.ercore.content.FluidCreator;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;

public class Fluids {
    public static final List<Fluid> FLUIDS = new ArrayList<>();
    public static final Fluid Tungsten = new FluidCreator("Tungsten", 3000, 10000, 1760, 15, 0xFF898887);
    public static final Fluid Sugarcane = new FluidCreator("Sugarcane", 500, 4000, 1500, 0, 0xFF966432);
    public static final Fluid Tungsten_Steel = new FluidCreator("Tungsten_Steel", 2800, 15000, 1550, 15, 0xFF767574);
}