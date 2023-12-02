//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class PartyFriendManager {
    private static boolean isWaitingForMembers = false;
    private static final List<String> partyList = new ArrayList<>();
    private static int page = 0;

    public static void startPartyManager() {
        isWaitingForMembers = true;
        page = 1;
        PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/friend list");
        partyList.clear();
    }

    public static void registerCommand() {
        new PSSCommand("friendparty")
                .addAlias("fp")
                .addAlias("pf")
                .setDescription("Parties all friends in the friend list")
                .setRunnable((s, a) -> {
                    PartyFriendManager.startPartyManager();
                })
                .register();
    }

    @SubscribeEvent
    public void getMembers(ClientChatReceivedEvent event) {
        if (!isWaitingForMembers) {
            return;
        }
        if (event.message.getUnformattedText().startsWith("-----------------------------------------------------")) {
            isWaitingForMembers = false;
        }
        String message = StringUtils.INSTANCE.removeColorCodes(event.message.getFormattedText());

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
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/friend list " + page);
        }
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
                PartlySaneSkies.minecraft.addScheduledTask(() -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party invite " + member));

            }).start();

            timeDelay += 500L;
        }
    }
}
