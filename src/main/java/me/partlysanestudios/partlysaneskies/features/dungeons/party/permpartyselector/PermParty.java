//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons.party.permpartyselector;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;

import java.io.IOException;
import java.util.List;

public class PermParty {
    public String name;
    public List<String> partyMembers;
    public boolean isFavorite = false;

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

    // Parties all the members of the perm party
    public void partyAll() {
        long timeDelay = 1000L;
        for (String member : this.partyMembers) {
            final long finalTimeDelay = timeDelay;
            new Thread(() -> {
                try {

                    Thread.sleep(finalTimeDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PartlySaneSkies.Companion.getMinecraft().addScheduledTask(() -> PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/party invite " + member));

            }).start();

            timeDelay += 500;
        }
    }

    // Sets the current permParty as the favorite
    public void setFavorite(boolean setFavorite) {
        this.isFavorite = setFavorite;
        save();
    }

    // Gets all the members of the perm party in a string
    public String getMemberString() {
        StringBuilder str = new StringBuilder();
        for (String name : partyMembers) {
            str.append(name).append(", ");
        }
        if (str.toString().endsWith(", "))
            str = new StringBuilder(new StringBuilder(str.toString()).replace(str.length() - 2, str.length() - 1, "").toString());
        return str.toString();
    }

    public void save() {
        try {
            PermPartyManager.save();
        } catch (IOException e) {
            e.printStackTrace();
            ChatUtils.INSTANCE.sendClientMessage("Could not save Permanent Party Data.");
        }
    }
}
