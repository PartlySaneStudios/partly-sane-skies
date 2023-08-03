//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.system.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.ClientCommandHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager {
    public static HashMap<String, PSSCommand> commandList = new HashMap<>();

    public static ICommand registerCommand(PSSCommand pssCommand) {

        if (pssCommand.isRegistered()) {
            return null;
        }

        ICommand iCommand = new ICommand() {
            @Override
            public String getCommandName() {
                return pssCommand.getName();
            }

            @Override
            public String getCommandUsage(ICommandSender sender) {
                return pssCommand.getDescription();
            }

            @Override
            public List<String> getCommandAliases() {
                return pssCommand.getAliases();
            }

            @Override
            public void processCommand(ICommandSender sender, String[] args) throws CommandException {
                pssCommand.runRunnable(sender, args);
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

            @Override
            public int compareTo(@NotNull ICommand o) {
                return this.getCommandName().compareTo(o.getCommandName());
            }
        };
        pssCommand.setICommand(iCommand);
        ClientCommandHandler.instance.registerCommand(iCommand);
        commandList.put(pssCommand.getName(), pssCommand);

        return iCommand;
    }

    public PSSCommand getCommand(String commandName) {
        return commandList.get(commandName);
    }

}
