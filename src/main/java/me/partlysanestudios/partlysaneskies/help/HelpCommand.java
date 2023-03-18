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

package me.partlysanestudios.partlysaneskies.help;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import gg.essential.elementa.components.Window;
import me.partlysanestudios.partlysaneskies.Keybinds;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
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

    List<String> configAliases = Arrays.asList("conf", "c", "config");
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0 && configAliases.contains(args[0].toLowerCase())) {
            Utils.sendClientMessage("Opening config GUI...");
            Window.Companion.enqueueRenderOperation(() -> {
                PartlySaneSkies.minecraft.displayGuiScreen(PartlySaneSkies.config.gui());
            });
            return;
        }
        
        printHelpMessage();
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
    
    public static void printHelpMessage() {
        Utils.sendClientMessage(StringUtils.colorCodes(
                "&3&m-----------------------------------------------------&r" +
                "\n" +
                "\n&b&l&nWelcome to Partly Sane Skies!&r" +
                "\nPartly Sane Skies is a mod developed by Su386 and FlagMaster. This mod aims to be a quality of life mod for Hypixel Skyblock." +
                "\n" +
                "\n &6> Open the config: " +
                "\n    &6> &ePress " + Keyboard.getKeyName(Keybinds.configKey.getKeyCode()) + " or use /pssc" +
                "\n    &6> &eMost features are turned off by default so to use the mod, you will need to configure the settings" +
                "\n    &6> &eTo change the keybinding, press Esc, Options, Video Settings, Controls, and scroll down to \"Partly Sane Skies\"." +
                "\n" +
                "\n" +
                "\n &1> Join the discord" +
                "\n    &1> &9To recieve any future updates" +
                "\n    &1> &9/pssdiscord" +
                "\n" +
                "\n &5> Visit the GitHub" +
                "\n    &5> &dAll of the features wouldn't fit in this message, so check out the GitHub to see all of the features." +
                "\n&3&m-----------------------------------------------------&r" +
                "\nCommands:" +
                "\n > /pss" +
                "\n    > Brings up the help screen" +
                "\n" +
                "\n > /pm, /partymanager" +
                "\n    > Opens the Party Manager" +
                "\n" +
                "\n > /permparty, /permp, /pp" +
                "\n    > Allows you to auto party permanent parties" +
                "\n" +
                "\n > /skillup <username>, /su <username> " +
                "\n    > Recommends which skill you should upgrade next." +
                "\n" +
                "\n > /pssdisc, /pssdiscord" +
                "\n    > Sends a link to the discord" +
                "\n" +
                "\n /friendparty, /fp, /pf" +
                "\n    > Parties all of your online friends" +
                "\n" +
                "\n /chatalert" +
                "\n    > Allows you to recieve alerts when certain messages are sent in chat. See github for more information" +
                "\n&3&m-----------------------------------------------------&r"
        ), true);
    }
}
