package com.origins_eternity.ercore.content.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Teleporter;

import javax.annotation.Nullable;
import java.util.*;

import static com.origins_eternity.ercore.content.command.TPACommand.send;
import static com.origins_eternity.ercore.utils.Utils.playSound;

public class TPAcceptCommand extends CommandBase {
    public static final Map<UUID, List<String>> receive = new HashMap<>();

    private static final String name = "tpaccept";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.tpaccept.usage";
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
            if (receive.get(player.getUniqueID()) == null) throw new CommandException("commands.tpa.no_requests");
            String name = args.length == 1 ? args[0] : receive.get(player.getUniqueID()).get(receive.get(player.getUniqueID()).size() - 1);
            EntityPlayerMP target = getPlayer(server, sender, name);
            if (send.get(target.getUniqueID()).hasKey(player.getName())) {
                send.get(target.getUniqueID()).removeTag(player.getName());
                receive.get(player.getUniqueID()).removeIf(string -> string.equals(target.getName()));
                playSound(player, SoundEvents.UI_BUTTON_CLICK);
                TextComponentTranslation accepted = new TextComponentTranslation("commands.tpa.accepted", player.getName());
                target.sendMessage(accepted.setStyle(accepted.getStyle().setColor(TextFormatting.GREEN)));
                if (target.dimension != player.dimension) {
                    target.changeDimension(player.dimension, new Teleporter(target.server.getWorld(player.dimension)));
                }
                target.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                playSound(target, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
            } else {
                throw new CommandException("commands.tpa.no_player", target.getName());
            }
        } else {
            throw new WrongUsageException("commands.tpaccept.usage");
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        try {
            return args.length == 1 ? getListOfStringsMatchingLastWord(args, receive.get(getCommandSenderAsPlayer(sender).getUniqueID())) : Collections.emptyList();
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}