package me.partlysanestudios.partlysaneskies.partyfriend;

import java.util.ArrayList;
import java.util.List;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PartyFriendManager {
    private static boolean isWaitingForMembers = false;
    private static List<String> partyList = new ArrayList<String>();
    private static int page = 0;

    public static void startPartyManager() {
        isWaitingForMembers = true;
        page = 1;
        PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/friend list");
        partyList.clear();
    }

    @SubscribeEvent
    public void getMembers(ClientChatReceivedEvent event) {
        if (!isWaitingForMembers) {
            return;
        }
        ;
        if (event.message.getUnformattedText().startsWith("-----------------------------------------------------")) {
            isWaitingForMembers = false;
        }
        String message = Utils.removeColorCodes(event.message.getFormattedText());

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
        Long timeDelay = 500l;

        for (String member : partyList) {
            final long finalTimeDelay = timeDelay.longValue();
            new Thread() {
                @Override
                public void run() {
                    try {

                        Thread.sleep(finalTimeDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party invite " + member);
                }
            }.start();

            timeDelay += 500l;
        }
    }
}
