//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.events.minecraft.PSSChatEvent
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import org.apache.logging.log4j.Level
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

object ChatUtils {
    /**
     * Prints an obvious message to the client and copies it to the clipboard.
     * @param print The message to print.
     */
    fun visPrint(print: Any) {
        SystemUtils.log(Level.INFO, "\n\n\n$print\n\n\n".trimIndent())
        try {
            PartlySaneSkies.minecraft.ingameGUI.chatGUI.printChatMessage(ChatComponentText("\n            $print"))
            val stringSelection = StringSelection(print.toString())
            try {
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
            } catch (e: IllegalStateException) {
            }
        } catch (ignored: NullPointerException) {
        }
    }

    fun sendClientMessage(chatComponent: IChatComponent?) {
        PartlySaneSkies.minecraft.ingameGUI.chatGUI.printChatMessage(chatComponent)
    }

    fun sendClientMessage(text: String) {
        sendClientMessage(text, false)
    }



    val PSSChatEvent.trueUnformattedMessage get() = this.component.unformattedText.removeColorCodes()

    /**
     * Sends a message to the client.
     * @param text The message to send.
     * @param silent If true, Sends a message discretely without the Prefix Partly Sane Skies >:
     */
    fun sendClientMessage(text: String, silent: Boolean = false) {
        if (silent) {
            try {
                sendClientMessage(ChatComponentText(text))
            } catch (ignored: java.lang.NullPointerException) {
            }
        } else {
            try {
                sendClientMessage(ChatComponentText(PartlySaneSkies.CHAT_PREFIX + text))
            } catch (ignored: java.lang.NullPointerException) {
            }
        }
    }
}
