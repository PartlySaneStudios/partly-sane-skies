package me.partlysanestudios.partlysaneskies.partymanager;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.partymanager.PartyMember.PartyRank;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PartyManager {
    private static boolean isWaitingForMembers = false;
    private static boolean isMembersListed = false;
    private static String[] RANK_NAMES = {"[VIP]", "[VIP+]", "[MVP]", "[MVP+]", "[MVP++]", "[YOUTUBE]", "[MOJANG]", "[EVENTS]", "[MCP]", "[PIG]", "[PIG+]", "[PIG++]", "[PIG+++]", "[GM]", "[ADMIN]", "[OWNER]"};
    
    public static HashMap<String, PartyMember> playerCache = new HashMap<String, PartyMember> ();
    public static List<PartyMember> partyList = new ArrayList<PartyMember>();
    public PartyManager() {
        
    }

    public static void startPartyManager() {
        isWaitingForMembers = true;
        Main.minecraft.thePlayer.sendChatMessage("/party list");
        partyList.clear();
    }

    

    @SubscribeEvent
    public void getMembers(ClientChatReceivedEvent event) {
        if (!isWaitingForMembers) {
            return;
        };

        
        if (event.message.getUnformattedText().startsWith("Party Leader: ")) {
            event.setCanceled(true);
            isMembersListed = true;
            String text = event.message.getUnformattedText();
            text = text.replace("Party Leader: ", "");
            for(String rank : RANK_NAMES) {
                text = text.replace(rank, "");
            }

            text = text.replace(" ", "");

            for(String name : text.split("●")) {
                addPartyMember(name, PartyRank.LEADER);
            }

            
        }

        else if (event.message.getUnformattedText().startsWith("Party Moderators: ")) {
            event.setCanceled(true);
            isMembersListed = true;
            String text = event.message.getUnformattedText();
            text = text.replace("Party Moderators: ", "");
            for(String rank : RANK_NAMES) {
                text = text.replace(rank, "");
            }

            text = text.replace(" ", "");

            for(String name : text.split("●")) {
                addPartyMember(name, PartyRank.MODERATOR);
            }

            
        }

        else if (event.message.getUnformattedText().startsWith("Party Members: ")) {
            event.setCanceled(true);
            isMembersListed = true;
            String text = event.message.getUnformattedText();
            
            text = text.replace("Party Members: ", "");
            for(String rank : RANK_NAMES) {
                text = text.replace(rank, "");
            }

            text = text.replace(" ", "");

            for(String name : text.split("●")) {
                addPartyMember(name, PartyRank.MEMBER);
            }

            
        }

        else if (event.message.getUnformattedText().startsWith("Party Members (")) {
            event.setCanceled(true);
        }

        else if(isMembersListed && event.message.getUnformattedText().startsWith("-----------------------------------------------------")) {
            event.setCanceled(true);
            isMembersListed = false;
            isWaitingForMembers = false;
            openGui();
        }
        if(event.message.getUnformattedText().startsWith("-----------------------------------------------------")) {
            event.setCanceled(true);
        }
        else if(event.message.getUnformattedText().startsWith("You are not currently in a party.")) {
            event.setCanceled(true);
            Utils.sendClientMessage(Utils.colorCodes("&9&m-----------------------------------------------------\n&r&cError: Could not run Party Manager.\n&r&cYou are not currently in a party.\n&r&9&m-----------------------------------------------------"));
            isMembersListed = false;
            isWaitingForMembers = false;
        }
    }

    public static void openGui() {
        PartyManagerGui gui = new PartyManagerGui();
        Main.minecraft.displayGuiScreen(gui);

        getData();
    
        gui.populateGui(partyList);
    }

    public static void addPartyMember(String username, PartyRank partyRank) {
        if(playerCache.containsKey(username)) {
            Utils.visPrint(username + " is a cached member");
            PartyMember cachedMember = playerCache.get(username);
            partyList.add(cachedMember);
        }
        else {
            Utils.visPrint(username + " is not a cached member");
            PartyMember member = new PartyMember(username, partyRank);
            partyList.add(member);
        }
    }

    public static void getData() {
        for(PartyMember member : partyList) {
            if(member.isExpired()) {
                Utils.visPrint(member.username + " is expired");
                try {
                    member.getData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            playerCache.put(member.username, member);
        }
    }
}
