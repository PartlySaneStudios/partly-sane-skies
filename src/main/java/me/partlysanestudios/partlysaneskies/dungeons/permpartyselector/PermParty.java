package me.partlysanestudios.partlysaneskies.dungeons.permpartyselector;

import java.util.List;

import me.partlysanestudios.partlysaneskies.Main;

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
    }

    public void partyAll() {
        Long timeDelay = 500l;
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
        }
    }

    public void setFavourite(boolean setFavourite) {
        this.isFavourite = setFavourite;
    }
}
