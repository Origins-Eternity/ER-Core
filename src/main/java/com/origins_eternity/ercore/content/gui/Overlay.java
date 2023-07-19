package com.origins_eternity.ercore.content.gui;

import com.origins_eternity.ercore.config.Configuration;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.content.capability.Capabilities.ENDURANCE;

public class Overlay extends Gui {
    private final ResourceLocation gui = new ResourceLocation(MOD_ID, "textures/gui/endurance.png");

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if ((mc.playerController.gameIsSurvivalOrAdventure()) && (Configuration.showOverlay)) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
                mc.getTextureManager().bindTexture(gui);
                IEndurance endurance = player.getCapability(ENDURANCE, null);
                float value = endurance.getEndurance();
                float percent = value / 20f;
                int rest = (int) (percent * 80);
                int consume = 80 - rest;
                int posX = event.getResolution().getScaledWidth() / 2 + 10;
                int posY = event.getResolution().getScaledHeight() - 49;
                posY -= Configuration.barOffset;
                if (player.getAir() < 300) {
                    posY -= 10;
                }
                drawTexturedModalRect(posX, posY, 0, 0, 80, 9);
                drawTexturedModalRect(posX + consume, posY, consume, 9, rest, 9);
            }
        }
    }
}