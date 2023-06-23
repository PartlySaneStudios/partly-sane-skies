package me.partlysanestudios.partlysaneskies.economy.minioncalculator;

import gg.essential.elementa.ElementaVersion;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinionCalculatorCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "minioncalculator";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Opens a GUI showing the best minions to use";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("minioncalc", "bestminion", "mc", "bm", "bestminioncalc");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        PartlySaneSkies.minecraft.displayGuiScreen(new ProfitMinionCalculator(ElementaVersion.V2));
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
        return 0;
    }
}
