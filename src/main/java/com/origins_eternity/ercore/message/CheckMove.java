package com.origins_eternity.ercore.message;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CheckMove implements IMessage {
    private boolean move;

    public CheckMove() {

    }

    public CheckMove(boolean move) {
        this.move = move;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.move = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.move);
    }

    public static class Handler implements IMessageHandler<CheckMove, IMessage> {
        @Override
        public IMessage onMessage(CheckMove message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
            endurance.setMove(message.move);
            return null;
        }
    }
}