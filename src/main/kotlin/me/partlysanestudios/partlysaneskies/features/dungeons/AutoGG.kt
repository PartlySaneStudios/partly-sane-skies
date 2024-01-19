//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Matcher
import java.util.regex.Pattern

object AutoGG {
    @SubscribeEvent
    fun handleChatEvent(event: ClientChatReceivedEvent) {
        if (!PartlySaneSkies.config.dungeons.autoGgEnabled) {
            return
        }
        if (event.message.formattedText.contains("§r§fTeam Score:")) {
            Thread({
                Thread.sleep((PartlySaneSkies.config.dungeons.autoGGCooldown * 1000).toLong())
                val input = event.message.unformattedText
                val regex = "\\((.*?)\\)"

                val pattern: Pattern = Pattern.compile(regex)
                val matcher: Matcher = pattern.matcher(input)

                if (matcher.find()) {
                    val score: String = matcher.group(1)
                    if (score.equals("S+")) {
                        val message = if (PartlySaneSkies.config.dungeons.sendAutoGGInWhatChat == 0) {
                            "/pc" + PartlySaneSkies.config.dungeons.autoGGMessageSPlus
                        } else {
                            "/ac" + PartlySaneSkies.config.dungeons.autoGGMessageSPlus
                        }
                        PartlySaneSkies.minecraft.thePlayer.sendChatMessage(message)

                    } else if (score.equals("S")) {
                        val message = if (PartlySaneSkies.config.dungeons.sendAutoGGInWhatChat == 0) {
                            "/pc" + PartlySaneSkies.config.dungeons.autoGGMessageS
                        } else {
                            "/ac" + PartlySaneSkies.config.dungeons.autoGGMessageS
                        }
                        PartlySaneSkies.minecraft.thePlayer.sendChatMessage(message)

                    } else {
                        val message = if (PartlySaneSkies.config.dungeons.sendAutoGGInWhatChat == 0) {
                            "/pc" + PartlySaneSkies.config.dungeons.autoGGMessageOther
                        } else {
                            "/ac" + PartlySaneSkies.config.dungeons.autoGGMessageOther
                        }
                        PartlySaneSkies.minecraft.thePlayer.sendChatMessage(message)

                    }
                }
            },"AutoGG wait").start()
        }
    }
}