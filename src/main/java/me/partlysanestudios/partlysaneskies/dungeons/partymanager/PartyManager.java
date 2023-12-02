//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.dungeons.partymanager;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyMember.PartyRank;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartyManager {
    private static boolean isWaitingForMembers = false;
    private static boolean isMembersListed = false;
    public static HashMap<String, PartyMember> playerCache = new HashMap<>();
    private static final List<PartyMember> partyList = new ArrayList<>();

    public PartyManager() {

    }

    public static void startPartyManager() {
        // Tells the program to start waiting for party members to be listed
        isWaitingForMembers = true;

        // If config option, kicks all offline party members
        if (PartlySaneSkies.config.autoKickOfflinePartyManager) {
            kickOffline();
        }

        // Creates a new thread that is separate from the game thread
        new Thread(() -> {
            // Sleeps to avoid conflict with kicking offline
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            PartlySaneSkies.minecraft.addScheduledTask(() -> {
                // Starts the party manager
                PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party list");
            });


            partyList.clear();
        }).start();

    }

    public static void registerCommand() {
        new PSSCommand("partymanager")
                .addAlias("pm")
                .addAlias("partym")
                .setDescription("Opens the Party Manager")
                .setRunnable((s, a) -> {
                    PartyManager.startPartyManager();
                })
                .register();
    }

    @SubscribeEvent
    public void onMemberJoin(ClientChatReceivedEvent event) {
        if (!PartlySaneSkies.config.getDataOnJoin) {
            return;
        }

        String unformattedMessage = event.message.getUnformattedText();
        // If the message is not a join dungeon message
        if (!(unformattedMessage.startsWith("Party Finder >") || unformattedMessage.contains("joined the dungeon group!"))) {
            return;
        }

        String memberName = unformattedMessage;
        memberName = memberName.replace("Party Finder > ", "");
        int indexOfText = memberName.indexOf("joined the dungeon group!");
        memberName = memberName.substring(0, indexOfText);
        memberName = StringUtils.INSTANCE.stripLeading(memberName);
        memberName = StringUtils.INSTANCE.stripTrailing(memberName);

        try {
            loadPlayerData(memberName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Upon chat message receives, it will check to see if it is the party list
    @SubscribeEvent
    public void getMembers(ClientChatReceivedEvent event) {
        // If it's not waiting for party members, it returns
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

        // Detects the closing line -----
        // when all the members have been listed and the bar appears, its end of the message
        else if (isMembersListed && event.message.getUnformattedText().startsWith("-----------------------------------------------------")) {
            // Hides the message
            event.setCanceled(true);
            // Resets
            isMembersListed = false;
            isWaitingForMembers = false;
            // Opens the message
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
            // Sends an error message
            ChatUtils.INSTANCE.sendClientMessage(("§9§m-----------------------------------------------------\n "+
                    "§r§cError: Could not run Party Manager." +
                    "\n§r§cYou are not currently in a party."
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
        for (String playerRank : PartlySaneSkies.RANK_NAMES) {
            str = str.replace(playerRank, "");
        }

        // Removes all space
        str = str.replace(" ", "");

        // Splits the list by the status indicator located before every name
        for (String name : str.split("●")) {
            addPartyMember(name, rank);
        }
    }

    // Opens the party manager GUI
    public static void openGui() {
        PartyManagerGui gui = new PartyManagerGui();
        PartlySaneSkies.minecraft.displayGuiScreen(gui);

        // Populates the GUI with the party list
        gui.populateGui(partyList);
    }
     
    // Kicks all offline players
    public static void kickOffline() {
        ChatUtils.INSTANCE.sendClientMessage("Kicking all offline members...");
        PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party kickoffline");
    }

    // Adds a new party member to the party list
    public static void addPartyMember(String username, PartyRank partyRank) {
        // If the player is already in the cache, it grabs the player from the cache
        if (playerCache.containsKey(username)) {
            PartyMember cachedMember = playerCache.get(username);
            cachedMember.setRank(partyRank);
            // Adds it to the party
            partyList.add(cachedMember);
        } else {
            // Creates a new uncased party member
            PartyMember member = new PartyMember(username, partyRank);
            partyList.add(member);
        }
    }

    // Loads the information the player and caches
    public static void loadPlayerData(String username) throws IOException {
        // Creates a new player
        PartyMember player = new PartyMember(username, PartyRank.LEADER);
        new Thread (() -> {
            try {
                player.populateData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }).start();


    }

    // Reparties all the members of the party
    public static void reparty(List<PartyMember> partyMembers) {
        // Disbands the party
        PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party disband");
        // Sets the delay 500 ms for the next message
        long timeDelay = 500L;

        for (PartyMember member : partyMembers) {
            // Creates a new delay final time delay that can be used inside the thread
            final long finalTimeDelay = timeDelay;
            new Thread(() -> {
                try {
                    // Waits the specified time
                    Thread.sleep(finalTimeDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Invites the member
                PartlySaneSkies.minecraft.addScheduledTask(() -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party invite " + member.username));

            }).start();

            // Adds 500 ms for the next message
            timeDelay += 500L;
        }
    }
}
