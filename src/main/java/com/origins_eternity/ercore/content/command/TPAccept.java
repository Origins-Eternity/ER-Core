package com.origins_eternity.ercore.content.command;

import net.minecraft.command.*;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;

import static com.origins_eternity.ercore.content.command.TPA.sendTo;
import static com.origins_eternity.ercore.content.command.TPAHere.sendHere;
import static com.origins_eternity.ercore.utils.Utils.playSound;

public class TPAccept extends CommandBase {
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
            if (receive.get(player.getUniqueID()) == null || receive.get(player.getUniqueID()).isEmpty()) throw new CommandException("commands.tpa.no_requests");
            String name = args.length == 1 ? args[0] : receive.get(player.getUniqueID()).get(receive.get(player.getUniqueID()).size() - 1);
            EntityPlayerMP target = getPlayer(server, sender, name);
            if (sendTo.get(target.getUniqueID()).hasKey(player.getName()) || sendHere.get(target.getUniqueID()).hasKey(player.getName())) {
                receive.get(player.getUniqueID()).removeIf(string -> string.equals(target.getName()));
                playSound(player, SoundEvents.UI_BUTTON_CLICK);
                TextComponentTranslation accepted = new TextComponentTranslation("commands.tpa.accepted", player.getName());
                target.sendMessage(accepted.setStyle(accepted.getStyle().setColor(TextFormatting.GREEN)));
                if (sendTo.get(target.getUniqueID()).hasKey(player.getName())) {
                    sendTo.get(target.getUniqueID()).removeTag(player.getName());
                    doTeleport(target, player, server);
                } else {
                    sendHere.get(target.getUniqueID()).removeTag(player.getName());
                    doTeleport(player, target, server);
                }
            } else {
                throw new CommandException("commands.tpa.no_player", target.getName());
            }
        } else {
            throw new WrongUsageException("commands.tpaccept.usage");
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayer;
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
    
    private static void doTeleport(EntityPlayerMP player, EntityPlayerMP target, MinecraftServer server) {
        EntityHorse horse = null;
        if (player.isRiding() && player.getRidingEntity() instanceof EntityHorse) {
            EntityHorse entity = (EntityHorse) player.getRidingEntity();
            horse = entity.getOwnerUniqueId().equals(player.getUniqueID()) && entity.isHorseSaddled() ? entity : null;
        }
        player.dismountRidingEntity();
        if (player.dimension != target.dimension) {
            server.getPlayerList().transferPlayerToDimension(player, target.dimension, new PlayerTeleporter(server.getWorld(target.dimension)));
            player.connection.setPlayerLocation(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
        } else if (horse != null) {
            horse.setPositionAndUpdate(target.posX, target.posY, target.posZ);
            player.connection.setPlayerLocation(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
            player.startRiding(horse);
        }
        playSound(target, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
    }
}