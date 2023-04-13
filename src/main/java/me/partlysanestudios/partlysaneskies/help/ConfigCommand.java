//
// Written by Su386.
// See LICENSE for copright and license notices.
//

package me.partlysanestudios.partlysaneskies.help;

import java.util.Arrays;
import java.util.List;

import gg.essential.elementa.components.Window;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class ConfigCommand implements ICommand {
    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "pssconfig";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pssconfig";
    }

    

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("pssconf", "pssc");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Utils.sendClientMessage("Opening config GUI...");
        
        Window.Companion.enqueueRenderOperation(() -> {
            PartlySaneSkies.config.openGui();
        });
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
