package com.origins_eternity.ercore.content.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

import static com.origins_eternity.ercore.utils.proxy.ClientProxy.mc;

@SideOnly(Side.CLIENT)
public class RollRender {
    private static final int DURATION = 400;
    private static final Map<Integer, Long> animations = new HashMap<>();

    private static int prevThirdPersonView = 0;

    public static void startAnimation(int entityId) {
        animations.put(entityId, Minecraft.getSystemTime());
        EntityPlayerSP player = mc().player;
        if (player != null && player.getEntityId() == entityId) {
            prevThirdPersonView = mc().gameSettings.thirdPersonView;
            mc().gameSettings.thirdPersonView = 1;
        }
    }

    public static float getAnimationProgress(int entityId) {
        Long startTime = animations.get(entityId);
        if (startTime == null) return -1.0f;

        long elapsed = Minecraft.getSystemTime() - startTime;
        if (elapsed >= DURATION) {
            animations.remove(entityId);
            
            EntityPlayerSP player = mc().player;
            if (player != null && player.getEntityId() == entityId) {
                mc().gameSettings.thirdPersonView = prevThirdPersonView;
                prevThirdPersonView = 0;
            }
            return 1.0f;
        }
        return (float) elapsed / (float) DURATION;
    }
}