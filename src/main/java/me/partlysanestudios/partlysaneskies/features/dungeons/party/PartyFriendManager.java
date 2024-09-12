//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons.party;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.api.events.PSSEvent;
import me.partlysanestudios.partlysaneskies.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.events.minecraft.PSSChatEvent;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PartyFriendManager {
    private static final List<String> partyList = new ArrayList<>();
    private static boolean isWaitingForMembers = false;
    private static int page = 0;

    public static void startPartyManager() {
        isWaitingForMembers = true;
        page = 1;
        PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/friend list");
        partyList.clear();
    }

    public static void registerCommand() {
        new PSSCommand("friendparty")
            .addAlias("fp")
            .addAlias("pf")
            .setDescription("Parties all friends in the friend list")
            .setRunnable(a -> {
                PartyFriendManager.startPartyManager();
            })
            .register();
    }

    public static void partyAll() {
        long timeDelay = 500L;

        for (String member : partyList) {
            final long finalTimeDelay = timeDelay;
            new Thread(() -> {
                try {

                    Thread.sleep(finalTimeDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PartlySaneSkies.Companion.getMinecraft().addScheduledTask(() -> PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/party invite " + member));

            }).start();

            timeDelay += 500L;
        }
    }

    @PSSEvent.Subscribe
    public void onChat(PSSChatEvent event) {
        if (!isWaitingForMembers) {
            return;
        }
        if (event.getComponent().getUnformattedText().startsWith("-----------------------------------------------------")) {
            isWaitingForMembers = false;
        }
        String message = StringUtils.INSTANCE.removeColorCodes(event.getMessage());

        String[] rows = message.split("\n");

        boolean isEndOfPage = false;
        for (String row : rows) {
            if (row.contains(" is in")) {
                partyList.add(row.substring(0, row.indexOf(" is in")));
            }

            if (row.contains(" is curr")) {
                isEndOfPage = true;
                break;
            }
        }

        if (isEndOfPage) {
            partyAll();
        } else {
            isWaitingForMembers = true;
            page++;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/friend list " + page);
        }
    }
}
