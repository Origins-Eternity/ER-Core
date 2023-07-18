package com.origins_eternity.ercore.event;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.utils.proxy.ClientProxy.mc;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ClientEvent {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
        Block block = event.getState().getBlock();
        if (block.equals(Blocks.STONE)) {
            event.setNewState(Utils.getBlockstate("taiga:basalt_block", Blocks.STONE));
        } else if (block.equals(Blocks.COBBLESTONE)) {
            event.setNewState(Utils.getBlockstate("chisel:basalt", Blocks.COBBLESTONE));
        } else if (block.equals(Blocks.OBSIDIAN)) {
            event.setNewState(Utils.getBlockstate("advancedrocketry:basalt", Blocks.OBSIDIAN));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void GuiOpen(GuiOpenEvent event) {
        EntityPlayer player = mc().player;
        if ((event.getGui() instanceof GuiSleepMP) || (event.getGui() instanceof GuiChat)) {
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            if (endurance.isExhausted()) {
                event.setCanceled(true);
            }
        }
    }
}