package com.origins_eternity.ercore.message;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.origins_eternity.ercore.ERCore.packetHandler;

public class Roll implements IMessage {
    public Roll() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<Roll, IMessage> {
        @Override
        public IMessage onMessage(Roll message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                float yaw = player.rotationYaw;
                float rad = (float) Math.toRadians(yaw);
                double dx = -MathHelper.sin(rad);
                double dz = MathHelper.cos(rad);

                player.motionX = dx * 0.8;
                player.motionZ = dz * 0.8;
                player.motionY = 0.15;
                player.velocityChanged = true;
                player.fallDistance = 0;

                IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                endurance.addExhaustion(3f);

                RollAnimation animation = new RollAnimation(player.getEntityId());
                packetHandler.sendToAllTracking(animation, player);
                packetHandler.sendTo(animation, player);
            });
            return null;
        }
    }
}