package com.origins_eternity.ercore.content.capability.endurance;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

public class Endurance implements IEndurance {
    private double maxhealth = 20;

    private float health = 20;

    private float endurance = 20;

    private int coolDown = 0;

    private float exhaustion = 0;

    private float saturation = 0;

    @Override
    public void setHealth(float health) {
        if (health > 40) {
            this.health = 40;
        } else {
            this.health = health;
        }
    }

    @Override
    public float getHealth() {
        if (health > 40) {
            return 40;
        } else {
            return health;
        }
    }

    @Override
    @Optional.Method(modid = "firstaid")
    public void setMaxHealth(double maxhealth) {
        this.maxhealth = maxhealth;
    }

    @Override
    @Optional.Method(modid = "firstaid")
    public double getMaxHealth() {
        return maxhealth;
    }

    @Override
    public void setEndurance(float endurance) {
        this.endurance = endurance;
    }

    @Override
    public float getEndurance() {
        return endurance;
    }

    @Override
    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    @Override
    public int getCoolDown() {
        return coolDown;
    }

    @Override
    public float getExhaustion() {
        return exhaustion;
    }

    @Override
    public float getSaturation() {
        return saturation;
    }

    @Override
    public void setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
    }

    @Override
    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    @Override
    public void addExhaustion(float value) {
        if (endurance > 0) {
            exhaustion += value;
            while (exhaustion > 1) {
                consumeEndurance(1);
                exhaustion -= 1;
            }
        }
    }

    @Override
    public void addSaturation(float value) {
        if (endurance < health) {
            if (coolDown == 0) {
                saturation += value;
                while (saturation > 1) {
                    recoverEndurance(1);
                    saturation -= 1;
                }
            }
        }
    }

    @Override
    public void consumeEndurance(int value) {
        if (endurance > 0) {
            endurance -= value;
            if (endurance <= 0) {
                endurance = 0;
                addCoolDown(312);
            }
        }
    }

    @Override
    public void recoverEndurance(int value) {
        if (endurance < health) {
            if (coolDown == 0) {
                endurance += value;
                if (endurance > health) {
                    endurance = health;
                }
            }
        }
    }

    @Override
    public void addCoolDown(int value) {
        if (value > coolDown) {
            coolDown = value;
        }
    }

    @Override
    public void removeCoolDown(int value) {
        coolDown -= value;
        if (coolDown <= 0) {
            coolDown = 0;
        }
    }

    public static class EnduranceProvider implements ICapabilitySerializable<NBTBase> {
        final Capability<IEndurance> capability;
        final IEndurance instance;

        public EnduranceProvider(Capability<IEndurance> endurance) {
            this.capability = endurance;
            this.instance = capability.getDefaultInstance();
        }

        @Override
        public boolean hasCapability(Capability<?> endurance, EnumFacing facing) {
            return endurance == this.capability;
        }

        @Override
        public <T> T getCapability(Capability<T> endurance, EnumFacing facing) {
            if (endurance == this.capability) {
                return capability.cast(this.instance);
            }
            return null;
        }

        @Override
        public NBTBase serializeNBT() {
            return this.capability.writeNBT(this.instance, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            this.capability.readNBT(this.instance, null, nbt);
        }
    }

    public static class EnduranceStorage implements Capability.IStorage<IEndurance> {
        @Override
        public NBTBase writeNBT(Capability<IEndurance> capability, IEndurance instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setFloat("Health", instance.getHealth());
            compound.setFloat("Endurance", instance.getEndurance());
            compound.setInteger("CoolDown", instance.getCoolDown());
            compound.setFloat("Exhaustion", instance.getExhaustion());
            compound.setFloat("Saturation", instance.getSaturation());
            if (Loader.isModLoaded("firstaid")) {
                compound.setDouble("MaxHealth", instance.getMaxHealth());
            }
            return compound;
        }

        @Override
        public void readNBT(Capability<IEndurance> capability, IEndurance instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                instance.setHealth(compound.getFloat("Health"));
                instance.setEndurance(compound.getFloat("Endurance"));
                instance.setCoolDown(compound.getInteger("CoolDown"));
                instance.setExhaustion(compound.getFloat("Exhaustion"));
                instance.setSaturation(compound.getFloat("Saturation"));
                if (Loader.isModLoaded("firstaid")) {
                    instance.setMaxHealth(compound.getDouble("MaxHealth"));
                }
            }
        }
    }
}