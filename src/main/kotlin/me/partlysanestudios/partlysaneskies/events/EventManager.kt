//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events

import me.partlysanestudios.partlysaneskies.events.minecraft.render.RenderWaypointEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonEndEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.RequiredSecretsFoundEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.WatcherReadyEvent
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.apache.logging.log4j.Level
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions

object EventManager {
    internal val registeredFunctions = HashMap<KClass<*>, ArrayList<EventFunction>>()

    fun register(obj: Any) {
        val kClass = obj::class // get the class
        for (function in kClass.memberFunctions) { // for each function in the class
            if (!function.hasAnnotation<SubscribePSSEvent>()) { // if the functions are not annotated, continue
                continue
            }

            val functionParameters = function.parameters
            if (functionParameters.size != 2) { // if there is not only 1 parameter (param 1 is always the instance parameter
                log(Level.WARN, "Unable to add ${function.name} due to incorrect number of function parameters (${functionParameters.size}")
                continue
            }
            val paramClass = functionParameters[1].type.classifier as? KClass<*> ?: continue

            if (!registeredFunctions.containsKey(paramClass)) {
                registeredFunctions[paramClass] = ArrayList()
            }
            registeredFunctions[paramClass]?.add(EventFunction(obj, function)) // adds the function to a list to call
            log(Level.INFO, "Registered ${function.name} from ${obj.javaClass.name} in PSS events")
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        RequiredSecretsFoundEvent.tick(registeredFunctions[RequiredSecretsFoundEvent::class] ?: ArrayList())
    }

    @SubscribeEvent
    fun onScreenRender(event: RenderWorldLastEvent) {
        RenderWaypointEvent.onEventCall(event.partialTicks, registeredFunctions[RenderWaypointEvent::class] ?: ArrayList())
    }

    @SubscribeEvent
    fun onChatRecievedEvent(event: ClientChatReceivedEvent) {
        val message = event.message.formattedText
        DungeonStartEvent.onMessageRecieved(registeredFunctions[DungeonStartEvent::class] ?: ArrayList(), message)
        DungeonEndEvent.onMessageRecieved(registeredFunctions[DungeonEndEvent::class] ?: ArrayList(), message)
        WatcherReadyEvent.onMessageRecieved(registeredFunctions[WatcherReadyEvent::class]?: ArrayList(), message)
    }

    internal class EventFunction(val obj: Any, val function: KFunction<*> )
}