package com.origins_eternity.ercore.compat.gravestone;

import net.minecraft.entity.player.EntityPlayer;

public class ExpHelper {
    public static int getTotalExperience(EntityPlayer player) {
        int level = player.experienceLevel;
        float progress = player.experience;

        int base;
        if (level <= 16) {
            base = level * level + 6 * level;
        } else if (level <= 31) {
            base = (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            base = (int) (4.5 * level * level - 162.5 * level + 2220);
        }

        base += (int) (progress * xpBarCap(level));
        return Math.max(base, player.experienceTotal);
    }

    private static int xpBarCap(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else if (level >= 15) {
            return 37 + (level - 15) * 5;
        } else {
            return 7 + level * 2;
        }
    }
}
