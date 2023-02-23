package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatColors {
    
    @SubscribeEvent
    public void onMessageSend(ClientChatReceivedEvent event) {
        String formattedMessage = event.message.getFormattedText();
        
        String prefix = getPrefix(formattedMessage);
        if (prefix.equals("")) {
            return;
        }

        event.setCanceled(true);

        String color = getChatColour(prefix);

        Utils.sendClientMessage(insertColour(formattedMessage, color), true);
    }

    public static String getChatColour(String prefix) {
        switch (prefix.toLowerCase()) {
            case "party":
                if (!Main.config.colorPartyChat) {
                    return "";
                }
                return Utils.colorCodes("&9");

            case "guild":
                if (!Main.config.colorGuildChat) {
                    return "";
                }
                return Utils.colorCodes("&2");

            case "officer":
                if (!Main.config.colorOfficerChat) {
                    return "";
                }
                return Utils.colorCodes("&3");

            case "to":
                if (!Main.config.colorPrivateMessages) {
                    return "";
                }
                return Utils.colorCodes("&d");

            case "from":
                if (!Main.config.colorPrivateMessages) {
                    return "";
                }
                return Utils.colorCodes("&d");

            case "co-op":
                if (!Main.config.colorCoopChat) {
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

    public static String insertColour(String message, String colour) {
        int messageStartIndex = message.indexOf(": ");

        if (messageStartIndex == -1) {
            return "";
        }

        String messageString = message.substring(messageStartIndex);
        String preMessageString = message.substring(0, messageStartIndex);

        messageString = Utils.removeColorCodes(messageString);
        messageString = colour + messageString;
        
        return preMessageString + messageString;
    }
}
