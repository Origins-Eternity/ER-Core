package com.origins_eternity.ercore.content.capability.endurance;

import net.minecraftforge.fml.common.Optional;

public interface IEndurance {
    void setHealth(float health);

    float getHealth();

    @Optional.Method(modid = "firstaid")
    void setMaxHealth(double maxhealth);

    @Optional.Method(modid = "firstaid")
    double getMaxHealth();

    void setEndurance(int endurance);

    int getEndurance();

    void setCoolDown(int coolDown);

    int getCoolDown();

    float getExhaustion();

    float getSaturation();

    void setExhaustion(float value);

    void setSaturation(float value);

    void addExhaustion(float value);

    void addSaturation(float value);

    void consumeEndurance(int value);

    void recoverEndurance(int value);

    void addCoolDown(int value);

    void removeCoolDown(int value);
}