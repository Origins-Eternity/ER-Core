package com.origins_eternity.ercore.message;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DoubleJump implements IMessage {
    public DoubleJump() {

    }

    @Override public void fromBytes(ByteBuf buf) {

    }

    @Override public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<DoubleJump, IMessage> {
        @Override
        public IMessage onMessage(DoubleJump msg, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                player.motionY = 0.42;
                player.fallDistance = 0;
                player.velocityChanged = true;

                endurance.setDoubleJumped(true);
                endurance.addExhaustion(1f);
            });
            return null;
        }
    }
}