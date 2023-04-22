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

public class Pos2Command implements ICommand {
    @Override
    public int compareTo(ICommand arg0) {
        return -1;
    }

    @Override
    public String getCommandName() {
        return "/pos2";
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
        EndOfFarmNotfier.selectedPos2 = new int[] {sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ()};

        Utils.sendClientMessage("&7Set &bpositon 2&7 to &b(" + EndOfFarmNotfier.selectedPos2[0] + ", " + EndOfFarmNotfier.selectedPos2[1] + ", " + EndOfFarmNotfier.selectedPos2[2] + ")&7");
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
