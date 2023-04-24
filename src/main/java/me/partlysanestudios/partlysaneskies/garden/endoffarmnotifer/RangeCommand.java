//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer;

import java.io.IOException;
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

            Utils.sendClientMessage("&7To create a new farm notifer, run &b//pos1&7 at one end of your selection, then run &b//pos2&7 at the other end of your farm. Once the area has been selected, run &b//create&7.\n\n&b//farmnotifier&7 command:\n&b//fn remove <index>:&7 remove a given index from the list.\n&b//fn list:&7 lists all of the farm notifiers and their indexes");

            EndOfFarmNotfier.listRanges();
            return;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length <= 1) {
                Utils.sendClientMessage("&cError: Must provide an index to remove");
                return;
            }

            int i = 0;
            try{
                i = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                Utils.sendClientMessage("&cPlease enter a valid number index and try again.");
                return;
            }

            if (i > EndOfFarmNotfier.ranges.size()) {
                Utils.sendClientMessage("&cPlease select a valid index and try again.");
                return;
            }
            Utils.sendClientMessage("&aRemoving: &b" + EndOfFarmNotfier.ranges.get(i - 1).toString());
            EndOfFarmNotfier.ranges.remove(i - 1);
            try {
                EndOfFarmNotfier.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
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
