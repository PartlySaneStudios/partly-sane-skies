package me.partlysanestudios.partlysaneskies.events.data

import me.partlysanestudios.partlysaneskies.events.EventManager
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
                        SystemUtils.log(Level.INFO, "Loading ${function.function.javaClass.name} ${function.function.name}")
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
            val onPSSDataReset = ArrayList<EventManager.EventFunction>()

            for (function in EventManager.registeredFunctions) {
                val paramClass = function.function.parameters[1].type.classifier as? KClass<*>

                if (paramClass?.isSubclassOf(LoadPublicDataEvent::class) == true) {
                    onPSSDataReset.add(function)
                }
            }
            callEvent(onPSSDataReset)
        }
    }
}
