package me.partlysanestudios.partlysaneskies.features.mining.crystalhollows.gemstonewaypoints

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d
import org.apache.logging.log4j.Level

object GemstoneData {

    val map = HashMap<Point3d, HashMap<GemstoneType, ArrayList<Gemstone>>>()
    private fun registerLocation(gemstone: Gemstone) {
        val chunk = gemstone.chunk
        if (!map.containsKey(chunk)) {
            map[chunk] = HashMap()
        }

        val type = gemstone.type
        if (map[chunk]?.containsKey(type) != true) {
            map[chunk]?.put(type, ArrayList())
        }

        map[chunk]?.get(gemstone.type)?.add(gemstone)
    }

    @SubscribePSSEvent
    fun loadJsonData(event: LoadPublicDataEvent) {
        Thread({
            val startTime = time
            val publicDataResponse = PublicDataManager.getFile("constants/gemstone_locations.json")
            val publicDataArray = JsonParser().parse(publicDataResponse).asJsonArray

            for (element in publicDataArray) {
                val gemstone = serializeGemstone(element.asJsonObject)
                registerLocation(gemstone)
            }

            val timeElasped = startTime - time
            log(Level.INFO, "Loaded all gemstone data in ${timeElasped / 1000} seconds")

        }, "GemstoneLoadData").start()

    }

    private fun serializeGemstone(gemstoneJson: JsonObject): Gemstone {
        val midpoint = gemstoneJson.getAsJsonObject("midPoint3d")
        val block = Point3d(midpoint.get("x").asDouble, midpoint.get("y").asDouble, midpoint.get("z").asDouble)
        val type = GemstoneType.valueOf(gemstoneJson.get("type").asString)
        val size = gemstoneJson.get("size").asInt
        return Gemstone(type, block, size)
    }

    enum class GemstoneType {
        TOPAZ,
        RUBY,
        AMETHYST,
        AMBER,
        JADE,
        SAPPHIRE
    }

    class Gemstone(val type: GemstoneType, val block: Point3d, val size: Int) {
        val chunk: Point3d get() = block.toChunk()
    }
}