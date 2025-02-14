package com.origins_eternity.ercore.event;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;

import java.text.NumberFormat;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.content.gui.Overlay.GUI;
import static com.origins_eternity.ercore.utils.proxy.ClientProxy.mc;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ClientEvent {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemFood && event.getEntityPlayer() == mc().player) {
            String[] values = foodValues(event.getItemStack(), event.getEntityPlayer());
            event.getToolTip().add("§f   " + values[0] + "    " + values[1]);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderTooltipPostText(RenderTooltipEvent.PostText event) {
        if (event.getStack().getItem() instanceof ItemFood) {
            int posX = event.getX();
            int posY = event.getY() + 1;
            String[] values = foodValues(event.getStack(), mc().player);
            for (String tip : event.getLines()) {
                if (tip.contains("§f   " + values[0] + "    " + values[1])) {
                    break;
                } else {
                    posY += event.getHeight() / event.getLines().size();
                }
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            drawFoodTooltip(posX, posY, values[0]);
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

    private static String[] foodValues(ItemStack stack, EntityPlayer player) {
        ItemFood food = (ItemFood) stack.getItem();
        int hunger = food.getHealAmount(stack);
        float saturation = food.getSaturationModifier(stack);
        if (player != null) {
            if (Loader.isModLoaded("applecore")) {
                FoodValues foodValues = AppleCoreAPI.accessor.getFoodValuesForPlayer(stack, player);
                hunger = foodValues.hunger;
                saturation = foodValues.saturationModifier;
            }
            if (Loader.isModLoaded("pyrotech")) {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if (effect.getEffectName().equals("pyrotech.effect.comfort")) {
                        hunger += (int) (food.getHealAmount(stack) * ModuleTechBasicConfig.CAMPFIRE_EFFECTS.COMFORT_HUNGER_MODIFIER);
                        saturation += (float) (food.getSaturationModifier(stack) * ModuleTechBasicConfig.CAMPFIRE_EFFECTS.COMFORT_SATURATION_MODIFIER);
                    }
                }
            }
        }
        return new String[]{String.format("%d", hunger), NumberFormat.getPercentInstance().format(saturation)};
    }
}