package com.origins_eternity.ercore.content.capability.endurance;

public interface IEndurance {
    void setMaxEndurance(int maxEndurance);

    int getMaxEndurance();

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

    boolean getDoubleJumped();

    void setDoubleJumped(boolean doubleJumped);

    boolean getEnergetic();

    void setEnergetic(boolean energetic);
}