package com.origins_eternity.ercore.content.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.origins_eternity.ercore.content.command.TPA.sendTo;
import static com.origins_eternity.ercore.content.command.TPAHere.sendHere;
import static com.origins_eternity.ercore.content.command.TPAccept.receive;
import static com.origins_eternity.ercore.utils.Utils.playSound;

public class TPADeny extends CommandBase {
    private static final String name = "tpadeny";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender sendToer) {
        return "commands.tpadeny.usage";
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add(name);
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            if (receive.get(player.getUniqueID()) == null || receive.get(player.getUniqueID()).isEmpty()) throw new CommandException("commands.tpa.no_requests");
            String name = args.length == 1 ? args[0] : receive.get(player.getUniqueID()).get(receive.get(player.getUniqueID()).size() - 1);
            EntityPlayerMP target = getPlayer(server, sender, name);
            if (sendTo.get(target.getUniqueID()).hasKey(player.getName()) || sendHere.get(target.getUniqueID()).hasKey(player.getName())) {
                receive.get(player.getUniqueID()).removeIf(string -> string.equals(target.getName()));
                playSound(player, SoundEvents.UI_BUTTON_CLICK);
                TextComponentTranslation denied = new TextComponentTranslation("commands.tpa.denied", player.getName());
                target.sendMessage(denied.setStyle(denied.getStyle().setColor(TextFormatting.RED)));
                playSound(target, SoundEvents.ENTITY_ITEM_PICKUP);
                NBTTagCompound compound = sendTo.get(target.getUniqueID()).hasKey(player.getName()) ? sendTo.get(target.getUniqueID()) : sendHere.get(target.getUniqueID());
                compound.removeTag(player.getName());
            } else {
                throw new CommandException("commands.tpa.no_player", target.getName());
            }
        } else {
            throw new WrongUsageException("commands.tpadeny.usage");
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sendToer, String[] args, @Nullable BlockPos targetPos) {
        try {
            return args.length == 1 ? getListOfStringsMatchingLastWord(args, receive.get(getCommandSenderAsPlayer(sendToer).getUniqueID())) : Collections.emptyList();
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}