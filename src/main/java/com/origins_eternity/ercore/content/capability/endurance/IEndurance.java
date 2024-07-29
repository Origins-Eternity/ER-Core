package com.origins_eternity.ercore.content.capability.endurance;

import net.minecraftforge.fml.common.Optional;

public interface IEndurance {
    void setHealth(float health);

    float getHealth();

    @Optional.Method(modid = "firstaid")
    void setMaxHealth(float maxhealth);

    @Optional.Method(modid = "firstaid")
    float getMaxHealth();

    void setEndurance(float endurance);

    float getEndurance();

    void setCoolDown(int coolDown);

    int getCoolDown();

    float getExhaustion();

    float getSaturation();

    void setExhaustion(float value);

    void setSaturation(float value);

    void addExhaustion(float value);

    void addSaturation(float value);

    boolean isExhausted();

    void setExhausted(boolean exhausted);

    boolean isTired();

    void setTired(boolean tired);

    boolean isMove();

    void setMove(boolean move);

    void consumeEndurance(int value);

    void recoverEndurance(int value);

    void addCoolDown(int value);

    void removeCoolDown(int value);
}
