//
// Written by Su386 (except where noted)
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.chat

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Pattern

object ChatManager {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!doModifyChatEnabled()) return

        if (event.type != 0.toByte()) return
        if (!event.message.doChatMessageModify()) return

        var messageToSend = event.message

        if (ChatColors.getChatColor(ChatColors.getPrefix(messageToSend.formattedText)).isNotEmpty()) {
            messageToSend = ChatColors.detectColorMessage(messageToSend)
        } else if (ChatColors.detectNonMessage(messageToSend).formattedText != messageToSend.formattedText) {
            messageToSend = ChatColors.detectNonMessage(messageToSend)
        }

        if (config.prettyMimicKilled) {
            messageToSend =
                ChatComponentText(
                    messageToSend.formattedText.replace(
                        "\$SKYTILS-DUNGEON-SCORE-MIMIC\$",
                        config.prettyMimicKilledString,
                    ),
                )
        }

        if (WordEditor.shouldEditMessage(messageToSend)) {
            messageToSend = ChatComponentText((WordEditor.handleWordEditorMessage(messageToSend.formattedText)))
        }

        if (ChatAlertsManager.checkChatAlert(messageToSend)  != null) {
            PartlySaneSkies.minecraft.soundHandler?.playSound(
                PositionedSoundRecord.create(
                    ResourceLocation(
                        "partlysaneskies",
                        "flute_scale",
                    ),
                ),
            )
            messageToSend = ChatAlertsManager.checkChatAlert(messageToSend, true)
        }

        if (config.owoLanguage) {
            messageToSend = ChatComponentText(OwO.owoify(messageToSend.formattedText))
        }

        if (messageToSend == event.message) return

        event.isCanceled = true
        messageToSend.chatStyle = event.message.chatStyle.createDeepCopy()

        val urls = messageToSend.extractUrls()
        if (!messageToSend.hasClickAction() && !event.message.hasClickAction() && urls.isNotEmpty()) {
            messageToSend.chatStyle.chatClickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, urls[0])
        }

        if (!messageToSend.hasHoverAction() && event.message.hasHoverAction()) {
            messageToSend.chatStyle.chatHoverEvent =
                HoverEvent(event.message.chatStyle.chatHoverEvent.action, event.message.chatStyle.chatHoverEvent.value)
        }

        PartlySaneSkies.minecraft.ingameGUI?.chatGUI?.printChatMessage(messageToSend)
    }

    fun IChatComponent.hasClickAction(): Boolean =
        chatStyle?.takeIf { !it.isEmpty }?.chatClickEvent?.value?.isNotEmpty() ?: false

    fun IChatComponent.hasHoverAction(): Boolean =
        chatStyle?.takeIf { !it.isEmpty }?.chatHoverEvent?.value?.unformattedText?.isNotEmpty() ?: false

    fun extractUrls(text: String): List<String> {
        val containedUrls = ArrayList<String>()
        val urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w:#@%/;$()~_?+-=\\\\.&]*)"
        val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val urlMatcher = pattern.matcher(text)

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)))
        }

        return containedUrls
    }

    fun IChatComponent.extractUrls(): List<String> = extractUrls(unformattedText.removeColorCodes())

    //    Returns if we interact with chat at all
//    ADD A CHECK FOR ANY FEATURE THAT MODIFIES AN EXISTING CHAT MESSAGE
    private fun doModifyChatEnabled(): Boolean = with (PartlySaneSkies.config) {
        colorCoopChat || colorGuildChat || colorOfficerChat || colorPartyChat ||
            colorNonMessages || colorPrivateMessages || ChatAlertsManager.getChatAlertCount() != 0 ||
            (WordEditor.wordsToEdit.isNotEmpty() && wordEditor) || owoLanguage
    }

    // ALSO HERE, DON'T FORGET
    private fun IChatComponent.doChatMessageModify(): Boolean {
        if (formattedText.startsWith("{\"server\":")) return false
        if (formattedText.startsWith(PartlySaneSkies.CHAT_PREFIX)) return false

        return ChatColors.getChatColor(ChatColors.getPrefix(formattedText)).isNotEmpty() ||
            ChatAlertsManager.checkChatAlert(this) != null ||
            ChatColors.detectNonMessage(this).formattedText != formattedText ||
            WordEditor.shouldEditMessage(this) || config.owoLanguage
    }
}
