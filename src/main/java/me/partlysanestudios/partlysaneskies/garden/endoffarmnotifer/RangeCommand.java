//
// Written by Su386.
// See LICENSE for copright and license notices.
//


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class RangeCommand implements ICommand{
    @Override
    public int compareTo(ICommand arg0) {
        return -1;
    }

    @Override
    public String getCommandName() {
        return "/farmnotifier";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("/farmnotif", "/fn");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 || (args.length >= 1 && args[0].equalsIgnoreCase("list"))) {
            Utils.sendClientMessage("To create a new farm notifer, run //pos1 at one end of your selection, then run //pos2 at the other end of your farm. Once the area has been selected, run //create.\n\n//farmnotifier command:\n//fn remove <index>: remove a given index from the list.\n//fn list: lists all of the farm notifiers and their indexes");
            EndOfFarmNotfier.listRanges();
            return;
        }

        if (args[1].equalsIgnoreCase("remove")) {
            if (args.length <= 2) {
                Utils.sendClientMessage("Error: Must provide an index to remove");
                return;
            }

            int i = Integer.parseInt(args[2]);
            Utils.sendClientMessage("Removing: " + EndOfFarmNotfier.ranges.get(i - 1).toString());
            EndOfFarmNotfier.ranges.remove(i - 1);
            return;
        }

        // Utils.sendClientMessage("To create a new farm notifer, run //pos1 at one end of your selection, then run //pos2 at the other end of your farm. Once the area has been selected, run //create.\n\n//farmnotifer command:\n//fn remove <index>: remove a given index from the list.\n//fn list: lists all of the farm notifiers and their indexes");
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
