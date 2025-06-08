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

        var formattedMessage = event.message.formattedText

        if (ChatColors.getChatColor(ChatColors.getPrefix(formattedMessage)).isNotEmpty()) {
            formattedMessage = ChatColors.detectColorMessage(formattedMessage)
        } else if (ChatColors.detectNonMessage(formattedMessage) != formattedMessage) {
            formattedMessage = ChatColors.detectNonMessage(formattedMessage)
        }

        if (config.prettyMimicKilled) {
            formattedMessage = formattedMessage.replace(
                "\$SKYTILS-DUNGEON-SCORE-MIMIC\$",
                config.prettyMimicKilledString,
            )
        }

        if (WordEditor.shouldEditMessage(formattedMessage)) {
            formattedMessage = WordEditor.handleWordEditorMessage(formattedMessage)
        }

        if (ChatAlertsManager.checkChatAlert(formattedMessage) != null) {
            PartlySaneSkies.minecraft.soundHandler?.playSound(
                PositionedSoundRecord.create(
                    ResourceLocation(
                        "partlysaneskies",
                        "flute_scale",
                    ),
                ),
            )
            formattedMessage = ChatAlertsManager.checkChatAlert(formattedMessage, true)
        }

        if (config.owoLanguage) {
            formattedMessage = OwO.owoify(formattedMessage)
        }

        if (formattedMessage == event.message.formattedText) return

        val messageToSend = ChatComponentText(formattedMessage)
        event.isCanceled = true
        messageToSend.chatStyle = event.message.chatStyle.createDeepCopy()

        messageToSend.chatStyle.chatClickEvent = event.message
            ?.chatStyle
            ?.createDeepCopy()
            ?.chatClickEvent ?: event.message
            ?.siblings
            ?.firstOrNull { it.chatStyle.chatClickEvent != null }
            ?.chatStyle
            ?.chatClickEvent

        messageToSend.chatStyle.chatHoverEvent = event.message
            ?.chatStyle
            ?.createDeepCopy()
            ?.chatHoverEvent ?: event.message
            ?.siblings
            ?.firstOrNull { it.chatStyle.chatHoverEvent != null }
            ?.chatStyle
            ?.chatHoverEvent

        PartlySaneSkies.minecraft.ingameGUI?.chatGUI?.printChatMessage(messageToSend)
    }

    fun IChatComponent.hasClickAction(): Boolean =
        chatStyle?.chatClickEvent?.value?.isNotEmpty() ?: false

    fun IChatComponent.hasHoverAction(): Boolean =
        chatStyle?.chatHoverEvent?.value?.unformattedText?.isNotEmpty() ?: false

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
            ChatAlertsManager.checkChatAlert(this.formattedText) != null ||
            ChatColors.detectNonMessage(this.formattedText) != formattedText ||
            WordEditor.shouldEditMessage(this.formattedText) || config.owoLanguage
    }
}
