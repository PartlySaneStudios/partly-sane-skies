//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.chat;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ChatColors {
    public static IChatComponent detectColorMessage(IChatComponent message) {
        String formattedMessage = message.getFormattedText();
        String prefix = getPrefix(formattedMessage);
        if (prefix.isEmpty()) {
            return message;
        }

        String color = getChatColor(prefix);
        if (color.isEmpty()) {
            return message;
        }

        return new ChatComponentText(insertColor(formattedMessage, color));
    }

    public static IChatComponent detectNonMessage(IChatComponent message) {
        if (!PartlySaneSkies.Companion.getConfig().getColorNonMessages()) {
            return message;
        }

        String formattedMessage = message.getFormattedText();
        // if it's not a non message, return
        if (!formattedMessage.contains("§r§7: ")) {
            return message;
        }

        // If its private message, return
        if (formattedMessage.startsWith("§dTo ") || formattedMessage.startsWith("§dFrom ")) {
            return message;
        }

        // Checks to see if the message has a rank
        boolean containsRankNames = false;
        String unformattedMessage = message.getUnformattedText();
        for (String rank : PartlySaneSkies.Companion.getRANK_NAMES()) {
            if (!unformattedMessage.contains(rank)) {
                continue;
            }
            containsRankNames = true;
            break;
        }

        // If it has a rank return
        if (containsRankNames) {
            return message;
        }

        // If it does not, it highlights the nons message
        return new ChatComponentText(insertColor(formattedMessage, "§r"));
    }

    public static String getChatColor(String prefix) {
        switch (prefix.toLowerCase()) {
            case "party":
                if (!PartlySaneSkies.Companion.getConfig().getColorPartyChat()) {
                    return "";
                }
                if (PartlySaneSkies.Companion.getConfig().getVisibleColors()) {
                    return "§6";
                }
                return "§9";

            case "guild":
                if (!PartlySaneSkies.Companion.getConfig().getColorGuildChat()) {
                    return "";
                }
                if (PartlySaneSkies.Companion.getConfig().getVisibleColors()) {
                    return "§a";
                }
                return "§2";

            case "officer":
                if (!PartlySaneSkies.Companion.getConfig().getColorOfficerChat()) {
                    return "";
                }
                return "§3";

            case "to":
            case "from":
                if (!PartlySaneSkies.Companion.getConfig().getColorPrivateMessages()) {
                    return "";
                }
                return "§d";

            case "co-op":
                if (!PartlySaneSkies.Companion.getConfig().getColorCoopChat()) {
                    return "";
                }
                return "§b";

            default:
                return "";
        }
    }

    public static String getPrefix(String message) {
        message = StringUtils.INSTANCE.removeColorCodes(message);
        if (message.startsWith("Party >")) {
            return "Party";
        } else if (message.startsWith("Guild >")) {
            return "Guild";
        } else if (message.startsWith("Officer >")) {
            return "Officer";
        } else if (message.startsWith("To ")) {
            return "To";
        } else if (message.startsWith("From ")) {
            return "From";
        } else if (message.startsWith("Co-op >")) {
            return "Co-op";
        }

        return "";
    }

    public static String insertColor(String message, String color) {
        int messageStartIndex = -1;

        for (String prefix : ChatAlertsManager.MESSAGE_PREFIXES) {
            if (message.contains(prefix)) {
                messageStartIndex = message.indexOf(prefix) + prefix.length();
                break;
            }
        }

        if (messageStartIndex == -1) {
            return message;
        }

        String messageString = message.substring(messageStartIndex);
        String preMessageString = message.substring(0, messageStartIndex);

        messageString = StringUtils.INSTANCE.removeColorCodes(messageString);
        messageString = color + messageString;

        return preMessageString + messageString;
    }
}
