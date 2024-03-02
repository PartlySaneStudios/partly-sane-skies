//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.render

import me.partlysanestudios.partlysaneskies.utils.vectors.Point2d
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Range3d
import net.minecraft.client.renderer.WorldRenderer
import java.lang.IllegalArgumentException

object RenderPrimitives {

    fun WorldRenderer.drawDiagonalFaceFill(range: Range3d, axisOfRotation: Axis) {
        val p1 = range.points[0]
        val p2 = range.points[1]
        val p3: Point3d
        val p4: Point3d
        when (axisOfRotation) {
            Axis.Y_AXIS -> {
                p3 = Point3d(p1.x, p2.y, p1.z)
                p4 = Point3d(p2.x, p1.y, p2.z)
            }
            Axis.X_AXIS -> {
                p3 = Point3d(p1.x, p2.y, p2.z)
                p4 = Point3d(p2.x, p1.y, p1.z)
            }
            Axis.Z_AXIS -> {
                p3 = Point3d(p2.x, p2.y, p1.z)
                p4 = Point3d(p1.x, p1.y, p2.z)
            }
        }
        // Add vertices for the face
        this.pos(p1.x, p1.y, p1.z).endVertex() // bottom corner
        this.pos(p3.x, p3.y, p3.z).endVertex() // top left
        this.pos(p2.x, p2.y, p2.z).endVertex() // top right
        this.pos(p4.x, p4.y, p4.z).endVertex() // bottom right
    }


    /**
     * Renders a flat face on the plane of the given axis
     * @param p1 a two-dimensional point where x will be the first point in the order (x, y, z)
     * @param p2 a two-dimensional point where x will be the first point in the order (x, y, z)
     * @param axis the plane that the face will be parallel to
     * @param constantDimension the non changing dimension (for the x plane, z stays constant; for the y plane, y stays constant; for the z plane, x stays constant)
     */
    fun WorldRenderer.drawPerpendicularFaceFill(p1: Point2d, p2: Point2d, axis: Axis, constantDimension: Double) {
//        If the plane is on the x plane, then the z stays constant
        when (axis) {
            Axis.X_AXIS -> {
                val (x1, x2) = listOf(p1.x, p2.x).sorted()
                val (y1, y2) = listOf(p1.y, p2.y).sorted()

                this.pos(x1, y1, constantDimension).endVertex()
                this.pos(x2, y1, constantDimension).endVertex()
                this.pos(x2, y2, constantDimension).endVertex()
                this.pos(x1, y2, constantDimension).endVertex()
            }

            //        If the plane is on the y plane, then the y stays constant
            Axis.Y_AXIS -> {
                val (x1, x2) = listOf(p1.x, p2.x).sorted()
                val (y1, y2) = listOf(p1.y, p2.y).sorted()

                this.pos(x1, constantDimension, y1).endVertex()
                this.pos(x2, constantDimension, y1).endVertex()
                this.pos(x2, constantDimension, y2).endVertex()
                this.pos(x1, constantDimension, y2).endVertex()
            }

            //        If the plane is on the z plane, then the x stays constant
            Axis.Z_AXIS -> {
                val (x1, x2) = listOf(p1.x, p2.x).sorted()
                val (y1, y2) = listOf(p1.y, p2.y).sorted()

                this.pos(constantDimension, x1, y1).endVertex()
                this.pos(constantDimension, x2, y1).endVertex()
                this.pos(constantDimension, x2, y2).endVertex()
                this.pos(constantDimension, x1, y2).endVertex()
            }
        }
    }

