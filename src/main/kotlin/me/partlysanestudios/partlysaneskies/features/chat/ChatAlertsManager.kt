//
// Written by Su386 and ItsEmpa.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.chat

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.system.SystemNotification
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import org.lwjgl.opengl.Display
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object ChatAlertsManager {
    private const val DATA_PATH_NAME = "./config/partly-sane-skies/chatAlertsData.json"
    private var chatAlertsList = mutableListOf<String>()
    @JvmField
    val MESSAGE_PREFIXES = listOf("§r§7: ", "§r§f: ", "§f: ")
    private val NON_COLOR_CODES = listOf("§r", "§o", "§n", "§m", "§l", "§k")

    @Throws(IOException::class)
    fun load() {
        val file = File(DATA_PATH_NAME)

        if (file.createNewFile()) {
            file.setWritable(true)
            FileWriter(file).use { writer ->
                writer.write(Gson().toJson(ArrayList<String>()))
            }
        }

        file.setWritable(true)
        Files.newBufferedReader(Paths.get(file.path)).use { reader ->
            chatAlertsList = Gson().fromJson(reader, ArrayList::class.java) as ArrayList<String>
        }
    }

    fun registerCommand() {
        PSSCommand("chatalerts")
            .addAlias("ca", "chatAlert", "chal")
            .setDescription("Operates the chat alerts feature: /chatalerts <add/remove/list> ")
            .setRunnable { args ->
                when {
                    args.isEmpty() -> ChatUtils.sendClientMessage("§cIncorrect usage. Correct usage: /chatalerts add/remove/list")
                    else -> when (args[0]) {
                        "list" -> listAlerts()
                        "add" -> {
                            if (args.size == 1) {
                                ChatUtils.sendClientMessage("§cIncorrect usage. Correct usage: /chatalerts add [alert]")
                            } else {
                                val alert = args.copyOfRange(1, args.size).joinToString(" ").trim()
                                addAlert(alert)
                            }
                        }
                        "remove" -> {
                            if (args.size == 1) {
                                ChatUtils.sendClientMessage("§cIncorrect usage. Correct usage: /chatalerts remove [number]")
                            } else {
                                args[1].toIntOrNull()?.let { removeAlert(it) } ?: run {
                                    ChatUtils.sendClientMessage("§c\"${args[1]}\" could not be read as a number. Correct Usage: /chatalerts remove [number]")
                                }
                            }
                        }
                        else -> ChatUtils.sendClientMessage("§cIncorrect usage. Correct usage: /chatalerts add/remove/list")
                    }
                }
            }.register()
    }

    @Throws(IOException::class)
    fun save() {
        val file = File(DATA_PATH_NAME)
        val gson = GsonBuilder().setPrettyPrinting().serializeSpecialFloatingPointValues().create()
        val json = gson.toJson(chatAlertsList)
        FileWriter(file).use { writer ->
            writer.write(json)
        }
    }

    private fun addAlert(alert: String) {
        chatAlertsList += alert
        try {
            save()
            ChatUtils.sendClientMessage("§b\"§d$alert§b\" was successfully added as alert number §d${chatAlertsList.size}§b.")
        } catch (e: IOException) {
            e.printStackTrace()
            ChatUtils.sendClientMessage("§cChat Alerts was unable to save. Please try again.")
        }
    }

    fun getChatAlertCount(): Int = chatAlertsList.size

    private fun listAlerts() {
        val message = StringBuilder("§d§m-----------------------------------------------------\n§bChat Alerts:\n§d§m-----------------------------------------------------\n")
        chatAlertsList.forEachIndexed { index, alert ->
            message.append("${(index+1).formatNumber()}: $alert\n")
        }
        ChatUtils.sendClientMessage(message.toString())
    }

    private fun removeAlert(id: Int) {
        if (id > chatAlertsList.size || id <= 0) {
            ChatUtils.sendClientMessage("§cChat alert number $id was not found. Please enter a valid number.")
            return
        }
        val message = chatAlertsList[id - 1]
        chatAlertsList.removeAt(id - 1)
        try {
            save()
        } catch (e: IOException) {
            ChatUtils.sendClientMessage("§cChat Alerts was unable to save. Please try again.")
            e.printStackTrace()
        }
        ChatUtils.sendClientMessage("§bChat Alert number §d$id §b(\"§d$message§b\") was successfully removed.")
    }

    fun checkChatAlert(message: IChatComponent): IChatComponent? {
        return checkChatAlert(message, false)
    }

    fun checkChatAlert(message: IChatComponent, sendSystemNotification: Boolean): IChatComponent? {
        var formattedMessage = message.formattedText
        var beginMessageIndex = formattedMessage.indexOfAny(MESSAGE_PREFIXES)

        if (beginMessageIndex == -1) {
            return null
        }

        beginMessageIndex = indexInUnformattedString(formattedMessage, beginMessageIndex)

        val unformattedMessage = formattedMessage.removeColorCodes()
        val cleanMessage = unformattedMessage
            .substring(beginMessageIndex)
            .replaceFirst(": ", "")
            .trim()

        if (!chatAlertsList.any { cleanMessage.contains(it, true) }) {
            return null
        }
        chatAlertsList.filter { cleanMessage.contains(it, true) && it.isNotEmpty()}
            .forEach {
                var startIndex = beginMessageIndex
                var indexOfAlert = unformattedMessage.indexOf(it, startIndex, true)
                while (indexOfAlert != -1) {
                    if (
                        !chatAlertsList.any {
                            it2 -> it2.length > it.length && unformattedMessage.startsWith(it2, indexOfAlert, true)
                        }
                    ) {
                        val formattedIndex = indexInFormattedString(formattedMessage, indexOfAlert)
                        val oldCode = getLastColorCode(formattedMessage.substring(0, formattedIndex))
                        formattedMessage = "${formattedMessage.substring(0, formattedIndex)}§d§l" +
                            "${formattedMessage.substring(formattedIndex, formattedIndex + it.length)}§r$oldCode" +
                            formattedMessage.substring(formattedIndex + it.length)
                    }
                    startIndex = indexOfAlert + it.length
                    indexOfAlert = unformattedMessage.indexOf(it, startIndex, true)
                }
            }

        if (PartlySaneSkies.config.chatAlertSendSystemNotification && !Display.isActive() && sendSystemNotification) {
            SystemNotification.showNotification(message.formattedText.removeColorCodes())
        }
        return ChatComponentText(formattedMessage.toString())
    }

    private fun indexInFormattedString(formattedMessage: String, indexInUnformattedMessage: Int): Int {
        var unformattedIndex = 0
        var formattedIndex = 0
        val unformattedMessage = formattedMessage.removeColorCodes()
        while (
            formattedIndex < formattedMessage.length &&
            unformattedIndex < unformattedMessage.length
        ) {
            if (formattedMessage[formattedIndex] == '§') {
                formattedIndex += 2
            }
            else if (unformattedIndex == indexInUnformattedMessage) {
                return formattedIndex
            }
            else {
                unformattedIndex++
                formattedIndex++
            }
        }

        return 0
    }

    private fun indexInUnformattedString(formattedMessage: String, indexInFormattedMessage: Int): Int =
        indexInFormattedMessage - 2 * formattedMessage.substring(0, indexInFormattedMessage)
            .count { '§' == it }

    private fun getLastColorCode(str: String): String {
        val index = str.lastIndexOf('§')
        return str.substring(index, index + 2)
    }
}
