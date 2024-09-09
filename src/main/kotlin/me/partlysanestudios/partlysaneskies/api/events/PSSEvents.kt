//
// Written by ThatGravyBoat.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.api.events

import org.apache.logging.log4j.LogManager
import java.lang.reflect.Method

object PSSEvents {

    private val handlers: MutableMap<Class<*>, EventHandler<*>> = mutableMapOf()
    private val logger = LogManager.getLogger("Partly Sane Skies Events")

    @Suppress("UNCHECKED_CAST")
    private fun <T : PSSEvent> getHandler(event: Class<T>): EventHandler<T> {
        return handlers.getOrPut(event) { EventHandler<T>(logger) } as EventHandler<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun register(method: Method, instance: Any): SubscribeResponse? {
        val subscribe = method.getAnnotation(PSSEvent.Subscribe::class.java) ?: return null
        if (method.parameterCount == 0) return SubscribeResponse.NO_PARAMETERS
        if (method.parameterCount > 1) return SubscribeResponse.TOO_MANY_PARAMETERS
        val eventClass = method.parameterTypes[0]
        if (!PSSEvent::class.java.isAssignableFrom(eventClass)) return SubscribeResponse.PARAMETER_NOT_EVENT
        getHandler(eventClass as Class<PSSEvent>).register(method, instance, subscribe)
        return SubscribeResponse.SUCCESS
    }

    fun register(instance: Any) {
        var hadEvent = false
        instance.javaClass.declaredMethods.forEach { method ->
            val response = register(method, instance) ?: return@forEach
            hadEvent = true
            when (response) {
                SubscribeResponse.NO_PARAMETERS, SubscribeResponse.TOO_MANY_PARAMETERS -> {
                    logger.warn("Event subscription on ${method.name} has an incorrect number of parameters (${method.parameterCount})")
                }
                SubscribeResponse.PARAMETER_NOT_EVENT -> {
                    logger.warn("Event subscription on ${method.name} does not have a parameter that extends ${PSSEvent::class.java.name}")
                }
                SubscribeResponse.SUCCESS -> {}
            }
        }
        if (!hadEvent) {
            logger.warn("No events found in ${instance.javaClass.name}")
        }
    }

    fun post(event: PSSEvent) {
        getHandler(event.javaClass).post(event)
    }

    private enum class SubscribeResponse {
        NO_PARAMETERS,
        TOO_MANY_PARAMETERS,
        PARAMETER_NOT_EVENT,
        SUCCESS
    }
}
