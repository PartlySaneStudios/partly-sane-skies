//
// Written by Su386 (except where noted)
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.chat

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Pattern


object ChatManager {
    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
//        If all chat modification features are disabled
        if (!doModifyChatEnabled()) {
            return
        }

        if (event.type != 0.toByte()) {
            return
        }

//        If the message doesn't need to be modified
        if (!event.message.doChatMessageModify()) {
            return
        }

        // ChatUtils.sendClientMessage("ChatManager.onChatReceived: ${event.message.formattedText}")

        event.isCanceled = true // cancels the even

        var messageToSend = event.message // Creates a new message to build off of

//        If the chat colours is supposed to run
        if (ChatColors.getChatColor(ChatColors.getPrefix(messageToSend.formattedText)).isNotEmpty()) {
            messageToSend = ChatColors.detectColorMessage(messageToSend)
        }

        else if (!ChatColors.detectNonMessage(messageToSend).formattedText.equals(messageToSend.formattedText)) {
            messageToSend = ChatColors.detectNonMessage(messageToSend)
        }

//        If the chat alerts manager finds something
        if (!ChatAlertsManager.checkChatAlert(messageToSend).formattedText.equals(messageToSend.formattedText)) {
            // Plays a flute sound
            PartlySaneSkies.minecraft
                .soundHandler
                .playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "flute_scale")))
            messageToSend = ChatAlertsManager.checkChatAlert(messageToSend)
        }

        // If the word editor wants to edit something
        if (WordEditor.shouldEditMessage(messageToSend)) {
            messageToSend = ChatComponentText((WordEditor.handleWordEditorMessage(messageToSend.formattedText)));
        }

        // If owo language is enabled
        if (PartlySaneSkies.config.owoLanguage) {
            messageToSend = ChatComponentText(OwO.owoify(messageToSend.formattedText))
        }

        // If the message has not changed
        if (messageToSend.equals(event.message)) {
            event.isCanceled = false // Reset the event
            // ChatUtils.sendClientMessage("Message has not changed")
            return // Exit
        }


        messageToSend.chatStyle = event.message.chatStyle.createDeepCopy()

        val urls = messageToSend.extractUrls()
        if (!messageToSend.hasClickAction() && !event.message.hasClickAction() && urls.isNotEmpty()) {
            messageToSend.chatStyle.chatClickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, urls[0])
        }

        if (!messageToSend.hasHoverAction() && event.message.hasHoverAction()) {
            messageToSend.chatStyle.chatHoverEvent = HoverEvent(event.message.chatStyle.chatHoverEvent.action, event.message.chatStyle.chatHoverEvent.value)
        }

        PartlySaneSkies.minecraft.ingameGUI.chatGUI.printChatMessage(messageToSend)
    }

    fun IChatComponent.hasClickAction(): Boolean {
//        If the message is null
        if (this.chatStyle == null) {
            return false
        }

//        If the chat style is empty
        else if (this.chatStyle.isEmpty) {
            return false
        }

//        If the chat has no click event
        else if (this.chatStyle.chatClickEvent == null) {
            return false
        }

//        If the chat has no click event value
        else if (this.chatStyle.chatClickEvent.value == null) {
            return false
        }

//        If the chat has no value for the action
        else if (this.chatStyle.chatClickEvent.value.isEmpty()) {
            return false
        }

        return true
    }

    fun IChatComponent.hasHoverAction(): Boolean {
//        If the message is null
        if (this.chatStyle == null) {
            return false
        }

//        If the chat style is empty
        else if (this.chatStyle.isEmpty) {
            return false
        }

//        If the chat has no click event
        else if (this.chatStyle.chatHoverEvent == null) {
            return false
        }

//        If the chat has no click event value
        else if (this.chatStyle.chatHoverEvent.value == null) {
            return false
        }

//        If the chat has no value for the action
            else if (this.chatStyle.chatHoverEvent.value.unformattedText.isEmpty()) {
            return false
        }

        return true
    }

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

    fun IChatComponent.extractUrls(): List<String> {
        return extractUrls(this.unformattedText.removeColorCodes())
    }

//    Returns if we interact with chat at all
//    ADD A CHECK FOR ANY FEATURE THAT MODIFIES AN EXISTING CHAT MESSAGE
    private fun doModifyChatEnabled(): Boolean {
        val config = PartlySaneSkies.config

        if (config.colorCoopChat) {
            return true
        }

        else if (config.colorGuildChat) {
            return true
        }

        else if (config.colorOfficerChat) {
            return true
        }

        else if (config.colorPartyChat) {
            return true
        }

        else if (config.colorNonMessages) {
            return true
        }

        else if (config.colorPrivateMessages) {
            return true
        }

        else if (ChatAlertsManager.getChatAlertCount() != 0) {
            return true
        }

        else if (WordEditor.wordsToEdit.isNotEmpty() && PartlySaneSkies.config.wordEditor) {
            return true
        }

        else if (config.owoLanguage){
            return true
        }

        return false
    }

    //ALSO HERE, DONT FORGET
    private fun IChatComponent.doChatMessageModify(): Boolean {
        if (this.formattedText.startsWith("{\"server\":")) {
            return false
        }
        if (this.formattedText.startsWith(PartlySaneSkies.CHAT_PREFIX)) {
            return false
        }
        if (ChatColors.getChatColor(ChatColors.getPrefix(this.formattedText)).isNotEmpty()) {
            return true
        }
        else if (!ChatAlertsManager.checkChatAlert(this).formattedText.equals(this.formattedText)) {
            return true
        }
        else if (!ChatColors.detectNonMessage(this).formattedText.equals(this.formattedText)) {
            return true
        }
        else if (WordEditor.shouldEditMessage(this)){
            return true
        }
        else if(PartlySaneSkies.config.owoLanguage){
            return true //there is almost no way this will never not trigger
        }
        else {
            return false
        }
    }
}