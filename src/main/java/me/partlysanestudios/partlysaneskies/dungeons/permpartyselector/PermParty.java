package me.partlysanestudios.partlysaneskies.dungeons.permpartyselector;

import java.util.List;

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

    public void setFavourite(boolean setFavourite) {
        this.isFavourite = setFavourite;
    }
}
