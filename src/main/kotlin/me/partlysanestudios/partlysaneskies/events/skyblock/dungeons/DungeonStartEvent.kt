//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.events.skyblock.dungeons

import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes

class DungeonStartEvent {
    companion object {
        internal fun onMessageRecieved(
            functionList: List<EventManager.EventFunction>,
            formattedMessage: String,
        ) {
            val message = formattedMessage.removeColorCodes()
            if (message.contains("Starting in 1 second.") && IslandType.CATACOMBS.onIsland()) {
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
