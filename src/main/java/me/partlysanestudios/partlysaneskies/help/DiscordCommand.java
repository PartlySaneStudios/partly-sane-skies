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

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class DiscordCommand implements ICommand {
    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "pssdiscord";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pssdiscord ";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("pssdisc", "psdisc");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // Creates a new message with the correct text
        IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + StringUtils.colorCodes("&9Join the discord: https://discord.gg/v4PU3WeH7z"));
        // Sets the text to be clickable with a link
        message.getChatStyle().setChatClickEvent(new ClickEvent(Action.OPEN_URL, "https://discord.gg/v4PU3WeH7z"));
        // Prints message
        PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
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
