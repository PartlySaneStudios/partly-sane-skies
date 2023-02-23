package me.partlysanestudios.partlysaneskies.dungeons.partymanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyMember.PartyRank;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PartyManager {
    private static boolean isWaitingForMembers = false;
    private static boolean isMembersListed = false;
    public static HashMap<String, PartyMember> playerCache = new HashMap<String, PartyMember>();
    private static List<PartyMember> partyList = new ArrayList<PartyMember>();

    public PartyManager() {

    }

    public static void startPartyManager() {
        // Tells the program to start waiting for party members to be listed
        isWaitingForMembers = true;

        // If config option, kicks all offline partymembers
        if (Main.config.autoKickOfflinePartyManager) {
            kickOffline();
        }

        // Creates a new thread that is separate from the grame thread
        new Thread() {
            @Override
            public void run() {
                // Sleeps to avoid conflict with kicking offline
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Starts the party manager
                Main.minecraft.thePlayer.sendChatMessage("/party list");
                partyList.clear();
            }
            
        }.start();;
        
    }

    // Upon chat message recieve, it will check to see if it is the party list
    @SubscribeEvent
    public void getMembers(ClientChatReceivedEvent event) {
        // If its not waiting for party members it returns
        if (!isWaitingForMembers) {
            return;
        }

        // If the message says "leader"
        if (event.message.getUnformattedText().startsWith("Party Leader: ")) {
            // Hides the message
            event.setCanceled(true);
            
            // Gets the message contents
            String text = event.message.getUnformattedText();
            // Removes the header
            text = text.replace("Party Leader: ", "");
            // Processes the list
            processList(text, PartyRank.MEMBER);
        }

        // If the message says "Party Moderators: "
        else if (event.message.getUnformattedText().startsWith("Party Moderators: ")) {
            // Hides the message
            event.setCanceled(true);
            String text = event.message.getUnformattedText();
            
            // Removes the header
            text = text.replace("Party Moderators: ", "");
            processList(text, PartyRank.MODERATOR);
        }

        // If the message says "Party Members: "
        else if (event.message.getUnformattedText().startsWith("Party Members: ")) {
            // Hides the message
            event.setCanceled(true);
            
            // Gets the message contents
            String text = event.message.getUnformattedText();
            // Removes the header
            text = text.replace("Party Members: ", "");
            // Processes the list
            processList(text, PartyRank.MEMBER);
        }

        // Hides the beginning of the "Party Members" list
        else if (event.message.getUnformattedText().startsWith("Party Members (")) {
            event.setCanceled(true);
        }

        // Detects the closing line ----- when all of the members have been listed and the bar appears, its the end of the message
        else if (isMembersListed && event.message.getUnformattedText().startsWith("-----------------------------------------------------")) {
            // Hides the message
            event.setCanceled(true);
            // Resets
            isMembersListed = false;
            isWaitingForMembers = false;
            // Opens the nessage
            openGui();
        }

        // Hides the ---------------------
        else if (event.message.getUnformattedText().startsWith("-----------------------------------------------------")) {
            event.setCanceled(true);
        }

        // If the player is not in the party
        else if (event.message.getUnformattedText().startsWith("You are not currently in a party.")) {
            // Hides message
            event.setCanceled(true);
            // Sends an error messsage
            Utils.sendClientMessage(Utils.colorCodes("&9&m-----------------------------------------------------\n "+
                    "&r&cError: Could not run Party Manager." +
                    "\n&r&cYou are not currently in a party."
            ));
            // Resets
            isMembersListed = false;
            isWaitingForMembers = false;
        }
    }

    private static void processList(String str, PartyRank rank) {
        // Members have started being listed
        isMembersListed = true;

        // Removes the rank from the name if it is contained
        for (String playerRank : Main.RANK_NAMES) {
            str = str.replace(playerRank, "");
        }

        // Removes all whitespace
        str = str.replace(" ", "");

        // Splits the list by the status indicator located before every name
        for (String name : str.split("●")) {
            addPartyMember(name, rank);
        }
    }

    // Opens the party manager GUI
    public static void openGui() {
        PartyManagerGui gui = new PartyManagerGui();
        Main.minecraft.displayGuiScreen(gui);

        // Populates the GUI with the party list
        gui.populateGui(partyList);
    }
     
    // Kicks all offline players
    public static void kickOffline() {
        Utils.sendClientMessage("Kicking all offline members...");
        Main.minecraft.thePlayer.sendChatMessage("/party kickoffline");
    }

    // Addes a new party member to the party list
    public static void addPartyMember(String username, PartyRank partyRank) {
        // If the player is already in the cache, it grabs the player from the cache
        if (playerCache.containsKey(username)) {
            PartyMember cachedMember = playerCache.get(username);
            cachedMember.setRank(partyRank);
            // Adds it to the party
            partyList.add(cachedMember);
        } else {
            // Creates a new uncached party member
            PartyMember member = new PartyMember(username, partyRank);
            partyList.add(member);
        }
    }

    // Loads the loads the information the the player and caches
    public static void loadPlayerData(String username) throws IOException {
        // Creates a new player
        PartyMember player = new PartyMember(username, PartyRank.LEADER);
        // Gets data
        player.getSkycryptData();
        // Says that it is the player
        player.isPlayer = true;
        // Adds to cache
        playerCache.put(username, player);
    }

    // Reparties all of the members of the party
    public static void reparty(List<PartyMember> partyMembers) {
        // Disbands the party
        Main.minecraft.thePlayer.sendChatMessage("/party disband");
        // Sets the delay 500 ms for the next message
        Long timeDelay = 500l;

        for (PartyMember member : partyMembers) {
            // Creates a new delay final time delay that can be used inside of the thread
            final long finalTimeDelay = timeDelay.longValue();
            new Thread() {
                @Override
                public void run() {
                    try {
                        // Waits the specified time
                        Thread.sleep(finalTimeDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Invites the member
                    Main.minecraft.thePlayer.sendChatMessage("/party invite " + member.username);
                }
            }.start();

            // Adds 500 ms for the next message
            timeDelay += 500l;
        }
    }
}
