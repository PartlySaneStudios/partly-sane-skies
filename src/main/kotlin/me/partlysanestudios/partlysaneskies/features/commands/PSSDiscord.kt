//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.commands

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.discordCode
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

object PSSDiscord {
    fun registerDiscordCommand() {
        PSSCommand("pssdiscord")
            .addAlias("pssdisc", "pssd", "psdisc", "psdiscord")
            .setDescription("Join the Partly Sane Studios PSSDiscord Server")
            .setRunnable { _: ICommandSender?, _: Array<String> ->
                // Creates a new message with the correct text
                val message: IChatComponent = ChatComponentText(PartlySaneSkies.CHAT_PREFIX + "§9Join the discord: https://discord.gg/$discordCode")
                // Sets the text to be clickable with a link
                message.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL,"https://discord.gg/$discordCode"))
                // Prints message
                minecraft.ingameGUI.chatGUI.printChatMessage(message)
            }.register()
    }
}
