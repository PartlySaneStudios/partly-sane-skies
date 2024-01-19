package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.features.farming.endoffarmnotifer.Range3d
import me.partlysanestudios.partlysaneskies.render.points.Point3d
import me.partlysanestudios.partlysaneskies.render.waypoint.Waypoint
import me.partlysanestudios.partlysaneskies.render.waypoint.WaypointEvent
import me.partlysanestudios.partlysaneskies.render.waypoint.WaypointRenderPipeline
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getAllArmorStands
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import org.apache.logging.log4j.Level
import java.awt.Color

object TerminalWaypoints {

    private val boundingBoxes = arrayOf(
        Range3d(42.0, 103.0, 27.0, 90.0, 157.0,124.0),
        Range3d(118.0, 103.0, 145.0, 36.0, 157.0, 118.0),
        Range3d(-4.0, 103.0, 145.0, 19.0, 157.0, 50.0),
        Range3d(-3.0, 103.0, 26.0, 92.0, 157.0, 53.0)
    )

    @WaypointEvent
    fun onWaypointRender(pipeline: WaypointRenderPipeline) {
        val playerPoint3d = Point3d(minecraft.thePlayer?.posX ?: -1.0, minecraft.thePlayer?.posY ?: -1.0, minecraft.thePlayer?.posZ ?: -1.0)
        val playerBoundingBox = getCurrentBoundingBox(playerPoint3d)

        val waypointsToRender = ArrayList<Waypoint>()

        for (point in getAllActivePos()) {
            log(Level.INFO, "$point")

            waypointsToRender.add(Waypoint("Active", point.toBlockPosInt(), fillColor = Color.green, outlineColor = Color.green))

        }

        for (point in getAllInactivePos()) {
            log(Level.INFO, "$point")
            waypointsToRender.add(Waypoint("Inactive", point.toBlockPosInt(), fillColor = Color.red, outlineColor = Color.red))
        }

        for (waypoint in waypointsToRender) {
            pipeline.add(waypoint)
        }
    }

    fun getAllActivePos(): List<Point3d> {
        val armorStands = getAllArmorStands()

        val activeDevicePoses = ArrayList<Point3d>()


        for (armorStand in armorStands) {
            if (armorStand.displayName.unformattedText.contains("Active") || armorStand.displayName.unformattedText.contains("Terminal Active")) {
                val pos = Point3d(armorStand.posX, armorStand.posY, armorStand.posZ)
                activeDevicePoses.add(pos)
            }
        }

        return activeDevicePoses
    }

    fun getAllInactivePos(): List<Point3d> {
        val armorStands = getAllArmorStands()

        val activeDevicePoses = ArrayList<Point3d>()


        for (armorStand in armorStands) {
            if (armorStand.displayName.unformattedText.contains("Inactive") || armorStand.displayName.unformattedText.contains("Inactive Terminal")) {
                val pos = Point3d(armorStand.posX, armorStand.posY, armorStand.posZ)
                activeDevicePoses.add(pos)
            }
        }

        return activeDevicePoses
    }

    fun getCurrentBoundingBox(point3d: Point3d): Range3d? {
        for (boundingBox in boundingBoxes) {
            if (boundingBox.isInRange(point3d)) {
                return boundingBox
            }
        }
        return null
    }



}