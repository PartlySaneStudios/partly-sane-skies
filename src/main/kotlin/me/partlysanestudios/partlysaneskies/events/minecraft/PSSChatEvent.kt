package me.partlysanestudios.partlysaneskies.events.minecraft

import me.partlysanestudios.partlysaneskies.events.EventManager
import net.minecraft.util.IChatComponent

class PSSChatEvent(val message: String, val component: IChatComponent) {
    companion object {
        internal fun onMessageReceived(functionList: List<EventManager.EventFunction>, component: IChatComponent) {
            functionList.forEach {
                try {
                    it.function.call(it.obj, PSSChatEvent(component.formattedText, component))
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
    }
}
