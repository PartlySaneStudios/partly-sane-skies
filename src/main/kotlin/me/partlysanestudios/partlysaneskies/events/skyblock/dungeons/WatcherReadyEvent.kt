//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events.skyblock.dungeons

import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes

class WatcherReadyEvent {
    companion object {
        internal fun onMessageRecieved(functionList: List<EventManager.EventFunction>, formattedMessage: String) {
            if (formattedMessage.removeColorCodes().startsWith("[BOSS] The Watcher: That will be enough for now.") && IslandType.CATACOMBS.onIsland()) {
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