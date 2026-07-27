package com.origins_eternity.ercore.message;

import com.origins_eternity.ercore.content.render.RollRender;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.origins_eternity.ercore.utils.proxy.ClientProxy.mc;

public class RollAnimation implements IMessage {
    private int entityId;

    public RollAnimation() {}

    public RollAnimation(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static class Handler implements IMessageHandler<RollAnimation, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(RollAnimation message, MessageContext ctx) {
            mc().addScheduledTask(() -> {
                RollRender.startAnimation(message.entityId);
            });
            return null;
        }
    }
}