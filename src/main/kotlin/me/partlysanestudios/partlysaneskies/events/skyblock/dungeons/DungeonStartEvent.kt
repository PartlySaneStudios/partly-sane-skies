//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events.skyblock.dungeons

import me.partlysanestudios.partlysaneskies.events.EventManager

class DungeonStartEvent {
    companion object {
        internal fun onMessageRecieved(functionList: List<EventManager.EventFunction>, formattedMessage: String) {
            if (formattedMessage.contains("[NPC] §bMort§f: Here, I found this map when I first entered")) {
                for (function in functionList) {
                    try {
                        function.function.call(function.obj, DungeonStartEvent())
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }
            }
        }
    }
}