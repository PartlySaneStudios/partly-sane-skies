//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.render

import me.partlysanestudios.partlysaneskies.render.RenderPrimitives.drawDiagonalFaceFill
import me.partlysanestudios.partlysaneskies.utils.geometry.orientation.Angle.Companion.cos
import me.partlysanestudios.partlysaneskies.utils.geometry.orientation.Angle.Companion.sin
import me.partlysanestudios.partlysaneskies.utils.geometry.orientation.Angle.Companion.toAngleFromDegrees
import me.partlysanestudios.partlysaneskies.utils.geometry.orientation.Axis
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point2d
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Range3d
import net.minecraft.client.renderer.WorldRenderer

object RenderEuclid {
    /**
     * @param baseCenter The center of the base of the cylinder
     * @param height The height of the cylinder
     * @param radius The radius of the cylinder
     * @param numOfSides Because this is minecraft, the cylinder is an approximation using a regular polygons. Because of this, the function should really be called "drawRegularPolygonalPrism"
     */
    fun WorldRenderer.drawCylinderFill(
        baseCenter: Point3d,
        radius: Double,
        height: Double,
        numOfSides: Int = 48,
    ) {
        val pointPairs: ArrayList<Range3d> = ArrayList()

        var lastX =
            calculatePoint(
                radius,
                numOfSides,
                0,
            ).x + baseCenter.x // Set the first x = (cos(angle) * r = 0) + the center of the circle
        var lastZ =
            calculatePoint(
                radius,
                numOfSides,
                0,
            ).y + baseCenter.z // Set the first z = (sin(angle) * r = r) + the center of the circle
        val firstX = lastX
        val firstZ = lastZ
        // Go around the circle, connecting the previous side to the next side
        for (i in 1..numOfSides + 1) {
            val circlePoint = calculatePoint(radius, numOfSides, i)
            val point1 = Point3d(lastX, baseCenter.y, lastZ)
            val point2 = Point3d(circlePoint.x + baseCenter.x, baseCenter.y + height, circlePoint.y + baseCenter.z)

            val range = Range3d(point1, point2)

            pointPairs.add(range)

            lastX = point2.x
            lastZ = point2.z
        }
        // Connect the last point with the first point to complete the circle
        pointPairs.add(Range3d(Point3d(lastX, baseCenter.y, lastZ), Point3d(firstX, baseCenter.y + height, firstZ)))
        // Draw the sides
        for (pair in pointPairs) {
            this.drawDiagonalFaceFill(pair, Axis.Y_AXIS)
        }
    }

    /**
     * @param baseCenter The center of the base of the cylinder
     * @param height The height of the cylinder
     * @param radius The radius of the cylinder
     * @param numOfSides Because this is minecraft, the cylinder is an approximation using a regular polygons. Because of this, the function should really be called "drawRegularPolygonalPrism"
     */
    fun WorldRenderer.drawCylinderOutline(
        baseCenter: Point3d,
        radius: Double,
        height: Double,
        numOfSides: Int = 48,
    ) {
        val bottomPointPairs: ArrayList<Range3d> = ArrayList()
        val topPointPairs: ArrayList<Range3d> = ArrayList()

        var lastX =
            calculatePoint(
                radius,
                numOfSides,
                0,
            ).x + baseCenter.x // Set the first x = (cos(angle) * r = 0) + the center of the circle
        var lastZ =
            calculatePoint(
                radius,
                numOfSides,
                0,
            ).y + baseCenter.z // Set the first z = (sin(angle) * r = r) + the center of the circle
        val firstX = lastX
        val firstZ = lastZ
        // Go around the circle, connecting the previous side to the next side
        for (i in 1..numOfSides + 1) {
            val circlePoint = calculatePoint(radius, numOfSides, i)
            val bottomPoint1 = Point3d(lastX, baseCenter.y, lastZ)
            val bottomPoint2 = Point3d(circlePoint.x + baseCenter.x, baseCenter.y, circlePoint.y + baseCenter.z)
            val bottomRange = Range3d(bottomPoint1, bottomPoint2)

            val topPoint1 = Point3d(lastX, baseCenter.y + height, lastZ)
            val topPoint2 = Point3d(circlePoint.x + baseCenter.x, baseCenter.y + height, circlePoint.y + baseCenter.z)
            val topRange = Range3d(topPoint1, topPoint2)

            bottomPointPairs.add(bottomRange)
            topPointPairs.add(topRange)

            lastX = bottomPoint2.x
            lastZ = bottomPoint2.z
        }
        // Connect the last point with the first point to complete the circle
        bottomPointPairs.add(Range3d(Point3d(lastX, baseCenter.y, lastZ), Point3d(firstX, baseCenter.y, firstZ)))
        topPointPairs.add(
            Range3d(
                Point3d(lastX, baseCenter.y + height, lastZ),
                Point3d(firstX, baseCenter.y + height, firstZ),
            ),
        )

        // Draw the sides
        for (i in bottomPointPairs.indices) {
            val bottomPair = bottomPointPairs[i]
            val topPair = topPointPairs[i]

            this.pos(bottomPair.points[0].x, bottomPair.points[0].y, bottomPair.points[0].z).endVertex()
            this.pos(bottomPair.points[1].x, bottomPair.points[1].y, bottomPair.points[1].z).endVertex()

            this.pos(topPair.points[0].x, topPair.points[0].y, topPair.points[0].z).endVertex()
            this.pos(topPair.points[1].x, topPair.points[1].y, topPair.points[1].z).endVertex()
        }
    }

    private fun calculatePoint(
        radius: Double,
        numOfSides: Int,
        currentSide: Int,
    ): Point2d {
        val angle = ((360 / numOfSides) * currentSide).toAngleFromDegrees()
        val x = cos(angle) * radius
        val y = sin(angle) * radius
        return Point2d(x, y)
    }
}
