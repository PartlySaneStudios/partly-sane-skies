package me.partlysanestudios.partlysaneskies.general.chatalerts;

import java.util.Arrays;
import java.util.List;

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
        // TODO Auto-generated method stub
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
                alert = Utils.stripLeading(alert);
                alert = Utils.stripTrailing(alert);
                
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
