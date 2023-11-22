package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import net.minecraft.util.ChatComponentText
import org.apache.logging.log4j.Level
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

object ChatUtils {

    fun visPrint(print: Any) {
        Utils.log(Level.INFO, "\n\n\n$print\n\n\n".trimIndent())
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
}