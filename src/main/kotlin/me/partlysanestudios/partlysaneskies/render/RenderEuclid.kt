package me.partlysanestudios.partlysaneskies.render

import me.partlysanestudios.partlysaneskies.render.RenderEuclid.Angle.Companion.cos
import me.partlysanestudios.partlysaneskies.render.RenderEuclid.Angle.Companion.sin
import me.partlysanestudios.partlysaneskies.render.RenderEuclid.Angle.Companion.toAngleFromDegrees
import me.partlysanestudios.partlysaneskies.render.RenderPrimitives.drawBoxFill
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Range3d
import net.minecraft.client.renderer.WorldRenderer
import kotlin.math.PI

object RenderEuclid {

    /**
     * @param baseCenter The center of the base of the cylinder
     * @param height The height of the cylinder
     * @param radius The radius of the cylinder
     * @param numOfSides Because this is minecraft, the cylinder is an approximation using a regular polygons. Because of this, the function should really be called "drawRegularPolygonalPrism"
     */
    fun WorldRenderer.drawCylinderFill(baseCenter: Point3d, radius: Double, height: Double, numOfSides: Int = 8) {
        val interiorAngleDegrees = (360 / numOfSides).toAngleFromDegrees() // 🚨🚨🚨 please note this is in degrees. Sin and Cos functions use radians
        val sideLength = 2 * radius * cos(interiorAngleDegrees / 2)

        val pointPairs: Array<Range3d?> = arrayOfNulls(numOfSides + 1)


        var lastX = radius * cos((interiorAngleDegrees * 0)/numOfSides) + baseCenter.x
        var lastZ = radius * sin((interiorAngleDegrees * 0)/numOfSides) + baseCenter.z
        val firstX = lastX
        val firstZ = lastZ
        for (i in 1..<pointPairs.size) {
            val point1 = Point3d(lastX, baseCenter.y, lastZ)
            val point2 = Point3d(radius * cos((interiorAngleDegrees * i)/numOfSides) + baseCenter.x, baseCenter.y + height, radius * sin((interiorAngleDegrees * i)/numOfSides) + baseCenter.z)
            val range = Range3d(point1, point2)

            lastX = range.points[0].x
            lastZ = range.points[0].z
        }
        pointPairs[pointPairs.size - 1] = Range3d(Point3d(firstX, baseCenter.y, firstZ), Point3d(firstX, baseCenter.y + height, firstZ))


        for (pair in pointPairs) {
            pair?.points ?: continue
            this.drawBoxFill(pair.points[0], pair.points[1])
        }
    }

    class Angle(private val radians: Double) {
        companion object {
            /**
             * @return An angle object given a number of degrees
             */
            fun Number.toAngleFromDegrees(): Angle {
                return Angle((180 / PI) * this.toDouble())
            }

            /**
             * @return An angle object given a number of radians
             */
            fun Number.toAngleFromRadians(): Angle {
                return Angle(this.toDouble())
            }

            /**
             * @return The sine of an angle
             */
            fun sin(angle: Angle): Double {
                return kotlin.math.sin(angle.asRadians())
            }

            /**
             * @return The cosine of an angle
             */
            fun cos(angle: Angle): Double {
                return kotlin.math.cos(angle.asRadians())
            }

            /**
             * @return The tangent of an angle
             */
            fun tan(angle: Angle): Double {
                return kotlin.math.tan(angle.asRadians())
            }

            /**
             * @return The arc sine of an angle
             */
            fun asin(angle: Angle): Double {
                return kotlin.math.asin(angle.asRadians())
            }

            /**
             * @return The arc cosine of an angle
             */
            fun acos(angle: Angle): Double {
                return kotlin.math.acos(angle.asRadians())
            }

            /**
             * @return The arc tangent of an angle
             */
            fun atan(angle: Angle): Double {
                return kotlin.math.atan(angle.asRadians())
            }

        }


        /**
         * @return The number of degrees this angle represents
         */
        fun asDegrees(): Double {
            return (180/PI) * radians
        }

        /**
         * @return The number of radians this angle represents
         */
        fun asRadians(): Double {
            return radians
        }



        operator fun minus(i: Number): Angle {
            return Angle(radians - i.toDouble())
        }

        operator fun plus(i: Number): Angle {
            return Angle(radians + i.toDouble())
        }

        operator fun div(i: Number): Angle {
            return Angle(radians / i.toDouble())
        }

        operator fun times(i: Number): Angle {
            return Angle(radians * i.toDouble())
        }

        operator fun rem(i: Number): Angle {
            return Angle(radians % i.toDouble())
        }
    }
}