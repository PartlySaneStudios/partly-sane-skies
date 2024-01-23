package me.partlysanestudios.partlysaneskies.events.skyblock.dungeons

import kotlin.reflect.KFunction

class DungeonEndEvent {
    companion object {
        internal fun onMessageRecieved(functionList: List<KFunction<*>>, formattedMessage: String) {
            if (formattedMessage.contains("§r§c☠ §r§eDefeated §r")) {
                for (function in functionList) {
                    function.call(DungeonEndEvent())
                }
            }
        }
    }
}