//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events.skyblock.dungeons

import kotlin.reflect.KFunction

class DungeonStartEvent {
    companion object {
        internal fun onMessageRecieved(functionList: List<KFunction<*>>, formattedMessage: String) {
            if (formattedMessage.contains("[NPC] §bMort§f: Here, I found this map when I first entered")) {
                for (function in functionList) {
                    function.call(DungeonStartEvent())
                }
            }
        }
    }
}