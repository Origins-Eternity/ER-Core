package com.origins_eternity.ercore.content.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.Nullable;
import java.util.*;

import static com.origins_eternity.ercore.content.command.TPAcceptCommand.receive;
import static com.origins_eternity.ercore.utils.Utils.playSound;

public class TPACommand extends CommandBase {
    public static final Map<UUID, NBTTagCompound> send = new HashMap<>();

    private static final String name = "tpa";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.tpa.usage";
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add(name);
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            EntityPlayerMP target = getPlayer(server, sender, args[0]);
            if (player != target) {
                send.computeIfAbsent(player.getUniqueID(), k -> new NBTTagCompound()).setLong(target.getName(), MinecraftServer.getCurrentTimeMillis());
                receive.computeIfAbsent(target.getUniqueID(), k -> new ArrayList<>()).add(player.getName());
                playSound(player, SoundEvents.ENTITY_ARROW_SHOOT);
                target.sendMessage(getMessage(player.getName()));
                playSound(target, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
            } else {
                throw new CommandException("commands.tpa.self");
            }
        } else {
            throw new WrongUsageException("commands.tpa.usage");
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

    private ITextComponent getMessage(String name) {
        ITextComponent massage = new TextComponentTranslation("commands.tpa.request", name);
        TextComponentTranslation accept = new TextComponentTranslation("commands.tpa.accept");
        accept.getStyle().setColor(TextFormatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + name));
        TextComponentTranslation deny = new TextComponentTranslation("commands.tpa.deny");
        deny.getStyle().setColor(TextFormatting.RED).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + name));
        return massage.appendSibling(accept).appendSibling(deny);
    }
}