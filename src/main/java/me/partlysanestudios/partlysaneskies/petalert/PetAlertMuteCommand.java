package me.partlysanestudios.partlysaneskies.petalert;

import java.util.Arrays;
import java.util.List;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class PetAlertMuteCommand implements ICommand {

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "mutepetalert";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Utils.sendClientMessage("&dPet alert has been muted for " +  PartlySaneSkies.config.petAlertMuteTime + " minutes.");
        PetAlert.lastMuteTime = Minecraft.getSystemTime();
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
