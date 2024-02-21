package me.partlysanestudios.partlysaneskies.data.skyblockdata

import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.data.api.Request
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.titleCase
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import net.minecraft.nbt.NBTTagList
import org.apache.logging.log4j.Level

class SkyblockPlayer(val username: String) {


    private var selectedProfileUUID: String = ""
    var lastUpdateTime: Long = 0
    var uuid: String = ""


    var skyblockLevel = 0.0
    var catacombsLevel = 0.0
    var combatLevel = 0.0
    var miningLevel = 0.0
    var foragingLevel = 0.0
    var farmingLevel = 0.0
    var enchantingLevel = 0.0
    var fishingLevel = 0.0
    var alchemyLevel = 0.0
    var tamingLevel = 0.0
    var averageSkillLevel = 0.0

    var armorName: Array<String> = arrayOf()
    var arrowCount = -1
    var petName: String = ""
    var selectedDungeonClass: String = ""
    var normalRunCount: Array<Int> = arrayOf()
    var masterModeRunCount: Array<Int> = arrayOf()

    var totalRuns = 0

    var secretsCount = 0
    var secretsPerRun = 0.0
    var baseHealth = 0.0
    var baseDefense = 0.0
    var baseIntelligence = 0.0
    var baseEffectiveHealth = 0.0


    private class Lock: Object()

    private val lock = Lock()

    fun isExpired(): Boolean {
        return !MathUtils.onCooldown(lastUpdateTime, config.playerDataCacheTime * 60 * 1000L)
    }

