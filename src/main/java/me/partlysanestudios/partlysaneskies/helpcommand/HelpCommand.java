package me.partlysanestudios.partlysaneskies.helpcommand;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class HelpCommand implements ICommand {
    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "pss";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pss ";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("helpss", "help");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Help.printHelpMessage();
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
