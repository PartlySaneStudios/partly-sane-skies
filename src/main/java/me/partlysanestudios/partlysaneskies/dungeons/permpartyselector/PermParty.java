package me.partlysanestudios.partlysaneskies.dungeons.permpartyselector;

import java.io.IOException;
import java.util.List;

import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.utils.Utils;

public class PermParty {
    public String name;
    public List<String> partyMembers;
    public boolean isFavourite;

    public PermParty(String partyName, List<String> partyMemberNames) {
        this.name = partyName;
        this.partyMembers = partyMemberNames;
    }

    public void addMember(String memberName) {
        this.partyMembers.add(memberName);
        try {
            PermPartyManager.save();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendClientMessage("Could not save Permanent Party Data.");
        }
    }

    public void removeMember(String memberName) {
        this.partyMembers.remove(memberName);
        try {
            PermPartyManager.save();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendClientMessage("Could not save Permanent Party Data.");
        }
    }

    public void partyAll() {
        Long timeDelay = 000l;
        for(String member : this.partyMembers) {
            final long finalTimeDelay = timeDelay.longValue();
            new Thread() {
                @Override
                public void run() {
                    try {

                        Thread.sleep(finalTimeDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Main.minecraft.thePlayer.sendChatMessage("/party invite " + member);
                }
            }.start();

            timeDelay += 500;
        }
    }

    public void setFavourite(boolean setFavourite) {
        this.isFavourite = setFavourite;
    }

    public String getMemberString() {
        String str = "";
        for(String name : partyMembers) {
            str += name + ", ";
        }
        if(str.endsWith(", "))
            str = new StringBuilder(str).replace(str.length()-2, str.length()-1, "").toString();
        return str;
    }
}
