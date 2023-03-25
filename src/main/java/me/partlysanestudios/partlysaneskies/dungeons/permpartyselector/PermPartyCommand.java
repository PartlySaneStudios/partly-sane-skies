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

package me.partlysanestudios.partlysaneskies.dungeons.permpartyselector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class PermPartyCommand implements ICommand {

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "permparty";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/permparty [<partyid>/add/remove/list/delete/new/fav]";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("pp", "permp");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            Utils.sendClientMessage(
                    StringUtils.colorCodes(
                            "&3/pp <partyid>\n&7Parties everyone in the perm party." +
                                    "\n&3/pp add <partyid> <playerusername>\n&7Adds a player to the perm party." +
                                    "\n&3/pp list {partyid}\n&7Lists all of the members in a given party. If no party is specified, lists all perm parties."
                                    +
                                    "\n&3/pp delete <partyid>\n&7Deletes a perm party. (Note: There is no way to undo this action)."
                                    +
                                    "\n&3/pp new <partyid> {partymembers}\n&7Creates a new perm party." +
                                    "\n&3/pp fav {partyid}\n&7Sets party as favourite. If no party is specified, parties everyone in the favourite perm party."));
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 3) {
                if (PermPartyManager.permPartyMap.containsKey(args[1])) {
                    PermParty party = PermPartyManager.permPartyMap.get(args[1]);
                    party.addMember(args[2]);
                    PermPartyManager.permPartyMap.put(party.name, party);
                    Utils.sendClientMessage(StringUtils.colorCodes("Added player " + args[2] + " to party " + args[1] + "."));
                } else {
                    Utils.sendClientMessage(StringUtils.colorCodes("&cNo party was found with the ID " + args[1]
                            + ".\n&cCorrect usage: /pp add <partyid> <playerusername>\n&7Adds a player to the perm party."));
                }
            } else {
                Utils.sendClientMessage(StringUtils.colorCodes(
                        "&cCorrect usage: /pp add <partyid> <playerusername>\n&7Adds a player to the perm party."));
            }
        }

        // Remove arg
        else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 3) {
                if (PermPartyManager.permPartyMap.containsKey(args[1])) {

                    PermParty party = PermPartyManager.permPartyMap.get(args[1]);
                    if (party.partyMembers.contains(args[2])) {
                        party.removeMember(args[2]);
                        PermPartyManager.permPartyMap.put(party.name, party);
                        Utils.sendClientMessage("Removed member " + args[2] + " from party " + args[1] + ".");
                    } else {
                        Utils.sendClientMessage(StringUtils.colorCodes("&cNo player was found with the name " + args[2]
                                + ".\n&cCorrect usage: /pp remove <partyid> <playerusername>\n&7Removes a player from the perm party."));
                    }

                } else {
                    Utils.sendClientMessage(StringUtils.colorCodes("&cNo party was found with the ID " + args[1]
                            + ".\n&cCorrect usage: /pp remove <partyid> <playerusername>\n&7Removes a player from the perm party."));
                }
            } else {
                Utils.sendClientMessage(StringUtils.colorCodes(
                        "&cCorrect usage: /pp remove <partyid> <playerusername>\n&7Removes a player from the perm party."));
            }
        }

        // List arg
        else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 1) {
                for (PermParty party : PermPartyManager.permPartyMap.values()) {
                    Utils.sendClientMessage(party.name + " | Members: " + party.getMemberString());
                }
            } else {
                if (PermPartyManager.permPartyMap.containsKey(args[1])) {
                    PermParty party = PermPartyManager.permPartyMap.get(args[0]);
                    Utils.sendClientMessage(party.name + " | Members: " + party.getMemberString());
                } else {
                    Utils.sendClientMessage(StringUtils.colorCodes("&cNo party was found with the ID " + args[1]
                            + ".\n&cCorrect usage: /pp list {partyid}\n&7Lists all of the members in a given party. If no party is specified, lists all parties."));
                }
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 2) {
                if (PermPartyManager.permPartyMap.containsKey(args[1])) {
                    PermPartyManager.deleteParty(args[1]);
                    Utils.sendClientMessage("Deleted party " + args[1] + ".");
                } else {
                    Utils.sendClientMessage(StringUtils.colorCodes("&cNo party was found with the ID " + args[1]
                            + ".\n&cCorrect usage: /pp delete <partyid>\n&7Deletes a perm party. (Note: There is no way to undo this action)."));
                }
            } else {
                Utils.sendClientMessage(StringUtils.colorCodes(
                        "&cCorrect usage: /pp delete <partyid>\n&7Deletes a perm party. (Note: There is no way to undo this action)."));
            }
        } else if (args[0].equalsIgnoreCase("new")) {
            if (args.length >= 2) {
                List<String> partyMembers = new ArrayList<String>();
                for (int i = 2; i < args.length; i++) {
                    partyMembers.add(args[i]);
                }
                PermPartyManager.addParty(args[1], partyMembers);

                Utils.sendClientMessage("Created party " + args[1] + ".");
            } else {
                Utils.sendClientMessage(StringUtils
                        .colorCodes("&cCorrect usage: /pp new <partyid> {partymembers}\n&7Creates a new perm party."));
            }
        } else if (args[0].equalsIgnoreCase("fav")) {
            if (args.length == 2) {
                if (PermPartyManager.permPartyMap.containsKey(args[1])) {
                    PermPartyManager.favouriteParty(args[1]);
                    Utils.sendClientMessage("Set " + args[1] + " to your favorite.");
                } else {
                    Utils.sendClientMessage(StringUtils.colorCodes("&cNo party was found with the ID " + args[1]
                            + ".\n&cCorrect usage: /pp fav {partyid}\n&7Sets party as favourite. If no party is specified, parties everyone in the favourite perm party."));
                }
            } else {
                if (PermPartyManager.favouriteParty != null) {
                    PermParty party = PermPartyManager.favouriteParty;
                    party.partyAll();
                } else {
                    Utils.sendClientMessage(StringUtils.colorCodes(
                            "&cCorrect usage: /pp fav {partyid}\n&7Sets party as favourite. If no party is specified, parties everyone in the favourite perm party."));
                }

            }
        } else {
            if (PermPartyManager.permPartyMap.containsKey(args[0])) {
                PermParty party = PermPartyManager.permPartyMap.get(args[0]);
                party.partyAll();
            } else {
                Utils.sendClientMessage(
                        StringUtils.colorCodes("&cCorrect usage: /pp <partyid>\n&7Parties everyone in a party."));
            }
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
