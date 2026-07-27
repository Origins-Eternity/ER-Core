package com.origins_eternity.ercore.event;

import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import com.origins_eternity.ercore.content.render.RollRender;
import com.origins_eternity.ercore.message.DoubleJump;
import com.origins_eternity.ercore.message.Roll;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.origins_eternity.ercore.ERCore.packetHandler;
import static com.origins_eternity.ercore.config.Configuration.Features;
import static com.origins_eternity.ercore.content.capability.Capabilities.ENDURANCE;
import static com.origins_eternity.ercore.content.render.Overlay.GUI;
import static com.origins_eternity.ercore.utils.Utils.getFoodValues;
import static com.origins_eternity.ercore.utils.proxy.ClientProxy.mc;
import static com.origins_eternity.ercore.utils.proxy.ClientProxy.rollKey;

@SideOnly(Side.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (Features.enableTooltip) {
            if (event.getItemStack().getItem() instanceof ItemFood && event.getEntityPlayer() == mc().player) {
                String[] values = getFoodValues(event.getItemStack(), event.getEntityPlayer());
                event.getToolTip().add("§f   " + values[0] + "    " + values[1]);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderTooltipPostText(RenderTooltipEvent.PostText event) {
        if (event.getStack().getItem() instanceof ItemFood && Features.enableTooltip) {
            int posX = event.getX();
            int posY = event.getY() + 1;
            String[] values = getFoodValues(event.getStack(), mc().player);
            for (String tip : event.getLines()) {
                if (tip.contains("§f   " + values[0] + "    " + values[1])) {
                    break;
                } else {
                    posY += event.getHeight() / event.getLines().size();
                }
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            Gui gui = mc().currentScreen;
            mc().getTextureManager().bindTexture(Gui.ICONS);
            if (gui != null) {
                gui.drawTexturedModalRect(posX, posY, 16, 27, 9, 9);
                gui.drawTexturedModalRect(posX, posY, 61, 27, 9, 9);
                posX += mc().fontRenderer.getStringWidth(values[0]) + 16;
                mc().getTextureManager().bindTexture(GUI);
                gui.drawTexturedModalRect(posX, posY, 9, 0, 9, 9);
                mc().getTextureManager().bindTexture(Gui.ICONS);
                gui.drawTexturedModalRect(posX, posY, 61, 27, 9, 9);
            }
            GlStateManager.disableBlend();
        }
    }

    private static long jumpTick;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer player = mc().player;
        if (player != null && mc().world != null) {
            if (player.isInWater() || !Features.enableEndurance) return;
            IEndurance endurance = player.getCapability(ENDURANCE, null);
            if (rollKey.isPressed()) {
                if (RollRender.getAnimationProgress(player.getEntityId()) >= 0.0f) return;
                if (player.onGround && endurance.getEndurance() >= 5) {
                    packetHandler.sendToServer(new Roll());
                    player.playSound(SoundEvents.ENTITY_BAT_TAKEOFF, 0.3F, 0.6F);
                }
            }
            if (mc().gameSettings.keyBindJump.isPressed()) {
                if (player.isCreative() || player.isSpectator()) return;
                long current = Minecraft.getSystemTime();
                if (current - jumpTick < 500 || endurance.getDoubleJumped()) return;
                if (!player.onGround && !player.isRiding() && endurance.getEndurance() >= 1) {
                    packetHandler.sendToServer(new DoubleJump());
                    player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.3F, 0.6F);
                    jumpTick = current;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        float progress = RollRender.getAnimationProgress(player.getEntityId());
        if (progress >= 0.0f) {
            float yaw = (float) Math.toRadians(player.rotationYaw);
            float axisX = MathHelper.cos(yaw);
            float axisZ = MathHelper.sin(yaw);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0, 0.5, 0.0);
            GlStateManager.rotate(progress * 360.0f, axisX, 0.0f, axisZ);
            GlStateManager.translate(0.0, -0.5, 0.0);
        }
    }


    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        int entityId = event.getEntityPlayer().getEntityId();
        float progress = RollRender.getAnimationProgress(entityId);
        if (progress >= 0.0f) {
            GlStateManager.popMatrix();
        }
    }
}