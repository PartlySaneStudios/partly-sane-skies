//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer;

import java.util.ArrayList;
import java.util.List;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class Pos1Command implements ICommand{

    @Override
    public int compareTo(ICommand arg0) {
        return -1;
    }

    @Override
    public String getCommandName() {
        return "/pos1";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EndOfFarmNotifier.selectedPos1 = new int[] {sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ()};

        Utils.sendClientMessage("&7Set &bpositon 1&7 to &b(" + EndOfFarmNotifier.selectedPos1[0] + ", " + EndOfFarmNotifier.selectedPos1[1] + ", " + EndOfFarmNotifier.selectedPos1[2] + ")&7");
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
    
}
