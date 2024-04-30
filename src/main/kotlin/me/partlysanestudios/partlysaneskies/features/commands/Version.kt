//
// Written by j10a1n15 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.commands

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.isLatestVersion
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText

object Version {
    fun registerVersionCommand() {
        PSSCommand("partlysaneskiesversion")
            .addAlias("pssversion")
            .addAlias("pssv")
            .addAlias("partlysaneskiesv")
            .setDescription("Prints the version of Partly Sane Skies you are using")
            .setRunnable { _: ICommandSender?, _: Array<String?>? ->
                val chatcomponent = ChatComponentText(
                    """§b§m-----------------------------------------------------§0
                    §b§lPartly Sane Skies Version:
                    §e${PartlySaneSkies.VERSION}${if (isLatestVersion) "\n§aYou are using the latest version of Partly Sane Skies!" else "\n§cYou are not using the latest version of Partly Sane Skies! Click here to download the newest version!"}
                    §b§m-----------------------------------------------------§0""".trimIndent()
                )
                if (!isLatestVersion) {
                    chatcomponent.chatStyle.setChatClickEvent(
                        ClickEvent(
                            ClickEvent.Action.OPEN_URL,
                            "https://github.com/PartlySaneStudios/partly-sane-skies/releases"
                        )
                    )
                }
                sendClientMessage(chatcomponent)
            }.register()
    }
}
