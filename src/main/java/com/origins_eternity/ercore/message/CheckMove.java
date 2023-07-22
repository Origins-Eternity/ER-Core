package com.origins_eternity.ercore.message;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CheckMove implements IMessage {
    private boolean move;

    private String uuid;

    public CheckMove() {

    }

    public CheckMove(boolean move, String uuid) {
        this.move = move;
        this.uuid = uuid;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.move = buf.readBoolean();
        this.uuid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.move);
        ByteBufUtils.writeUTF8String(buf, uuid);
    }

    public static class Handler implements IMessageHandler<CheckMove, IMessage> {
        @Override
        public IMessage onMessage(CheckMove message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                EntityPlayer player = ctx.getServerHandler().player;
                if (player != null) {
                    if (player.getCachedUniqueIdString().equals(message.uuid)) {
                        player.getServer().addScheduledTask(() -> {
                            IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
                            endurance.setMove(message.move);
                        });
                    }
                }
            }
            return null;
        }
    }
}