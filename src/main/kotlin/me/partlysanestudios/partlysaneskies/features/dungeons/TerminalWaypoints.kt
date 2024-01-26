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
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getAllPlayersInWorld
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d.Companion.toPoint3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Range3d
import net.minecraft.entity.Entity
import org.apache.logging.log4j.Level.*
import java.awt.Color

object TerminalWaypoints {

    private val boundingBoxes = arrayOf(
        Range3d(118.0, 103.0, 27.0, 90.0, 157.0,124.0),
        Range3d(118.0, 103.0, 145.0, 19.0, 157.0, 124.0),
        Range3d(-4.0, 103.0, 145.0, 19.0, 157.0, 50.0),
        Range3d(-3.0, 103.0, 26.0, 92.0, 157.0, 53.0)
    )
    private val cachedPuzzles = ArrayList<F7Puzzle>()

    fun logCachedPuzzles() {
        log(INFO, cachedPuzzles.toString())
    }

    @SubscribePSSEvent
    fun onWaypointRender(event: RenderWaypointEvent) {
        if (!config.terminalWaypoints) {
            return
        }
        if (!IslandType.CATACOMBS.onIsland()) {
            return
        }
        if (DebugKey.isDebugMode()) {
            return
        }
        val playerPoint3d = Point3d.atPlayer()
        val playerBoundingBox = getCurrentBoundingBox(playerPoint3d)

        updateAllPuzzles()


        for (puzzle in cachedPuzzles) {
            if (playerBoundingBox?.isInRange(puzzle.pos) != true) {
                continue
            }
            val waypoint = if (puzzle.active) { // Active
                Waypoint(
                    puzzle.type.displayName,
                    puzzle.pos.toBlockPosInt(),
                    fillColor = Color.green.applyOpacity(50),
                    outlineColor = Color.green.applyOpacity(125)
                )
            } else if (puzzle.isPlayerNearby()) { // Inactive but someone nearby
                Waypoint(
                    puzzle.type.displayName,
                    puzzle.pos.toBlockPosInt(),
                    fillColor = Color.orange.applyOpacity(50),
                    outlineColor = Color.orange.applyOpacity(125)
                )
            } else { // Inactive
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


    @SubscribePSSEvent
    fun onDungeonStart(event: DungeonStartEvent) { // Yes I wrote the entire event system just so that I wouldn't have to call a chat event here
        for (terminal in cachedPuzzles) {
            terminal.active = false
        }
    }

    private fun findPuzzle(point3d: Point3d): F7Puzzle? {
        val pos = Point3d(point3d.toBlockPosInt())

        for (puzzle in cachedPuzzles) {
            if (puzzle.pos.distanceTo(pos) < 4) {
                return puzzle
            }
        }
        return null
    }

    private fun updateAllPuzzles() {
        val armorStands = getAllPuzzleStands()

        for (standPair in armorStands) {
            val type = standPair.value
            val stand = standPair.key
            val standName = stand.displayName.unformattedText

            val active = !standName.contains(type.inactiveArmorStandName)
            val point = stand.position.toPoint3d()

            val foundPuzzle = findPuzzle(point)
            if (foundPuzzle != null) {
                foundPuzzle.active = active
            } else {
                val puzzle = F7Puzzle(Point3d(point.toBlockPosInt()), type)
                puzzle.active = active
                cachedPuzzles.add(puzzle)
            }
        }
    }

    private fun getAllPuzzleStands(): Map<Entity, F7Puzzle.Type> {
        val armorStands = getAllArmorStands()
        val puzzles = HashMap<Entity, F7Puzzle.Type>()

        for (armorStand in armorStands) {
            val name = armorStand.displayName.unformattedText
            for (type in F7Puzzle.Type.entries) {
                if (!(name.contains(type.inactiveArmorStandName) || name.contains(type.activeArmorStandName))) {
                    continue
                }

                puzzles[armorStand] = type
            }
        }

        return puzzles
    }



    private fun getCurrentBoundingBox(point3d: Point3d): Range3d? {
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

        fun isPlayerNearby(): Boolean {
            val players = getAllPlayersInWorld()

            for (player in players) {
                if (player.position.toPoint3d().distanceTo(pos) <= 5) {
                    return true
                }
            }

            return false
        }

        override fun toString(): String {
            return "F7Puzzle(pos=$pos, type=$type, active=$active)"
        }
    }
}