    fun instantiateData() {
        val requestURL = "https://mowojang.matdoes.dev/users/profiles/minecraft/$username"


        RequestsManager.newRequest(Request(requestURL, { uuidRequest ->
            if (!uuidRequest.hasSucceeded()) {
                synchronized(lock) {
                    lock.notifyAll()
                }
                log(Level.ERROR, "Error getting mojang api for $username")
                return@Request
            }

            uuid = JsonParser().parse(uuidRequest.getResponse()).getAsJsonObject().get("id")?.asString ?: ""

            val pssSkyblockPlayerUrl = "${config.apiUrl}/v1/hypixel/skyblockplayer?uuid=$uuid"
            RequestsManager.newRequest(Request(pssSkyblockPlayerUrl, { skyblockPlayerResponse ->
                if (!skyblockPlayerResponse.hasSucceeded()) {
                    synchronized(lock) {
                        lock.notifyAll()
                    }
                    log(Level.ERROR, "Error getting partly sane skies api $username")
                    return@Request
                }
                lastUpdateTime = time

                val skyblockPlayerObject = JsonParser().parse(skyblockPlayerResponse.getResponse()).asJsonObject["skyblockPlayer"].asJsonObject

                selectedProfileUUID = skyblockPlayerObject["currentProfileId"]?.asString ?: ""
                val profiles = skyblockPlayerObject["profiles"].asJsonArray
                for (profile in profiles) {
                    val profObj = profile.asJsonObject
                    if (profObj["selected"].asBoolean) {
                        skyblockLevel = profObj["skyblockExperience"].asDouble / 100
                        catacombsLevel = catacombsLevelToExperience(profObj["catacombsExperience"].asFloat)
                        combatLevel = SkyblockDataManager.getSkill("COMBAT")?.getLevelFromExperience(profObj["combatExperience"].asFloat) ?: 0.0
                        miningLevel = SkyblockDataManager.getSkill("MINING")?.getLevelFromExperience(profObj["miningExperience"].asFloat) ?: 0.0
                        foragingLevel = SkyblockDataManager.getSkill("FORAGING")?.getLevelFromExperience(profObj["foragingExperience"].asFloat) ?: 0.0
                        farmingLevel = SkyblockDataManager.getSkill("FARMING")?.getLevelFromExperience(profObj["farmingExperience"].asFloat) ?: 0.0
                        enchantingLevel = SkyblockDataManager.getSkill("ENCHANTING")?.getLevelFromExperience(profObj["enchantingExperience"].asFloat) ?: 0.0
                        fishingLevel = SkyblockDataManager.getSkill("FISHING")?.getLevelFromExperience(profObj["fishingExperience"].asFloat) ?: 0.0
                        alchemyLevel = SkyblockDataManager.getSkill("ALCHEMY")?.getLevelFromExperience(profObj["alchemyExperience"].asFloat) ?: 0.0
                        tamingLevel = SkyblockDataManager.getSkill("TAMING")?.getLevelFromExperience(profObj["tamingExperience"].asFloat) ?: 0.0
                        averageSkillLevel = (combatLevel + miningLevel + foragingLevel + farmingLevel + enchantingLevel + fishingLevel + alchemyLevel + tamingLevel) / 8
                        petName = profObj["petName"].asString.titleCase()
                        selectedDungeonClass = profObj["selectedDungeonClass"].asString.titleCase()
                        totalRuns = profObj["totalRuns"].asInt
                        secretsCount = profObj["secretsCount"].asInt
                        secretsPerRun = secretsCount/totalRuns.toDouble()
                        baseHealth = profObj["baseHealth"].asDouble
                        baseDefense = profObj["baseDefense"].asDouble
                        baseIntelligence = profObj["baseIntelligence"].asDouble
                        baseEffectiveHealth = profObj["baseEffectiveHealth"].asDouble


                        if (profObj["armorData"] == null) {
                            armorName = arrayOf("", "", "", "")
                        } else {
                            val armorNBT: NBTTagList = SystemUtils.base64ToNbt(profObj["armorData"].asString).getTagList("i", 10)
                            armorName = arrayOf("", "", "", "")
                            for (i in 0 until armorNBT.tagCount()) {
                                armorName[i] =
                                    armorNBT.getCompoundTagAt(i).getCompoundTag("tag").getCompoundTag("display")
                                        .getString("Name")
                            }
                        }

                        val arrowNBT: NBTTagList = SystemUtils.base64ToNbt(profObj["quiverData"].asString).getTagList("i", 10)

                        var sum = -1
                        for (i in 0 until arrowNBT.tagCount()) {
                            val item = arrowNBT.getCompoundTagAt(i)
                            val itemDisplayTag =
                                arrowNBT.getCompoundTagAt(i).getCompoundTag("tag").getCompoundTag("display")
                            if (!itemDisplayTag.hasKey("Lore")) {
                                continue
                            }
                            val loreList = itemDisplayTag.getTagList("Lore", 8)
                            for (k in 0 until loreList.tagCount()) {
                                val loreLine = loreList.getStringTagAt(k)
                                if (loreLine.contains("ARROW")) {
                                    sum += if (item.hasKey("Count")) {
                                        item.getInteger("Count")
                                    } else {
                                        1
                                    }
                                    break
                                }
                            }
                        }
                        arrowCount = sum


                        normalRunCount = arrayOf(0, 0, 0, 0, 0, 0, 0, 0)

                        val normalRunArray = profObj["normalRuns"].asJsonArray
                        for (i in 0 until normalRunArray.size()) {
                            normalRunCount[i] = normalRunArray[i].asInt
                        }

                        masterModeRunCount = arrayOf(0, 0, 0, 0, 0, 0, 0, 0)

                        val masterModeRunArray = profObj["masterModeRuns"].asJsonArray
                        for (i in 0 until normalRunArray.size()) {
                            masterModeRunCount[i] = masterModeRunArray[i].asInt
                        }

                        break
                    }
                }
                synchronized(lock) {
                    lock.notifyAll()
                }
            }))
        }))

        synchronized(lock) {
            lock.wait()
        }

    }


    var catacombsExperiencePerLevel = intArrayOf(
        50,
        75,
        110,
        160,
        230,
        330,
        470,
        670,
        950,
        1340,
        1890,
        2665,
        3760,
        5260,
        7380,
        10300,
        14400,
        20000,
        27600,
        38000,
        52500,
        71500,
        97000,
        132000,
        180000,
        243000,
        328000,
        445000,
        600000,
        800000,
        1065000,
        1410000,
        1900000,
        2500000,
        3300000,
        4300000,
        5600000,
        7200000,
        9200000,
        12000000,
        15000000,
        19000000,
        24000000,
        30000000,
        38000000,
        48000000,
        60000000,
        75000000,
        93000000,
        116250000
    )

    private fun catacombsLevelToExperience(experience: Float): Double {
        var level = 0f
        if (experience >= catacombsExperiencePerLevel[catacombsExperiencePerLevel.size - 1]) {
            return 50.0
        }
        for (i in catacombsExperiencePerLevel.indices) {
            if (experience > catacombsExperiencePerLevel[i]) {
                level = (i - 1).toFloat()
            }
        }
        level += (experience - catacombsExperiencePerLevel[level.toInt()]) / (catacombsExperiencePerLevel[level.toInt() + 1] - catacombsExperiencePerLevel[level.toInt()])
        return level.toDouble()
    }


}
