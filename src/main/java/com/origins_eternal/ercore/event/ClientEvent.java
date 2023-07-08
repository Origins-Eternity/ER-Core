package com.origins_eternal.ercore.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static com.origins_eternal.ercore.ERCore.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ClientEvent {
    public static DataParameter<Float> EnduranceData = new DataParameter<>(99, DataSerializers.FLOAT);

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void GuiOpen(GuiOpenEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if ((event.getGui() instanceof GuiSleepMP) || (event.getGui() instanceof GuiChat) || (event.getGui() instanceof GuiIngameMenu)) {
            Set<String> tags = player.getTags();
            if (tags.contains("rest")) {
                event.setCanceled(true);
            }
        }
    }
}