package com.origins_eternity.ercore.event;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.text.NumberFormat;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.content.gui.Overlay.GUI;
import static com.origins_eternity.ercore.utils.proxy.ClientProxy.mc;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Side.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood) event.getItemStack().getItem();
            String hunger = String.format("%d", food.getHealAmount(event.getItemStack()));
            String saturation = NumberFormat.getPercentInstance().format(food.getSaturationModifier(event.getItemStack()));
            event.getToolTip().add("§f   " + hunger + "    " + saturation);
        }
    }

    @SubscribeEvent
    public static void onRenderTooltipPostText(RenderTooltipEvent.PostText event) {
        if (event.getStack().getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood) event.getStack().getItem();
            String hunger = String.format("%d", food.getHealAmount(event.getStack()));
            String saturation = NumberFormat.getPercentInstance().format(food.getSaturationModifier(event.getStack()));
            int posX = event.getX();
            int posY = event.getY() + 1;
            for (String tip : event.getLines()) {
                if (tip.contains("§f   " + hunger + "    " + saturation)) {
                    break;
                } else {
                    posY += event.getHeight() / event.getLines().size();
                }
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            drawFoodTooltip(posX, posY, hunger);
            GlStateManager.disableBlend();
        }
    }

    private static void drawFoodTooltip(int posX, int posY, String hunger) {
        GuiScreen gui = mc().currentScreen;
        if (gui != null) {
            mc().getTextureManager().bindTexture(Gui.ICONS);
            gui.drawTexturedModalRect(posX, posY, 16, 27, 9, 9);
            gui.drawTexturedModalRect(posX, posY, 61, 27, 9, 9);
            posX += mc().fontRenderer.getStringWidth(hunger) + 16;
            mc().getTextureManager().bindTexture(GUI);
            gui.drawTexturedModalRect(posX, posY, 9, 0, 9, 9);
            mc().getTextureManager().bindTexture(Gui.ICONS);
            gui.drawTexturedModalRect(posX, posY, 61, 27, 9, 9);
        }
    }
}