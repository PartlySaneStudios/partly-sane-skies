//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.chatalerts;

import java.util.Arrays;
import java.util.List;

import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

public class ChatAlertsCommand implements ICommand {

    @Override
    public int compareTo(@NotNull ICommand o) {
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
        return Arrays.asList("ca", "chatalert", "chal");
    }

    // Runs when command is run
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // If the user doesn't provide any arguments whatsoever print message
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
                if (args.length == 1) {
                    Utils.sendClientMessage("&cIncorrect usage. Correct usage: /chatalerts add [alert]");
                    break;
                }

                // Adds each argument as a space
                StringBuilder alert = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    alert.append(args[i]);
                    alert.append(" ");
                }

                // Removes any leading or trailing spaces
                alert = new StringBuilder(StringUtils.stripLeading(alert.toString()));
                alert = new StringBuilder(StringUtils.stripTrailing(alert.toString()));
                
                ChatAlertsManager.addAlert(alert.toString());

                break;

            // If the user does /chatalerts remove
            case "remove":
                // If no number is given
                if (args.length == 1) {
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
