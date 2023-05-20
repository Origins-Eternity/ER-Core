package com.origins_eternal.ercore.utils.registry;

import com.origins_eternal.ercore.content.fluid.Fluids;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidRegister {
    public static void registerFluids() {
        for (Fluid fluid : Fluids.FLUIDS) {
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);
        }
    }
}
