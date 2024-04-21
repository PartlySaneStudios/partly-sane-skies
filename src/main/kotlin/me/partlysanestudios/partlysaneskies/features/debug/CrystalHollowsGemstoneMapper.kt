package me.partlysanestudios.partlysaneskies.features.debug

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.annotations.Expose
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Range3d
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.IBlockState
import net.minecraft.item.EnumDyeColor
import java.io.File
import java.io.FileWriter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object CrystalHollowsGemstoneMapper {

    private val range = Range3d(Point3d(201.0, 30.0, 201.0), Point3d(824.0, 189.0, 824.0))
    private val world = minecraft.theWorld
    private var alreadyCheckedCoords = ArrayList<Point3d>()

    fun scanWorld() {
        alreadyCheckedCoords = ArrayList()
        val rangeSize = (range.sortedPoints[1].x - range.sortedPoints[0].x) * (range.sortedPoints[1].y - range.sortedPoints[0].y) *  (range.sortedPoints[1].z - range.sortedPoints[0].z)
        var checkedBlocks = 0
        val gemstones = LinkedList<Gemstone>()
        val startTime = time

        for (x in range.sortedPoints[0].x.toInt()..range.sortedPoints[1].x.toInt()) {
            for (y in range.sortedPoints[0].y.toInt()..range.sortedPoints[1].y.toInt()) {
                for (z in range.sortedPoints[0].z.toInt()..range.sortedPoints[1].z.toInt()) {
                    checkedBlocks++
                    val currentTime = time
                    val timeElapsed = currentTime - startTime
                    val estimatedTotalTime = (rangeSize * timeElapsed) / checkedBlocks
                    val timeLeft = estimatedTotalTime - timeElapsed

                    val minutesLeft = timeLeft / 1000 / 60
                    sendClientMessage("Checking block (${x.formatNumber()}, ${y.formatNumber()}, ${z.formatNumber()})\n${checkedBlocks.formatNumber()} / ${rangeSize.formatNumber()} (${(checkedBlocks/rangeSize *  100).round(1)}%, ${minutesLeft.round(2).formatNumber()} minutes left)...")
                    val point = Point3d(x.toDouble(), y.toDouble(), z.toDouble())

                    if (!isGlass(world.getBlockState(point.toBlockPosInt()))) {
                        continue
                    }

                    if (alreadyCheckedCoords.contains(point)) {
                        continue
                    }

                    val gemstoneCoords = ArrayList<Point3d>()

                    gemstoneCoords.add(point)
                    extractGemstone(point, gemstoneCoords)

                    gemstones.add(Gemstone(gemstoneCoords, world.getBlockState(point.toBlockPosInt()).block.localizedName))
                }
            }
        }
        sendClientMessage("Finished checking world, dumping data...")
        dumpGemstoneData(gemstones)
        sendClientMessage("Data dumped data")
    }


    fun getPrettyData() {
        val filePath = File("./config/partly-sane-skies/rawgemstone.json")
        sendClientMessage("Loading data...")
        val json = JsonParser().parse(filePath.reader())
        sendClientMessage("Converting data...")
        val jsonArray = json.asJsonArray

        val totalLength = jsonArray.size()
        val gemstones = LinkedList<PrettyGemstone>()
        var i = 0.0
        for (element in jsonArray) {
            i++
            sendClientMessage("Converting crystal ${i.formatNumber()} of ${totalLength.formatNumber()} (${(i/totalLength * 100).round(1).formatNumber()}%)...")
            val obj = element.asJsonObject

            val coordinates = obj.get("coordinates").asJsonArray
            var sumX = 0.0
            var sumY = 0.0
            var sumZ = 0.0

            val firstPointObj = coordinates[0].asJsonObject
            val firstPoint = Point3d(firstPointObj.get("x").asDouble, firstPointObj.get("y").asDouble, firstPointObj.get("z").asDouble)
            for (coordElement in coordinates) {
                val coordObj = coordElement.asJsonObject
                sumX += coordObj.get("x").asDouble
                sumY += coordObj.get("y").asDouble
                sumZ += coordObj.get("z").asDouble
            }

            val averagePoint = Point3d((sumX/coordinates.size()).round(1), (sumY/coordinates.size()).round(1), (sumZ/coordinates.size()).round(1))

            try {
                val type = "COLOR_${world.getBlockState(firstPoint.toBlockPosInt()).getValue(PropertyEnum.create("color", EnumDyeColor::class.java))}"
                gemstones.add(PrettyGemstone(averagePoint, type, coordinates.size()))
            } catch (e: Exception) {
                e.printStackTrace()
                continue
            }

        }

        sendClientMessage("Dumping data...")
        dumpPrettyGemstoneData(gemstones)
        sendClientMessage("Data dumped.")
    }


    fun removeNucleusCords() {
        val filePath = File("./config/partly-sane-skies/prettygemstone.json")
        sendClientMessage("Loading data...")
        val json = JsonParser().parse(filePath.reader())
        sendClientMessage("Converting data...")
        val jsonArray = json.asJsonArray

        val crystalNucleusRange = Range3d(Point3d(463.0, 64.0, 460.0), Point3d(559.0, 187.0, 562.0))
        val totalLength = jsonArray.size()
        val list = ArrayList<JsonObject>()
        var i = 0.0
        for (element in jsonArray) {
            sendClientMessage("Converting crystal ${i.formatNumber()} of ${totalLength.formatNumber()} (${(i/totalLength * 100).round(1).formatNumber()}%)...")

            val obj = element.asJsonObject
            val midpoint = obj.getAsJsonObject("midPoint3d")
            val point = Point3d(midpoint["x"].asDouble, midpoint["y"].asDouble, midpoint["z"].asDouble)
            if (crystalNucleusRange.isInRange(point)) {
                continue
            }
            list.add(obj)
            i++
        }

        sendClientMessage("Dumping data...")
        dumpPrettyGemstoneDataNoNucleus(list)
        sendClientMessage("Data dumped.")
    }
    private class PrettyGemstone(
        @Expose
        val midPoint3d: Point3d,
        @Expose
        val type: String,
        @Expose
        val size: Int,
    )


    private fun dumpGemstoneData(gemstones: List<Gemstone>) {
        val json = GsonBuilder().setPrettyPrinting().create().toJson(gemstones)
        // Format the Instant to a human-readable date and time
        // Convert epoch time to LocalDateTime
        val dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())

        // Format the LocalDateTime to a human-readable date and time
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
        val formattedDate = dateTime.format(formatter)

        File("./config/partly-sane-skies/dumps/").mkdirs()
        // Declares the file
        val file = File("./config/partly-sane-skies/dumps/gemstone-dump-${formattedDate}.json")
        file.createNewFile()
        file.setWritable(true)
        // Saves teh data to the file
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()
    }

    private fun dumpPrettyGemstoneData(gemstones: List<PrettyGemstone>) {
        val json = GsonBuilder().setPrettyPrinting().create().toJson(gemstones)
        // Format the Instant to a human-readable date and time
        // Convert epoch time to LocalDateTime
        val dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())

        // Format the LocalDateTime to a human-readable date and time
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
        val formattedDate = dateTime.format(formatter)

        File("./config/partly-sane-skies/dumps/").mkdirs()
        // Declares the file
        val file = File("./config/partly-sane-skies/dumps/pretty-gemstone-dump-${formattedDate}.json")
        file.createNewFile()
        file.setWritable(true)
        // Saves teh data to the file
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()
    }

    private fun dumpPrettyGemstoneDataNoNucleus(gemstones: List<JsonObject>) {
        val json = GsonBuilder().setPrettyPrinting().create().toJson(gemstones)
        // Format the Instant to a human-readable date and time
        // Convert epoch time to LocalDateTime
        val dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())

        // Format the LocalDateTime to a human-readable date and time
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
        val formattedDate = dateTime.format(formatter)

        File("./config/partly-sane-skies/dumps/").mkdirs()
        // Declares the file
        val file = File("./config/partly-sane-skies/dumps/nonucleus-gemstone-dump-${formattedDate}.json")
        file.createNewFile()
        file.setWritable(true)
        // Saves the data to the file
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()
    }

    private fun extractGemstone(point: Point3d, gemstoneCoords: ArrayList<Point3d>) {
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue
                    }

                    val newPoint = point + Point3d(x.toDouble(), y.toDouble(), z.toDouble())

                    if (alreadyCheckedCoords.contains(newPoint)) {
                        continue
                    }

                    if (gemstoneCoords.contains(newPoint)) {
                        continue
                    }

                    if (isGlass(world.getBlockState(newPoint.toBlockPosInt()))) {
                        gemstoneCoords.add(newPoint)
                        alreadyCheckedCoords.add(newPoint)
                        extractGemstone(newPoint, gemstoneCoords)
                    }
                }
            }
        }
    }

    private fun isGlass(blockState: IBlockState): Boolean {
        return blockState.block.material == Material.glass
    }


    private class Gemstone(val coordinates: List<Point3d>, val type: String) {
        val geographicMiddle: Point3d get() {
            var xPoints = 0.0
            var yPoints = 0.0
            var zPoints = 0.0


            for (point in coordinates) {
                xPoints += point.x
                yPoints += point.y
                zPoints += point.z
            }

            return Point3d(xPoints/coordinates.size, yPoints/coordinates.size, zPoints/coordinates.size)
        }

    }
}