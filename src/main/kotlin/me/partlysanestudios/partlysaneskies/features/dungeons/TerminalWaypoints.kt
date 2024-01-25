//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.render.RenderWaypointEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey
import me.partlysanestudios.partlysaneskies.render.waypoint.Waypoint
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.applyOpacity
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getAllArmorStands
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Range3d
import java.awt.Color

object TerminalWaypoints {

    private val boundingBoxes = arrayOf(
        Range3d(118.0, 103.0, 27.0, 90.0, 157.0,124.0),
        Range3d(118.0, 103.0, 145.0, 19.0, 157.0, 118.0),
        Range3d(-4.0, 103.0, 145.0, 19.0, 157.0, 50.0),
        Range3d(-3.0, 103.0, 26.0, 92.0, 157.0, 53.0)
    )
    private val terminals = ArrayList<F7Puzzle>()

    @SubscribePSSEvent
    fun onWaypointRender(event: RenderWaypointEvent) {
        if (!config.terminalWaypoints)
        if (!IslandType.CATACOMBS.onIsland()) {
            return
        }
        if (DebugKey.isDebugMode()) {
            return
        }
        val playerPoint3d = Point3d.atPlayer()
        val playerBoundingBox = getCurrentBoundingBox(playerPoint3d)



        for (pointPair in getAllInactivePos()) {
            val point = pointPair.key
            val type = pointPair.value
            if (playerBoundingBox?.isInRange(point) != true) {
                continue
            }

            val puzzleFound = findPuzzle(point)
            if (puzzleFound == null) {
                val puzzle = F7Puzzle(Point3d(point.toBlockPosInt()), type)
                terminals.add(puzzle)
            } else {
                if (!puzzleFound.active) {
                    puzzleFound.active = false
                }
            }
        }

        for (pointPair in getAllActivePos()) {
            val point = pointPair.key
            val type = pointPair.value
            if (playerBoundingBox?.isInRange(point) != true) {
                continue
            }

            val puzzleFound = findPuzzle(point)
            if (puzzleFound == null) {
                val puzzle = F7Puzzle(Point3d(point.toBlockPosInt()), type)

                puzzle.active = true
                terminals.add(puzzle)
            } else {
                if (!puzzleFound.active) {
                    puzzleFound.active = true
                }
            }
        }

        for (puzzle in terminals) {
            if (playerBoundingBox?.isInRange(puzzle.pos) != true) {
                continue
            }
            val waypoint = if (puzzle.active) {
                Waypoint(
                    puzzle.type.displayName,
                    puzzle.pos.toBlockPosInt(),
                    fillColor = Color.green.applyOpacity(50),
                    outlineColor = Color.green.applyOpacity(125)
                )
            } else {
                Waypoint(
                    puzzle.type.displayName,
                    puzzle.pos.toBlockPosInt(),
                    fillColor = Color.red.applyOpacity(50),
                    outlineColor = Color.red.applyOpacity(125)
                )
            }

            event.pipeline.add(waypoint)
        }
    }

    fun getAllActivePos(): Map<Point3d, F7Puzzle.Type> {
        val armorStands = getAllArmorStands()
        val activeDevicePoses = HashMap<Point3d, F7Puzzle.Type>()

        for (armorStand in armorStands) {
            val name = armorStand.displayName.unformattedText
            for (type in F7Puzzle.Type.entries) {
                if (!name.contains(type.activeArmorStandName)) {
                    continue
                }

                val pos = Point3d(armorStand.posX, armorStand.posY + 1, armorStand.posZ)
                activeDevicePoses[pos] = type
            }
        }

        return activeDevicePoses
    }


    @SubscribePSSEvent
    fun onDungeonStart(event: DungeonStartEvent) { // Yes I wrote the entire event system just so that I wouldn't have to call a chat event here
        for (terminal in terminals) {
            terminal.active = false
        }
    }

    fun findPuzzle(point3d: Point3d): F7Puzzle? {
        val pos = Point3d(point3d.toBlockPosInt())

        for (puzzle in terminals) {
            if (puzzle.pos.distanceTo(pos) < 4) {
                return puzzle
            }
        }
        return null
    }

    fun getAllInactivePos(): Map<Point3d, F7Puzzle.Type> {
        val armorStands = getAllArmorStands()
        val inactiveDevicePoses = HashMap<Point3d, F7Puzzle.Type>()

        for (armorStand in armorStands) {
            val name = armorStand.displayName.unformattedText
            for (type in F7Puzzle.Type.entries) {
                if (!name.contains(type.inactiveArmorStandName)) {
                    continue
                }

                val pos = Point3d(armorStand.posX, armorStand.posY, armorStand.posZ)
                inactiveDevicePoses[pos] = type
            }
        }

        return inactiveDevicePoses
    }

    fun getCurrentBoundingBox(point3d: Point3d): Range3d? {
        for (boundingBox in boundingBoxes) {
            if (boundingBox.isInRange(point3d)) {
                return boundingBox
            }
        }
        return null
    }

    class F7Puzzle(val pos: Point3d, val type: Type) {
        var active = false
        enum class Type(val activeArmorStandName: String, val inactiveArmorStandName: String, val  displayName: String) {
            TERMINAL("Active Terminal", "Inactive Terminal", "Terminal"),
            LEVER("Activated", "Not Activated", "Lever"),
            DEVICE("Active", "Inactive", "Device")
        }
    }
}