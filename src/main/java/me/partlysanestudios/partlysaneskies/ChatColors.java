package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatColors {
    @SubscribeEvent
    public void detectColorMessage(ClientChatReceivedEvent event) {
        String formattedMessage = event.message.getFormattedText();
        String prefix = getPrefix(formattedMessage);
        if (prefix.equals("")) {
            return;
        }

        event.setCanceled(true);
        String color = getChatColor(prefix);
        Utils.sendClientMessage(insertColor(formattedMessage, color), true);
    }

    @SubscribeEvent
    public void detectNonMessage(ClientChatReceivedEvent event) {
        if (!PartlySaneSkies.config.colorNonMessages) {
            return;
        }

        String formattedMessage = event.message.getFormattedText();
        // if it's not a non message, return
        if (!formattedMessage.contains("§r§7: ")) {
            return;
        }

        // If its a private message, return
        if (formattedMessage.startsWith("§dTo ") || formattedMessage.startsWith("§dFrom ")) {
            return;
        }

        // Checks to see if the message has a rank
        boolean containsRankNames = false;
        String unformattedMessage = event.message.getUnformattedText();
        for (String rank : PartlySaneSkies.RANK_NAMES) {
            if (!unformattedMessage.contains(rank)) {
                continue;
            }
            containsRankNames = true;
            break;
        }

        // If it has a rank return
        if (containsRankNames) {
            return;
        }
        
        // If it does not, it highlights the nons message
        event.setCanceled(true);
        Utils.sendClientMessage(insertColor(formattedMessage, "&r"), true);
    }

    public static String getChatColor(String prefix) {
        switch (prefix.toLowerCase()) {
            case "party":
                if (!PartlySaneSkies.config.colorPartyChat) {
                    return "";
                }
                return Utils.colorCodes("&9");

            case "guild":
                if (!PartlySaneSkies.config.colorGuildChat) {
                    return "";
                }
                return Utils.colorCodes("&2");

            case "officer":
                if (!PartlySaneSkies.config.colorOfficerChat) {
                    return "";
                }
                return Utils.colorCodes("&3");

            case "to":
                if (!PartlySaneSkies.config.colorPrivateMessages) {
                    return "";
                }
                return Utils.colorCodes("&d");

            case "from":
                if (!PartlySaneSkies.config.colorPrivateMessages) {
                    return "";
                }
                return Utils.colorCodes("&d");

            case "co-op":
                if (!PartlySaneSkies.config.colorCoopChat) {
                    return "";
                }
                return Utils.colorCodes("&b");

            default:
                return "";
        }
    }

    public static String getPrefix(String message) {
        message = Utils.removeColorCodes(message);
        if (message.startsWith("Party >")) {
            return "Party";
        }

        else if (message.startsWith("Guild >")) {
            return "Guild";
        }

        else if (message.startsWith("Officer >")) {
            return "Officer";
        }
        
        else if (message.startsWith("To ")) {
            return "To";
        }

        else if (message.startsWith("From ")) {
            return "From";
        }
        else if (message.startsWith("Co-op >")) {
            return "Co-op";
        }

        return "";
    }

    public static String insertColor(String message, String color) {
        int messageStartIndex = message.indexOf(": ");

        if (messageStartIndex == -1) {
            return message;
        }

        String messageString = message.substring(messageStartIndex);
        String preMessageString = message.substring(0, messageStartIndex);

        messageString = Utils.removeColorCodes(messageString);
        messageString = color + messageString;
        
        return preMessageString + messageString;
    }
}
