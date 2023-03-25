/*
 * Partly Sane Skies: A Hypixel Skyblock QOL and Economy mod
 * Created by Su386#9878 (Su386yt) and FlagMaster#1516 (FlagHater), the Partly Sane Studios team
 * Copyright (C) ©️ Su386 and FlagMaster 2023
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


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
            Utils.sendClientMessage("To create a new farm notifer, run //pos1 at one end of your selection, then run //pos2 at the other end of your farm. Once the area has been selected, run //create.\n\n//farmnotifer command:\n//fn remove <index>: remove a given index from the list.\n//fn list: lists all of the farm notifiers and their indexes");
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
