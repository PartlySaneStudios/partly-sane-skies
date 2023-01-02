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
    private static String[] RANK_NAMES = { "[VIP]", "[VIP+]", "[MVP]", "[MVP+]", "[MVP++]", "[YOUTUBE]", "[MOJANG]",
            "[EVENTS]", "[MCP]", "[PIG]", "[PIG+]", "[PIG++]", "[PIG+++]", "[GM]", "[ADMIN]", "[OWNER]" };

    public static HashMap<String, PartyMember> playerCache = new HashMap<String, PartyMember>();
    private static List<PartyMember> partyList = new ArrayList<PartyMember>();

    public PartyManager() {

    }

    public static void startPartyManager() {
        isWaitingForMembers = true;
        // if (Main.config.apiKey.equals("")) {
        //     Utils.sendClientMessage(
        //             "No API Key detected. Party Manager will not work. Run /api new or add a key manually in the config");
        // }

        if (Main.config.autoKickOfflinePartyManager) {
            kickOffline();
        }

        Main.minecraft.thePlayer.sendChatMessage("/party list");
        partyList.clear();
    }

    @SubscribeEvent
    public void getMembers(ClientChatReceivedEvent event) {
        if (!isWaitingForMembers) {
            return;
        }
        ;

        if (event.message.getUnformattedText().startsWith("Party Leader: ")) {
            event.setCanceled(true);
            isMembersListed = true;
            String text = event.message.getUnformattedText();
            text = text.replace("Party Leader: ", "");
            for (String rank : RANK_NAMES) {
                text = text.replace(rank, "");
            }

            text = text.replace(" ", "");

            for (String name : text.split("●")) {
                addPartyMember(name, PartyRank.LEADER);
            }

        }

        else if (event.message.getUnformattedText().startsWith("Party Moderators: ")) {
            event.setCanceled(true);
            isMembersListed = true;
            String text = event.message.getUnformattedText();
            text = text.replace("Party Moderators: ", "");
            for (String rank : RANK_NAMES) {
                text = text.replace(rank, "");
            }

            text = text.replace(" ", "");

            for (String name : text.split("●")) {
                addPartyMember(name, PartyRank.MODERATOR);
            }

        }

        else if (event.message.getUnformattedText().startsWith("Party Members: ")) {
            event.setCanceled(true);
            isMembersListed = true;
            String text = event.message.getUnformattedText();

            text = text.replace("Party Members: ", "");
            for (String rank : RANK_NAMES) {
                text = text.replace(rank, "");
            }

            text = text.replace(" ", "");

            for (String name : text.split("●")) {
                addPartyMember(name, PartyRank.MEMBER);
            }

        }

        else if (event.message.getUnformattedText().startsWith("Party Members (")) {
            event.setCanceled(true);
        }

        else if (isMembersListed && event.message.getUnformattedText()
                .startsWith("-----------------------------------------------------")) {
            event.setCanceled(true);
            isMembersListed = false;
            isWaitingForMembers = false;
            openGui();
        }
        if (event.message.getUnformattedText().startsWith("-----------------------------------------------------")) {
            event.setCanceled(true);
        } else if (event.message.getUnformattedText().startsWith("You are not currently in a party.")) {
            event.setCanceled(true);
            Utils.sendClientMessage(Utils.colorCodes(
                    "&9&m-----------------------------------------------------\n&r&cError: Could not run Party Manager.\n&r&cYou are not currently in a party.\n&r&9&m-----------------------------------------------------"));
            isMembersListed = false;
            isWaitingForMembers = false;
        }
    }

    public static void openGui() {
        PartyManagerGui gui = new PartyManagerGui();
        Main.minecraft.displayGuiScreen(gui);

        gui.populateGui(partyList);
    }
     
    public static void kickOffline() {
        Utils.sendClientMessage("Kicking all offline members...");
        Main.minecraft.thePlayer.sendChatMessage("/party kickoffline");
    }

    public static void addPartyMember(String username, PartyRank partyRank) {
        if (playerCache.containsKey(username)) {
            PartyMember cachedMember = playerCache.get(username);
            cachedMember.setRank(partyRank);
            partyList.add(cachedMember);
        } else {
            PartyMember member = new PartyMember(username, partyRank);
            partyList.add(member);
        }
    }

    public static void loadPersonalPlayerData() throws IOException {
        String username = Main.minecraft.getSession().getUsername();
        PartyMember player = new PartyMember(username, PartyRank.LEADER);
        player.getSkycryptData();
        player.isPlayer = true;
        playerCache.put(username, player);
    }

    public static void reparty(List<PartyMember> partyMembers) {
        Main.minecraft.thePlayer.sendChatMessage("/party disband");
        Long timeDelay = 500l;

        for (PartyMember member : partyMembers) {
            final long finalTimeDelay = timeDelay.longValue();
            new Thread() {
                @Override
                public void run() {
                    try {

                        Thread.sleep(finalTimeDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Main.minecraft.thePlayer.sendChatMessage("/party invite " + member.username);
                }
            }.start();

            timeDelay += 500l;
        }
    }
}
