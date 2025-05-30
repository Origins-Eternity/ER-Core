package com.origins_eternity.ercore.content.fluid;

import com.origins_eternity.ercore.content.FluidCreator;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;

public class Fluids {
    public static final List<Fluid> FLUIDS = new ArrayList<>();
    public static final Fluid Tungsten = new FluidCreator("Tungsten", 1500, 3000, 3000, 6, 0xFFe1e6e8);
    public static final Fluid Sugarcane = new FluidCreator("Sugarcane", 100, 3500, 3000, 5, 0xFF947153);
    public static final Fluid Tungsten_Carbide = new FluidCreator("Tungsten_Carbide", 1435, 3500, 2500, 7, 0xFF27282c);
    public static final Fluid Tungsten_Steel = new FluidCreator("Tungsten_Steel", 1000, 3000, 2500, 4, 0xFF3c3c3c);
}