//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.pow

object MathUtils {
    fun Number.round(decimalPlaces: Int): Double = Math.round(this.toDouble() * 10.0.pow(decimalPlaces)) / 10.0.pow(decimalPlaces)

    fun Number.floor(decimalPlaces: Int): Double = (this.toDouble() * 10.0.pow(decimalPlaces)).toInt() / 10.0.pow(decimalPlaces)

    fun Number.ceil(decimalPlaces: Int): Double = (this.toDouble() * 10.0.pow(decimalPlaces)).toInt() / 10.0.pow(decimalPlaces)

    fun randInt(
        min: Int,
        max: Int,
    ): Int = ThreadLocalRandom.current().nextInt(min, max + 1)

    fun <K : Any, V : Number> Map<K, V>.sortMap(reverseOrder: Boolean = false): Map<K, V> =
        if (reverseOrder) {
            this.entries.sortedByDescending { it.value.toDouble() }
        } else {
            this.entries.sortedBy { it.value.toDouble() }
        }.associate { it.toPair() }

    /** Takes the last time the event happened in Unix epoch time in milliseconds,
     * and takes the length that the event should last in millisecond
     * Returns false if the event is over, returns true if it is still ongoing
     */
    fun onCooldown(
        lastTime: Long,
        length: Long,
    ): Boolean = PartlySaneSkies.time <= lastTime + length
}