    /**
     * Renders an outline of a flat face on the plane of the given axis
     * @param p1 a two-dimensional point where x will be the first point in the order (x, y, z)
     * @param p2 a two-dimensional point where x will be the first point in the order (x, y, z)
     * @param axis the plane that the face will be parallel to
     * @param constantDimension the non changing dimension (for the x plane, z stays constant; for the y plane, y stays constant; for the z plane, x stays constant)
     */
    fun WorldRenderer.drawPerpendicularFaceOutline(p1: Point2d, p2: Point2d, axis: Axis, constantDimension: Double) {
        //        If the plane is on the x plane, then the z stays constant
        when (axis) {
            Axis.X_AXIS -> {
                val (x1, x2) = listOf(p1.x, p2.x).sorted()
                val (y1, y2) = listOf(p1.y, p2.y).sorted()

                this.pos(x1, y1, constantDimension).endVertex()
                this.pos(x2, y1, constantDimension).endVertex()

                this.pos(x1, y1, constantDimension).endVertex()
                this.pos(x1, y2, constantDimension).endVertex()

                this.pos(x2, y1, constantDimension).endVertex()
                this.pos(x2, y2, constantDimension).endVertex()

                this.pos(x1, y2, constantDimension).endVertex()
                this.pos(x2, y2, constantDimension).endVertex()
            }

            //        If the plane is on the y plane, then the y stays constant
            Axis.Y_AXIS -> {
                val (x1, x2) = listOf(p1.x, p2.x).sorted()
                val (y1, y2) = listOf(p1.y, p2.y).sorted()

                this.pos(x1, constantDimension, y1).endVertex()
                this.pos(x2, constantDimension, y1).endVertex()

                this.pos(x1, constantDimension, y1).endVertex()
                this.pos(x1, constantDimension, y2).endVertex()

                this.pos(x2, constantDimension, y1).endVertex()
                this.pos(x2, constantDimension, y2).endVertex()

                this.pos(x1, constantDimension, y2).endVertex()
                this.pos(x2, constantDimension, y2).endVertex()
            }

            //        If the plane is on the z plane, then the x stays constant
            Axis.Z_AXIS -> {
                val (x1, x2) = listOf(p1.x, p2.x).sorted()
                val (y1, y2) = listOf(p1.y, p2.y).sorted()


                this.pos(constantDimension, x1, y1).endVertex()
                this.pos(constantDimension, x2, y1).endVertex()

                this.pos(constantDimension, x1, y1).endVertex()
                this.pos(constantDimension, x1, y2).endVertex()

                this.pos(constantDimension, x2, y1).endVertex()
                this.pos(constantDimension, x2, y2).endVertex()

                this.pos(constantDimension, x1, y2).endVertex()
                this.pos(constantDimension, x2, y2).endVertex()
            }
        }
    }

    /**
     * Draws a cube with outlines
     *
     * @param p1 One corner of the box to draw
     * @param p2 Opposite corner of the box to draw
     */
    fun WorldRenderer.drawBoxOutline(p1: Point3d, p2: Point3d) {
        val (x1, x2) = listOf(p1.x, p2.x).sorted()
        val (y1, y2) = listOf(p1.y, p2.y).sorted()
        val (z1, z2) = listOf(p1.z, p2.z).sorted()

//        x face front (z is constant)
        this.drawPerpendicularFaceOutline(Point2d(x1, y1), Point2d(x2, y2), Axis.X_AXIS, z1)
//        x face back (z is constant)
        this.drawPerpendicularFaceOutline(Point2d(x1, y1), Point2d(x2, y2), Axis.X_AXIS, z2)
//        y face front (y is constant)
        this.drawPerpendicularFaceOutline(Point2d(x1, z1), Point2d(x2, z2), Axis.Y_AXIS, y1)
//        y face back (y is constant)
        this.drawPerpendicularFaceOutline(Point2d(x1, z1), Point2d(x2, z2), Axis.Y_AXIS, y2)
//        z face front (x is constant)
        this.drawPerpendicularFaceOutline(Point2d(y1, z1), Point2d(y2, z2), Axis.Z_AXIS, x1)
//        z face back (x is constant)
        this.drawPerpendicularFaceOutline(Point2d(y1, z1), Point2d(y2, z2), Axis.Z_AXIS, x2)
    }


    /**
     * Draws a cube's faces
     *
     * @param p1 One corner of the box to draw
     * @param p2 Opposite corner of the box to draw
     */
    fun WorldRenderer.drawBoxFill(p1: Point3d, p2: Point3d) {
        val (x1, x2) = listOf(p1.x, p2.x).sorted()
        val (y1, y2) = listOf(p1.y, p2.y).sorted()
        val (z1, z2) = listOf(p1.z, p2.z).sorted()

//        x face front (z is constant)
        this.drawPerpendicularFaceFill(Point2d(x1, y1), Point2d(x2, y2), Axis.X_AXIS, z1)
//        x face back (z is constant)
        this.drawPerpendicularFaceFill(Point2d(x1, y1), Point2d(x2, y2), Axis.X_AXIS, z2)
//        y face front (y is constant)
        this.drawPerpendicularFaceFill(Point2d(x1, z1), Point2d(x2, z2), Axis.Y_AXIS, y1)
//        y face back (y is constant)
        this.drawPerpendicularFaceFill(Point2d(x1, z1), Point2d(x2, z2), Axis.Y_AXIS, y2)
//        x face front (x is constant)
        this.drawPerpendicularFaceFill(Point2d(y1, z1), Point2d(y2, z2), Axis.Z_AXIS, x1)
//        x face back (x is constant)
        this.drawPerpendicularFaceFill(Point2d(y1, z1), Point2d(y2, z2), Axis.Z_AXIS, x2)
    }


    enum class Axis {
        X_AXIS,
        Y_AXIS,
        Z_AXIS
    }

}