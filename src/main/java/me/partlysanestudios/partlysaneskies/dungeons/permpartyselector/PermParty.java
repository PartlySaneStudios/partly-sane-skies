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

import java.io.IOException;
import java.util.List;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.Utils;

public class PermParty {
    public String name;
    public List<String> partyMembers;
    public boolean isFavourite;

    public PermParty(String partyName, List<String> partyMemberNames) {
        this.name = partyName;
        this.partyMembers = partyMemberNames;
    }

    // Adds a new member to the perm party
    public void addMember(String memberName) {
        this.partyMembers.add(memberName);
        save();
    }

    // Removes a member from the perm party
    public void removeMember(String memberName) {
        this.partyMembers.remove(memberName);
        save();
    }

    // Parties all of the members of the perm party
    public void partyAll() {
        Long timeDelay = 000l;
        for (String member : this.partyMembers) {
            final long finalTimeDelay = timeDelay.longValue();
            new Thread() {
                @Override
                public void run() {
                    try {

                        Thread.sleep(finalTimeDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PartlySaneSkies.minecraft.addScheduledTask(() -> {
                        PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party invite " + member);
                    });
                    
                }
            }.start();

            timeDelay += 500;
        }
    }

    // Sets the current permParty as the favourite
    public void setFavourite(boolean setFavourite) {
        this.isFavourite = setFavourite;
        save();
    }

    // Gets all of the members of the perm party in a string
    public String getMemberString() {
        String str = "";
        for (String name : partyMembers) {
            str += name + ", ";
        }
        if (str.endsWith(", "))
            str = new StringBuilder(str).replace(str.length() - 2, str.length() - 1, "").toString();
        return str;
    }

    public void save() {
        try {
            PermPartyManager.save();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendClientMessage("Could not save Permanent Party Data.");
        }
    }
}
