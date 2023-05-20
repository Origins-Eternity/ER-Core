package com.origins_eternal.ercore.handler;

import com.origins_eternal.ercore.message.TiredMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TiredHandler implements IMessageHandler<TiredMessage, IMessage> {
    @Override
    public IMessage onMessage(TiredMessage message, MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        if (!player.isPotionActive(MobEffects.SLOWNESS)) {
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 3, 2, false, false));
        }
        if (!player.isPotionActive(MobEffects.MINING_FATIGUE)) {
            player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 20 * 3, 3, false, false));
        }
        if (!player.isPotionActive(MobEffects.WEAKNESS)) {
            player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 20 * 3, 1, false, false));
        }
        return null;
    }
}