package me.partlysanestudios.partlysaneskies.features.chat

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.system.SystemNotification
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils
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
import java.util.ArrayList
import java.util.Locale

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
            .addAlias("ca")
            .addAlias("chatAlert")
            .addAlias("chal")
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
                                val id = args[1].toIntOrNull()?.let { removeAlert(it) } ?: run {
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

    fun addAlert(alert: String) {
        chatAlertsList += alert
        try {
            save()
        } catch (e: IOException) {
            ChatUtils.sendClientMessage("§cChat Alerts was unable to save. Please try again.")
            e.printStackTrace()
        }
        ChatUtils.sendClientMessage("§b\"§d$alert§b\" was successfully added as alert number §d${chatAlertsList.size}§b.")
    }

    fun getChatAlertCount(): Int = chatAlertsList.size

    fun listAlerts() {
        val message = StringBuilder("§d§m-----------------------------------------------------\n§bChat Alerts:\n§d§m-----------------------------------------------------\n")
        chatAlertsList.forEachIndexed { index, alert ->
            message.append("${(index+1).formatNumber()}: $alert\n")
        }
        ChatUtils.sendClientMessage(message.toString())
    }

    fun removeAlert(id: Int) {
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

    fun checkChatAlert(message: IChatComponent): IChatComponent {
        return checkChatAlert(message, false)
    }

    fun checkChatAlert(message: IChatComponent, sendSystemNotification: Boolean): IChatComponent {
        val formattedMessage = message.formattedText
        var beginMessageIndex = -1
        for (messagePrefix in MESSAGE_PREFIXES) {
            beginMessageIndex = formattedMessage.indexOf(messagePrefix)
            if (beginMessageIndex != -1) {
                break
            }
        }

        if (beginMessageIndex == -1) {
            return message
        }

        val unformattedMessage = formattedMessage.removeColorCodes()
        var rawMessage = formattedMessage.substring(beginMessageIndex)
        rawMessage = rawMessage.removeColorCodes().replaceFirst(": ", "").trim()
        val lowerCaseMessage = rawMessage.lowercase()

        for (alert in chatAlertsList) {
            if (!lowerCaseMessage.contains(alert.lowercase())) {
                continue
            }
            val messageBuilder = StringBuilder(formattedMessage)
            val alertIndexUnformatted = unformattedMessage.lowercase().indexOf(alert.lowercase(), unformattedMessage.indexOf(rawMessage))
            val numOfColorCodeTotal = numOfColorCodes(formattedMessage)
            val numOfColorCodeBefore = numOfColorCodeTotal - 1
            val alertIndexFormatted = numOfColorCodeBefore * 2 + alertIndexUnformatted
            val charsToAdd = getLastColorCode(formattedMessage.substring(0, alertIndexFormatted + 1)).toCharArray()
            messageBuilder.insert(alertIndexFormatted + alert.length, charsToAdd, 0, charsToAdd.size)
            messageBuilder.insert(alertIndexFormatted, "§d§l".toCharArray(), 0, 3)

            if (PartlySaneSkies.config.chatAlertSendSystemNotification && !Display.isActive() && sendSystemNotification) {
                SystemNotification.showNotification(message.formattedText.removeColorCodes())
            }
            return ChatComponentText(messageBuilder.toString())
        }
        return message
    }

    private fun numOfColorCodes(str: String): Int {
        var i = 0
        val textBuilder = StringBuilder(str)
        while (textBuilder.indexOf("§") != -1) {
            textBuilder.deleteCharAt(textBuilder.indexOf("§") + 1)
            textBuilder.deleteCharAt(textBuilder.indexOf("§"))
            i++
        }
        return i
    }

    private fun getLastColorCode(str: String): String {
        var currentCode = ""
        val textBuilder = StringBuilder(str)
        while (textBuilder.indexOf("§") != -1) {
            var shouldContinue = false
            for (code in NON_COLOR_CODES) {
                if (textBuilder.indexOf("§") == -1 || textBuilder.substring(textBuilder.indexOf("§"), textBuilder.indexOf("§") + 2) != code) {
                    continue
                }
                textBuilder.deleteCharAt(textBuilder.indexOf("§") + 1)
                textBuilder.deleteCharAt(textBuilder.indexOf("§"))
                shouldContinue = true
                break
            }

            if (shouldContinue || textBuilder.indexOf("§") == -1) {
                continue
            }

            currentCode = textBuilder.substring(textBuilder.indexOf("§"), textBuilder.indexOf("§") + 2)
            textBuilder.deleteCharAt(textBuilder.indexOf("§") + 1)
            textBuilder.deleteCharAt(textBuilder.indexOf("§"))
        }
        return currentCode
    }
}
