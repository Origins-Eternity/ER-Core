package com.origins_eternity.ercore.content.gui;

import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.content.capability.Capabilities.ENDURANCE;

public class Overlay extends Gui {
    public static final ResourceLocation gui = new ResourceLocation(MOD_ID, "textures/gui/endurance.png");

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (!Loader.isModLoaded("classicbar")) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.player;
            if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
                GlStateManager.enableBlend();
                GlStateManager.pushMatrix();
                int posX = event.getResolution().getScaledWidth() / 2 + 82;
                int posY = event.getResolution().getScaledHeight() - GuiIngameForge.right_height;
                mc.getTextureManager().bindTexture(gui);
                drawTexture(player, posX, posY);
                GuiIngameForge.right_height += 10;
                GlStateManager.popMatrix();
                mc.getTextureManager().bindTexture(Gui.ICONS);
                GlStateManager.disableBlend();
            }
        }
    }

    private void drawTexture(EntityPlayerSP player, int posX, int posY) {
        IEndurance endurance = player.getCapability(ENDURANCE, null);
        float health = endurance.getHealth();
        int value = (int) endurance.getEndurance();
        for (int i = 0; (i * 2 < health && i < 10); i++) {
            drawTexturedModalRect(posX - i * 8, posY, 0, 0, 8, 9);
        }
        if (value > 20) {
            int extra = value - 20;
            drawTexturedModalRect(posX - 72, posY, 0, 9, 80, 9);
            drawTexturedModalRect(posX + 8 - extra * 4, posY, 80 - extra * 4, 18, extra * 4, 9);
        } else {
            drawTexturedModalRect(posX + 8 - value * 4, posY, 80 - value * 4, 9, value * 4, 9);
        }
    }
}