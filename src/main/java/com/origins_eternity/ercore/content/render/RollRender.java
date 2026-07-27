package com.origins_eternity.ercore.content.render;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RollRender {
    private static final int duration = 400;
    private static final Map<Integer, Long> animations = new HashMap<>();

    public static void startAnimation(int entityId) {
        animations.put(entityId, Minecraft.getSystemTime());
    }
    
    public static float getAnimationProgress(int entityId) {
        Long startTime = animations.get(entityId);
        if (startTime == null) return -1.0f;
        long elapsed = Minecraft.getSystemTime() - startTime;
        if (elapsed >= duration) {
            animations.remove(entityId);
            return 1.0f;
        }
        return (float) elapsed / (float) duration;
    }
}