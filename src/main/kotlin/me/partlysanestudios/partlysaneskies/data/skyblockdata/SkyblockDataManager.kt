//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.data.skyblockdata

import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.data.api.Request
import me.partlysanestudios.partlysaneskies.data.api.RequestRunnable
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager.newRequest
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager.getFile
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import java.io.IOException
import java.net.MalformedURLException

object SkyblockDataManager {
    @SubscribePSSEvent
    fun onDataLoad(event: LoadPublicDataEvent?) {
        try {
            initBitValues()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //    --------------------------- Items ---------------------------
    var bitIds = ArrayList<String>()
    private var nameToIdMap = HashMap<String, String>()
    private var idToItemMap = HashMap<String, SkyblockItem>()
    private var lastAhUpdateTime = time
    private fun checkLastUpdate(): Boolean {
        return time >= lastAhUpdateTime + 1000 * 60 * 5
    }

    fun updateAll() {
        lastAhUpdateTime = time
        updateItems()
    }

    @Throws(IOException::class)
    private fun updateItems() {
        newRequest(
            Request("${config.apiUrl}/v1/hypixel/skyblockitem",
                RequestRunnable { s: Request ->
                    val itemDataString = s.getResponse()
                    if (!s.hasSucceeded()) {
                        return@RequestRunnable
                    }
                    val itemDataJson = JsonParser().parse(itemDataString).getAsJsonObject()
                    val products = itemDataJson.getAsJsonArray("products")

                    for (product in products) {
                        val en = product.asJsonObject
                        val rarity = try {
                            Rarity.valueOf(en.get("rarity")?.asString ?: "")
                        } catch (e: IllegalArgumentException) {
                            Rarity.UNKNOWN
                        }

                        val skyblockItem = SkyblockItem(
                            en.get("itemId").asString,
                            rarity,
                            en.get("name")?.asString ?: "",
                            en.get("npcSell")?.asDouble ?: 0.0,
                            en.get("bazaarBuy")?.asDouble ?: 0.0,
                            en.get("bazaarSell")?.asDouble ?: 0.0,
                            en.get("averageBazaarBuy")?.asDouble ?: 0.0,
                            en.get("averageBazaarSell")?.asDouble ?: 0.0,
                            en.get("lowestBin")?.asDouble ?: 0.0,
                            en.get("averageLowestBin")?.asDouble ?: 0.0,
                            en.get("material")?.asString ?: "",
                            en.get("unstackable")?.asBoolean ?: false
                        )

                        idToItemMap[en.get("itemId").asString] = skyblockItem
                        nameToIdMap[en.get("name").asString] = en.get("itemId").asString

                    }

                }, inMainThread = false, executeOnNextFrame = false, acceptAllCertificates = false
            )
        )
    }

    fun initBitValues() {
        val bitsShopObject =
            JsonParser().parse(getFile("constants/bits_shop.json")).getAsJsonObject().getAsJsonObject("bits_shop")
        for ((id, value) in bitsShopObject.entrySet()) {
            val bitCost = value.asInt
            val item = getItem(id)
                ?: continue
            bitIds.add(item.id)
            item.bitCost = bitCost
        }
    }

    fun getId(name: String): String {
        return if (!nameToIdMap.containsKey(name)) {
            ""
        } else nameToIdMap[name]!!
    }

    fun getItem(id: String): SkyblockItem? {
        return if (!idToItemMap.containsKey(id)) {
            null
        } else idToItemMap[id]
    }

    fun runUpdaterTick() {
        if (checkLastUpdate()) {
            lastAhUpdateTime = time
            Thread {
                lastAhUpdateTime =
                    time
                updateAll()
                lastAhUpdateTime =
                    time
            }.start()
            lastAhUpdateTime = time
        }
    }

    //    --------------------------- Skills ---------------------------
    private var idToSkillMap = HashMap<String, SkyblockSkill>()

    @Throws(MalformedURLException::class)
    fun initSkills() {
        newRequest(
            Request(
                "https://api.hypixel.net/resources/skyblock/skills",
                RequestRunnable { s: Request ->
                    val itemDataString = s.getResponse()
                    if (!s.hasSucceeded()) {
                        return@RequestRunnable
                    }
                    val skillDataJson =
                        JsonParser().parse(itemDataString).getAsJsonObject()
                    val skillObject = skillDataJson["skills"].getAsJsonObject()
                    idToSkillMap =
                        HashMap()

//            Goes through each skill
                    for ((id, value) in skillObject.entrySet()) {
//                Gets the id from the key of the element
                        //                Gets the data from the skill
                        val obj = value.getAsJsonObject()

//                Gets the max level
                        val maxLevel = obj["maxLevel"].asInt
                        //                Gets the json array containing the level
                        val levelArray = obj.getAsJsonArray("levels")
                        //                Creates a new hashmap to store the level and the total Experience Required to get to that level
                        val levelToExpMap =
                            HashMap<Int, Float>()
                        //                Adds the level to the map
                        for (i in 0 until levelArray.size()) {
                            val level = i + 1
                            val experience =
                                levelArray[i].getAsJsonObject()["totalExpRequired"].asFloat
                            levelToExpMap[level] = experience
                        }
                        //                Instantiates a new skyblock skill with all the data
                        val skill = SkyblockSkill(id, maxLevel, levelToExpMap)
                        //                Adds the skill to the map
                        idToSkillMap[id] = skill
                    }
                }, false, false
            )
        )
    }

    fun getSkill(skillId: String): SkyblockSkill? {
        return idToSkillMap[skillId]
    }

    //    --------------------------- Players ---------------------------
    private val playerCache = HashMap<String, SkyblockPlayer>()
    fun getPlayer(username: String): SkyblockPlayer {
        val player: SkyblockPlayer?
        if (playerCache.containsKey(username)) {
            player = playerCache[username]
            if (player?.isExpired() != true) {
                return player ?: SkyblockPlayer("") // Returns an empty player if null
            }
        } else {
            player = SkyblockPlayer(username)
        }
        player.instantiateData()
        playerCache[username] = player
        return player
    }
}
