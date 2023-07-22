package com.origins_eternity.ercore.event;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
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
    public static void GuiOpen(GuiOpenEvent event) {
        EntityPlayer player = mc().player;
        if ((event.getGui() instanceof GuiSleepMP) || (event.getGui() instanceof GuiChat)) {
            if (player.getTags().contains("rest")) {
                event.setCanceled(true);
            }
        }
    }
}
