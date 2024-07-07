package me.partlysanestudios.partlysaneskies.features.economy.minioncalculator

import com.google.gson.JsonObject
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.getJsonFromPath

class MinionFuel(val id: String, private val duration: Double?, val upgrade: Double) {
    constructor(id: String, obj: JsonObject) : this(
        id,
        obj.getJsonFromPath("duration")?.asDouble,
        obj.getJsonFromPath("speed_upgrade")?.asDouble ?: 0.0
    )

    // Returns the amount of fuel needed for the duration specified (in minutes)
    fun amountNeeded(minuteDuration: Double = 1.0): Double? = duration?.let { minuteDuration / it }
}
