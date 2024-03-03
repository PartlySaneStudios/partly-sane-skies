//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.utils.geometry.orientation

import kotlin.math.PI

class Angle private constructor(private val radians: Double) {
    companion object {
        /**
         * @return An angle object given a number of degrees
         */
        fun Number.toAngleFromDegrees(): Angle {
            return Angle((PI / 180) * this.toDouble())
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
        return (180/ PI) * radians
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

    override fun toString(): String {
        return "Angle(radians=$radians, degrees=${this.asDegrees()})"
    }
}