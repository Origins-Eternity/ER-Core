package com.origins_eternity.ercore.compat.crafttweaker;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.origins_eternity.ercore.content.damage.Damages.EXHAUSTED;

@ZenRegister
@ZenExpansion("crafttweaker.player.IPlayer")
public class CraftTweaker {
    @ZenMethod
    public static int getEndurance(IPlayer iplayer) {
        IEndurance endurance = CraftTweakerMC.getPlayer(iplayer).getCapability(Capabilities.ENDURANCE, null);
        return endurance.getEndurance();
    }

    @ZenMethod
    public static void addEndurance(IPlayer iplayer, int value) {
        EntityPlayer player = CraftTweakerMC.getPlayer(iplayer);
        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
        if (value > 0) {
            endurance.recoverEndurance(value);
        } else if (value < 0) {
            endurance.consumeEndurance(-value);
            if (endurance.getEndurance() <= 0) {
                player.attackEntityFrom(EXHAUSTED, 1f);
            }
        }
    }
}