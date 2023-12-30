package me.partlysanestudios.partlysaneskies.renderers.waypoint

import me.partlysanestudios.partlysaneskies.utils.ChatUtils

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
}