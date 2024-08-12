package me.partlysanestudios.partlysaneskies.events.minecraft

import me.partlysanestudios.partlysaneskies.events.EventManager

class TablistUpdateEvent(val list: List<String>) {
    companion object {
        internal fun onUpdate(functionList: List<EventManager.EventFunction>, list: List<String>) {
            functionList.forEach {
                try {
                    it.function.call(it.obj, TablistUpdateEvent(list))
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
    }
}
