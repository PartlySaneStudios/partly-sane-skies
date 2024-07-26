package me.partlysanestudios.partlysaneskies.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.data.api.GetRequest
import me.partlysanestudios.partlysaneskies.data.api.Request
import me.partlysanestudios.partlysaneskies.data.api.RequestRunnable
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.getJsonFromPath

object SkyCryptUtils {
    private val skyCryptProfileURL: String = "https://sky.shiiyu.moe/api/v2/profile/"
    private val skyCryptNetworthPath: String = "data/networth/networth"
    private val skyCryptFirstJoinPath: String = "raw/first_join"

    fun getSkyCryptNetworth(username: String): Double {
        var networth: Double
        val skyCryptObject = obtainSkyCryptPlayerJSONData(username)
        val profileData = (skyCryptObject["profiles"] as JsonObject)
        if (DebugKey.isDebugMode()) ChatUtils.sendClientMessage("§eProfiles obtained.")
        val currentProfile: JsonObject = findCurrentProfile(profileData)
        networth = currentProfile.getJsonFromPath(skyCryptNetworthPath)?.asDouble ?: -1.0
        if (DebugKey.isDebugMode()) ChatUtils.sendClientMessage("§eCurrent profile and its networth found.")
        return networth
    }

    fun getFirstJoinEpoch(username: String): Long {
        var firstJoinEpoch: Long
        val skyCryptObject = obtainSkyCryptPlayerJSONData(username)
        val profileData = (skyCryptObject["profiles"] as JsonObject)
        if (DebugKey.isDebugMode()) ChatUtils.sendClientMessage("§eProfiles obtained.")
        val currentProfile: JsonObject = findCurrentProfile(profileData)
        firstJoinEpoch = currentProfile.getJsonFromPath(skyCryptFirstJoinPath)?.asLong ?: -1L
        return firstJoinEpoch
    }

    private fun obtainSkyCryptPlayerJSONData(username: String): JsonObject {
        lateinit var skyCryptObject: JsonObject
        RequestsManager.newRequest(
            GetRequest(
                (skyCryptProfileURL + username),
                RequestRunnable { r: Request ->
                    if (!r.hasSucceeded()) {
                        ChatUtils.sendClientMessage(
                            "§ePSS is having trouble contacting SkyCrypt's API. Please try again; if this continues please report this to us via §9/discord§e.",
                        )
                        return@RequestRunnable
                    }
                    if (DebugKey.isDebugMode()) ChatUtils.sendClientMessage("§eSuccessfully contacted SkyCrypt's API.")
                    skyCryptObject = (JsonParser().parse(r.getResponse()) as JsonObject)
                },
            ),
        )
        return skyCryptObject
    }

    private fun findCurrentProfile(profileData: JsonObject): JsonObject {
        lateinit var theProfileDataToReturn: JsonObject
        if (DebugKey.isDebugMode()) ChatUtils.sendClientMessage("§eFinding current profile...")
        for (profile: Map.Entry<String, JsonElement> in profileData.entrySet()) {
            val theProfile = profile.value.getAsJsonObject()
            if (theProfile.get("current").asBoolean) {
                theProfileDataToReturn = theProfile
            }
        }
        return theProfileDataToReturn
    }
}
