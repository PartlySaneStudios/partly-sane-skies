package me.partlysanestudios.partlysaneskies.renderers.waypoint

import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import net.minecraft.command.ICommandSender
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object WaypointManager {
    private val waypoints: MutableList<Waypoint> = mutableListOf()

    fun addWaypoint(waypoint: Waypoint) {
        if (waypoints.any { it.position == waypoint.position }) {
            ChatUtils.sendClientMessage("Waypoint already exists at that position, overwriting...")
            removeWaypoint(waypoints.first { it.position == waypoint.position })
        }
        waypoints.add(waypoint)
        ChatUtils.sendClientMessage("Added waypoint: ${waypoint.name}")
    }

    fun removeWaypoint(waypoint: Waypoint) {
        waypoints.remove(waypoint)
        ChatUtils.sendClientMessage("Removed waypoint: ${waypoint.name}")
    }

    fun getWaypoints(): List<Waypoint> {
        return waypoints.toList()
    }

    fun registerCommand() {
        PSSCommand("pssclearwaypoints")
            .setDescription("Clears all waypoints")
            .setRunnable { s: ICommandSender, a: Array<String> ->
                waypoints.clear()
                ChatUtils.sendClientMessage("Cleared all waypoints")
            }
            .register()
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        waypoints.clear()
    }
}