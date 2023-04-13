//
// Written by Su386.
// See LICENSE for copright and license notices.
//


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer;

import java.util.ArrayList;
import java.util.List;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class CreateRangeCommand implements ICommand {
    
    @Override
    public int compareTo(ICommand arg0) {
        return -1;
    }

    @Override
    public String getCommandName() {
        return "/create";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String name = "";
        if (args.length >= 1) {
            name = args[0];
        }

        if (EndOfFarmNotfier.createNewRange(name) == null) {
            Utils.sendClientMessage("Unable to create a new farm notifer. Make sure both //pos1 and //pos2 have been selected.");
            return;
        } ;

        Utils.sendClientMessage("Created new Farm Notifier");
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<String>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
