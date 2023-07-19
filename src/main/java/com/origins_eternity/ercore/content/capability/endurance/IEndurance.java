package com.origins_eternity.ercore.content.capability.endurance;

public interface IEndurance {
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
