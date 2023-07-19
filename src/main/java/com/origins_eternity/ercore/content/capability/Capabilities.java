package com.origins_eternity.ercore.content.capability;

import com.origins_eternity.ercore.content.capability.endurance.Endurance;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
    @CapabilityInject(IEndurance.class)
    public static final Capability<IEndurance> ENDURANCE = null;

    public static void registerCapability(CapabilityManager manager) {
        manager.register(IEndurance.class, new Endurance.EnduranceStorage(), Endurance::new);
    }
}
