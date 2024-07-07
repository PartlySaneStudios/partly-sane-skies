//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining.crystalhollows.gemstonewaypoints

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point2d
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d
import org.apache.logging.log4j.Level
import java.awt.Color

object GemstoneData {
    val map = HashMap<Point2d, HashMap<GemstoneType, ArrayList<Gemstone>>>()

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
        Thread(
            {
                val startTime = time
                val publicDataResponse = PublicDataManager.getFile("constants/gemstone_locations.json")
//            log(Level.INFO, publicDataResponse)
                val publicDataArray = JsonParser().parse(publicDataResponse).asJsonArray

                for (element in publicDataArray) {
                    val gemstone = serializeGemstone(element.asJsonObject)
                    registerLocation(gemstone)
                }

                val timeElasped = time - startTime
                log(Level.INFO, "Loaded all gemstone data in ${timeElasped / 1000.0} seconds")
            },
            "GemstoneLoadData",
        ).start()
    }

    private fun serializeGemstone(gemstoneJson: JsonObject): Gemstone {
        val midpoint = gemstoneJson.getAsJsonObject("midPoint3d")
        val block = Point3d(midpoint.get("x").asDouble, midpoint.get("y").asDouble, midpoint.get("z").asDouble)
        val type = GemstoneType.valueOf(gemstoneJson.get("type").asString)
        val size = gemstoneJson.get("size").asInt
        return Gemstone(type, block, size)
    }

    enum class GemstoneType(val displayName: String, val color: Color) {
        TOPAZ("Topaz", Color(0xFFFF55)),
        RUBY("Ruby", Color(0xFF5555)),
        AMETHYST("Amethyst", Color(0xFF55FF)),
        AMBER("Amber", Color(0xFFAA00)),
        JADE("Jade", Color(0x55FF55)),
        SAPPHIRE("Sapphire", Color(0x5555FF)),
    }

    class Gemstone(val type: GemstoneType, val block: Point3d, val size: Int) {
        val chunk: Point2d get() = block.toChunk()
    }
}
