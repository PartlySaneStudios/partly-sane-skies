//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.events.data

import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.events.minecraft.player.PlayerBreakBlockEvent
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import org.apache.logging.log4j.Level
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class LoadPublicDataEvent() {
    companion object {
        private fun callEvent(functions: List<EventManager.EventFunction>) {
            Thread() {
                for (function in functions) {
                    try {
                        SystemUtils.log(Level.INFO, "Loading ${function.obj.javaClass.name} ${function.function.name}")
                        val event = LoadPublicDataEvent()
                        function.function.call(function.obj, event)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {

                    }
                }
            }.start()
        }

        internal fun onDataLoad() {
            callEvent(EventManager.registeredFunctions[LoadPublicDataEvent::class] ?: ArrayList())
        }
    }
}
