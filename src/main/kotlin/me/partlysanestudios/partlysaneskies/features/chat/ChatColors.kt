//
// Written by Su386 and ItsEmpa.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.chat

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.StringUtils.matchGroup
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

object ChatColors {

    fun detectColorMessage(message: IChatComponent): IChatComponent {
        val formattedMessage = message.formattedText
        val prefix = getPrefix(formattedMessage)
        if (prefix.isEmpty()) return message

        val color = getChatColor(prefix)
        return if (color.isEmpty()) message
        else ChatComponentText(insertColor(formattedMessage, color))
    }

    fun detectNonMessage(message: IChatComponent): IChatComponent {
        if (!PartlySaneSkies.config.colorNonMessages) return message

        val formattedMessage = message.formattedText
        if (!formattedMessage.contains("§r§7: ")) return message

        if (formattedMessage.startsWith("§dTo ") || formattedMessage.startsWith("§dFrom ")) {
            return message
        }

        val unformattedMessage = message.unformattedText
        val containsRankNames = PartlySaneSkies.RANK_NAMES.any { unformattedMessage.contains(it) }

        return if (containsRankNames) message
        else ChatComponentText(insertColor(formattedMessage, "§r"))
    }

    fun getChatColor(prefix: String): String = when (prefix.lowercase()) {
        "party" -> {
            if (!PartlySaneSkies.config.colorPartyChat) ""
            else if (PartlySaneSkies.config.visibleColors) "§6"
            else "§9"
        }
        "guild" -> {
            if (!PartlySaneSkies.config.colorGuildChat) ""
            else if (PartlySaneSkies.config.visibleColors) "§a"
            else "§2"
        }
        "officer" -> {
            if (!PartlySaneSkies.config.colorOfficerChat) ""
            else "§3"
        }
        "to", "from" -> {
            if (!PartlySaneSkies.config.colorPrivateMessages) ""
            else "§d"
        }
        "co-op" -> {
            if (!PartlySaneSkies.config.colorCoopChat) ""
            else "§b"
        }
        else -> ""
    }

    private val prefixPattern = "(?<chat>Party|Guild|Officer|To|From|Co-op) >?.*".toPattern()

    fun getPrefix(message: String): String = prefixPattern.matchGroup(message.removeColorCodes(), "chat") ?: ""

    private fun insertColor(message: String, color: String): String {
        var messageStartIndex = -1

        for (prefix in ChatAlertsManager.MESSAGE_PREFIXES) {
            if (message.contains(prefix)) {
                messageStartIndex = message.indexOf(prefix) + prefix.length
                break
            }
        }

        if (messageStartIndex == -1) return message

        val preMessageString = message.substring(0, messageStartIndex)
        val messageString = message.substring(messageStartIndex)

        return preMessageString + color + messageString.removeColorCodes()
    }
}
