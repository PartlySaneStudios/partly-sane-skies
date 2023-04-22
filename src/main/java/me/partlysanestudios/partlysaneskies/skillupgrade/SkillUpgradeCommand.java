//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.skillupgrade;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SkillUpgradeCommand implements ICommand {
    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "skillup";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/skillup [username]";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("skillup", "skillu", "su");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Utils.sendClientMessage("Loading...");

        new Thread() {
            @Override
            public void run() {
                HashMap<String, Double> map;
                if (args.length > 0) {
                    try {
                        map = SkillUpgradeRecommendation.getRecomendedSkills(args[0]);
                    } catch (IOException e) {
                        Utils.sendClientMessage(StringUtils.colorCodes("Error getting data for " + args[0]
                                + ". Maybe the player is nicked or there is an invalid API key. Try running /api new."));
                        return;
                    }
                } else {
                    try {
                        map = SkillUpgradeRecommendation.getRecomendedSkills(PartlySaneSkies.minecraft.thePlayer.getName());
                    } catch (IOException e) {
                        Utils.sendClientMessage(StringUtils.colorCodes("Error getting data for "
                                + PartlySaneSkies.minecraft.thePlayer.getName()
                                + ". Maybe the player is nicked or there is an invalid API key. Try running /api new."));
                        return;
                    }
                }
                
                PartlySaneSkies.minecraft.addScheduledTask(() -> {
                    SkillUpgradeRecommendation.printMessage(map);
                });
                
            }
        }.start();

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
