package com.origins_eternity.ercore.content.capability.endurance;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class Endurance implements IEndurance {
    private int endurance = 20;

    private int coolDown = 0;

    private float exhaustion = 0;

    private float saturation = 0;

    private boolean exhausted = false;

    private boolean tired = false;

    private boolean move = false;

    @Override
    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    @Override
    public int getEndurance() {
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
        exhaustion += value;
        while (exhaustion > 1) {
            consumeEndurance(1);
            exhaustion -= 1;
        }
    }

    @Override
    public void addSaturation(float value) {
        saturation += value;
        while (saturation > 1) {
            recoverEndurance(1);
            saturation -= 1;
        }
    }

    @Override
    public boolean isExhausted() {
        return exhausted;
    }

    @Override
    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
    }

    @Override
    public boolean isTired() {
        return tired;
    }

    @Override
    public void setTired(boolean tired) {
        this.tired = tired;
    }

    @Override
    public boolean isMove() {
        return move;
    }

    @Override
    public void setMove(boolean move) {
        this.move = move;
    }

    @Override
    public void consumeEndurance(int value) {
        endurance -= value;
        if (endurance <= 5) {
            if (!tired) {
                setTired(true);
            }
            if (endurance <= 0) {
                endurance = 0;
                if (!exhausted) {
                    setExhausted(true);
                }
            }
        }
    }

    @Override
    public void recoverEndurance(int value) {
        endurance += value;
        if (endurance >= 5) {
            if (tired) {
                setTired(false);
            }
            if (endurance >= 20) {
                endurance = 20;
                if (exhausted) {
                    setExhausted(false);
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
            compound.setInteger("Endurance", instance.getEndurance());
            compound.setInteger("CoolDown", instance.getCoolDown());
            compound.setFloat("Exhaustion", instance.getExhaustion());
            compound.setFloat("Saturation", instance.getSaturation());
            compound.setBoolean("Exhausted", instance.isExhausted());
            compound.setBoolean("Tired", instance.isTired());
            compound.setBoolean("Move", instance.isMove());
            return compound;
        }

        @Override
        public void readNBT(Capability<IEndurance> capability, IEndurance instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                instance.setEndurance(compound.getInteger("Endurance"));
                instance.setCoolDown(compound.getInteger("CoolDown"));
                instance.setExhaustion(compound.getFloat("Exhaustion"));
                instance.setSaturation(compound.getFloat("Saturation"));
                instance.setExhausted(compound.getBoolean("Exhausted"));
                instance.setTired(compound.getBoolean("Tired"));
                instance.setMove(compound.getBoolean("Move"));
            }
        }
    }
}