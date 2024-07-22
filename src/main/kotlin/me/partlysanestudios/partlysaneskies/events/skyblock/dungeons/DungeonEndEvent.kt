package me.partlysanestudios.partlysaneskies.events.skyblock.dungeons

import me.partlysanestudios.partlysaneskies.events.EventManager

class DungeonEndEvent {
    companion object {
        internal fun onMessageReceived(functionList: List<EventManager.EventFunction>, formattedMessage: String) {
            if (formattedMessage.contains("§r§c☠ §r§eDefeated §r")) {
                for (function in functionList) {
                    try {
                        function.function.call(function.obj, DungeonEndEvent())
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }
            }
        }
    }
}
