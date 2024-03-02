package me.partlysanestudios.partlysaneskies.render

import me.partlysanestudios.partlysaneskies.render.RenderEuclid.Angle.Companion.cos
import me.partlysanestudios.partlysaneskies.render.RenderEuclid.Angle.Companion.toAngleFromDegrees
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import net.minecraft.client.renderer.WorldRenderer
import kotlin.math.PI

object RenderEuclid {

    /**
     * @param baseCenter The center of the base of the cylinder
     * @param height The height of the cylinder
     * @param radius The radius of the cylinder
     * @param numOfSides Because this is minecraft, the cylinder is an approximation using a regular polygons. Because of this, the function should really be called "drawRegularPolygonalPrism"
     */
    fun WorldRenderer.drawCylinder(baseCenter: Point3d, radius: Double, height: Double, numOfSides: Int = 8) {
        val interiorAngleDegrees = (360 / numOfSides).toAngleFromDegrees() // 🚨🚨🚨 please note this is in degrees. Sin and Cos functions use radians
        val sideLength = 2 * radius * cos(interiorAngleDegrees / 2)



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