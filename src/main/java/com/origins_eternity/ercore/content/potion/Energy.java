package com.origins_eternity.ercore.content.potion;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.origins_eternity.ercore.content.render.Overlay.indicator;

public class Energy extends Potion {
    public Energy(String name) {
        super(false, 0xFF52ef8e);
        setPotionName("effect." + name);
        setRegistryName(name);
        setBeneficial();

        Potions.POTIONS.add(this);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            if (!endurance.getEnergetic()) {
                endurance.setEnergetic(true);
            }
            endurance.recoverEndurance(1);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int interval = 20 >> amplifier;
        if (interval > 0) {
            return duration % interval == 0;
        } else {
            return true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        mc.getTextureManager().bindTexture(indicator);
        Gui.drawModalRectWithCustomSizedTexture(x + 7, y + 8, 0, 28, 16, 16, 256, 256);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(indicator);
        Gui.drawModalRectWithCustomSizedTexture(x + 4, y + 4, 0, 28, 16, 16, 256, 256);
    }
}