package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Matcher
import java.util.regex.Pattern

object AutoGG {
//    Checks if within 1 second after the dungeon has ended, the "Team Score" message was sent
    private var lastDungeonEndTime = 0L;

    @SubscribeEvent
    fun handleChatEvent(event: ClientChatReceivedEvent) {
        if (event.message.formattedText.contains("§r§c☠ §r§eDefeated §r")) {
            lastDungeonEndTime = PartlySaneSkies.getTime();
        }

        if (event.message.formattedText.contains("§r§fTeam Score: ") && MathUtils.onCooldown(lastDungeonEndTime, 1000)) {
            val input = event.message.unformattedText
            val regex = "\\((\\w)\\)"

            val pattern: Pattern = Pattern.compile(regex)
            val matcher: Matcher = pattern.matcher(input)

            if (matcher.find()) {
                val score: String = matcher.group(1)
                if (score.equals("S+")) {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage(PartlySaneSkies.config.autoGGMessageSPlus)

                } else if (score.equals("S")) {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage(PartlySaneSkies.config.autoGGMessageS)

                } else {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage(PartlySaneSkies.config.autoGGMessageOther)

                }
            }
        }
    }
}