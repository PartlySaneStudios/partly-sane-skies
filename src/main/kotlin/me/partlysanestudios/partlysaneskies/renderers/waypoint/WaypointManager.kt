package me.partlysanestudios.partlysaneskies.renderers.waypoint

import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import net.minecraft.command.ICommandSender

object WaypointManager {
    private val waypoints: MutableList<Waypoint> = mutableListOf()

    fun addWaypoint(waypoint: Waypoint) {
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
}