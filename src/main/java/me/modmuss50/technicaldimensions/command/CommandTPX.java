package me.modmuss50.technicaldimensions.command;

import me.modmuss50.technicaldimensions.misc.TeleportationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by Mark on 07/08/2016.
 */
public class CommandTPX extends CommandBase {
    @Override
    public String getCommandName() {
        return "tpx";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/tpx <dimID>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new TextComponentString("Please provide a dim id"));
        } else {
            if (sender instanceof Entity) {
                TeleportationUtils.teleportEntity((Entity) sender, 0, 0, 0, 0, 0, parseInt(args[0]), true);
            } else {
                sender.addChatMessage(new TextComponentString("Does not work from console"));
            }

        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }


}
