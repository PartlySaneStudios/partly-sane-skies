package me.partlysanestudios.partlysaneskies.api.events

import org.apache.logging.log4j.Logger
import java.lang.invoke.LambdaMetafactory
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.reflect.Method
import java.util.function.Consumer

class EventHandler<T : PssEvent>(private val logger: Logger) {

    private val listeners: MutableList<Listener> = mutableListOf()
    private var lastCancellableIndex: Int = -1

    fun register(method: Method, instance: Any, options: PssEvent.Subscribe) {
        listeners.add(Listener(options, createEventConsumer(instance, method)))
        if (options.receiveCancelled) lastCancellableIndex = listeners.size - 1
    }

    @Suppress("UNCHECKED_CAST")
    private fun createEventConsumer(instance: Any, method: Method): Consumer<Any> {
        try {
            val handle = MethodHandles.lookup().unreflect(method)
            return LambdaMetafactory.metafactory(
                MethodHandles.lookup(),
                "accept",
                MethodType.methodType(Consumer::class.java, instance::class.java),
                MethodType.methodType(Nothing::class.javaPrimitiveType, Object::class.java),
                handle,
                MethodType.methodType(Nothing::class.javaPrimitiveType, method.parameterTypes[0])
            ).target.bindTo(instance).invokeExact() as Consumer<Any>
        } catch (e: Throwable) {
            throw IllegalArgumentException("Method ${method.name} is not a valid event consumer", e)
        }
    }

    fun post(
        event: T,
        errorHandler: (Throwable) -> Boolean = {
            logger.error("Error occurred while handling event", it)
            true
        }
    ) {
        for ((index, listener) in listeners.withIndex()) {
            if (event.isCancelled) {
                if (index >= lastCancellableIndex) break
                else if (!listener.options.receiveCancelled) continue
            }
            try {
                listener.consumer.accept(event)
            } catch (e: Throwable) {
                if (!errorHandler(e)) throw e
            }
        }

    }

    private data class Listener(val options: PssEvent.Subscribe, val consumer: Consumer<Any>)
}
