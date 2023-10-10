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

package me.partlysanestudios.partlysaneskies.chatalerts;

import java.util.Arrays;
import java.util.List;

import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class ChatAlertsCommand implements ICommand {

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "chatalerts";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("ca", "chatalet", "chal");
    }

    // Runs when command is run
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // If the user doesnt provide any arguments whatsoever print message
        if (args.length == 0) {
            Utils.sendClientMessage("&cIncorrect usage. Correct usage: /chatalerts add/remove/list");
            return;
        }

        // Looks at the first argument given
        switch (args[0]) {
            // If the user does /chatalerts list
            case "list":
                ChatAlertsManager.listAlerts();
                break;

            // If the user does /chatalerts add
            case "add":
                // Prints error message if no message alert is given
                if (args.length <= 1) {
                    Utils.sendClientMessage("&cIncorrect usage. Correct usage: /chatalerts add [alert]");
                    break;
                }

                // Adds each sepearate argument as a space
                String alert = "";
                for (int i = 1; i < args.length; i++) {
                    alert += args[i];
                    alert += " ";
                }

                // Removes any leading or trailing spaces
                alert = StringUtils.stripLeading(alert);
                alert = StringUtils.stripTrailing(alert);
                
                ChatAlertsManager.addAlert(alert);

                break;

            // If the user does /chatalerts remove
            case "remove":
                // If no number is given
                if (args.length <= 1) {
                    Utils.sendClientMessage("&cIncorrect usage. Correct usage: /chatalerts remove [number]");
                    break;
                }

                // Tries to parse the number given as a number
                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) { // If the number cannot be parsed, prints an error message
                    Utils.sendClientMessage("&c\"" + args[1] + "\" could not be read as a number. Correct Usage: /chatalerts remove [number]");
                    break;
                }

                // Removes the chat alert
                ChatAlertsManager.removeAlert(id);
                break;

            // If none of the above are given
            default:
                Utils.sendClientMessage("&cIncorrect usage. Correct usage: /chatalerts add/remove/list");
                break;
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

}
