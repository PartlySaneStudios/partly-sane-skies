//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import org.apache.logging.log4j.Level
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

object ChatUtils {

    fun visPrint(print: Any) {
        SystemUtils.log(Level.INFO, "\n\n\n$print\n\n\n".trimIndent())
        try {
            PartlySaneSkies.minecraft.ingameGUI
                .chatGUI
                .printChatMessage(ChatComponentText("\n            $print"))
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
        PartlySaneSkies.minecraft.ingameGUI
            .chatGUI
            .printChatMessage(chatComponent)
    }

    fun sendClientMessage(text: String) {
        sendClientMessage(text, false)
    }

    // If true, Sends a message discretely without the Prefix Partly Sane Skies >:
    fun sendClientMessage(text: String, silent: Boolean) {
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