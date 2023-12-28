package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.config.oneconfig.OneConfigScreen
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Matcher
import java.util.regex.Pattern

object AutoGG {
    @SubscribeEvent
    fun handleChatEvent(event: ClientChatReceivedEvent) {
        if (!PartlySaneSkies.config.autoGgEnabled) {
            return
        }
        if (event.message.formattedText.contains("§r§fTeam Score:")) {
            Thread() {
                Thread.sleep((PartlySaneSkies.config.autoGGCooldown * 1000).toLong())
                val input = event.message.unformattedText
                val regex = "\\((.*?)\\)"

                val pattern: Pattern = Pattern.compile(regex)
                val matcher: Matcher = pattern.matcher(input)

                if (matcher.find()) {
                    val score: String = matcher.group(1)
                    if (score.equals("S+")) {
                        val message = if (PartlySaneSkies.config.sendAutoGGInWhatChat == 0) {
                            "/pc" + PartlySaneSkies.config.autoGGMessageSPlus
                        } else {
                            "/ac" + PartlySaneSkies.config.autoGGMessageSPlus
                        }
                        PartlySaneSkies.minecraft.thePlayer.sendChatMessage(message)

                    } else if (score.equals("S")) {
                        val message = if (PartlySaneSkies.config.sendAutoGGInWhatChat == 0) {
                            "/pc" + PartlySaneSkies.config.autoGGMessageS
                        } else {
                            "/ac" + PartlySaneSkies.config.autoGGMessageS
                        }
                        PartlySaneSkies.minecraft.thePlayer.sendChatMessage(message)

                    } else {
                        val message = if (PartlySaneSkies.config.sendAutoGGInWhatChat == 0) {
                            "/pc" + PartlySaneSkies.config.autoGGMessageOther
                        } else {
                            "/ac" + PartlySaneSkies.config.autoGGMessageOther
                        }
                        PartlySaneSkies.minecraft.thePlayer.sendChatMessage(message)

                    }
                }
            }
        }
    }
}