//
// Written by Su386 and Erymanthus[#5074] | (u/)RayDeeUx.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.data.cache

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity.Companion.getRarityFromColorCode
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.containerInventory
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getLore
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import java.io.FileWriter
import java.io.IOException
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path

object VisitorLogbookData {
    private var data: VisitorLogBookJson? = null
    private val jsonPath = Path("./config/partly-sane-skies/visitorLogbook.json")

    @Throws(IOException::class)
    fun load() {
        val file = jsonPath.toFile()

        // If the file doesn't exist, fill it with an empty array list to prevent NullPointerException
        if (file.createNewFile()) {
            file.setWritable(true)
            val writer = FileWriter(file)
            writer.write(Gson().toJson(VisitorLogBookJson()))
            writer.close()
        }

        // Make a new reader
        file.setWritable(true)
        val reader: Reader = Files.newBufferedReader(Paths.get(file.path))

        // Reads the file and set it as the list
        val data = Gson().fromJson(reader, VisitorLogBookJson::class.java) as VisitorLogBookJson
        this.data = data
    }

    // Saves all the visitor data
    @Throws(IOException::class)
    fun save() {
        // Creates a new file and Gson instance
        val file = jsonPath.toFile()
        val builder = GsonBuilder()
        builder.setPrettyPrinting()
        builder.serializeSpecialFloatingPointValues()
        val gson = builder.create()

        // Converts list to JSON string
        val json = gson.toJson(data)

        // Writes string to file
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()
    }

    fun scanForVisitors() {
        val screen = minecraft.currentScreen
        if (screen !is GuiChest) {
            return
        }
        if (!isVisitorLogbook(screen)) {
            return
        }
        val chest = screen.containerInventory
        for (i in 9..44) {
            val item = chest.getStackInSlot(i) ?: continue
            val lore = item.getLore()

            if (lore.isEmpty()) {
                continue
            }

            val displayName = item.displayName
            val rarity = item.displayName.substring(2, 4).getRarityFromColorCode()

            val noColorLore = lore.removeColorCodes()

            val timesVisitedRegex = "Times Visited: (\\d+(?:,\\d+)*)".toRegex()
            val offersAcceptedRegex = "Offers Accepted: (\\d+(?:,\\d+)*)".toRegex()
            var timesAccepted = 0
            var timesVisited = 0
            for (line in noColorLore) {
                if (offersAcceptedRegex.containsMatchIn(line)) {
                    val (find) = offersAcceptedRegex.find(line)?.destructured ?: continue
                    timesAccepted = find.replace(",", "").toIntOrNull() ?: 0
                } else if (timesVisitedRegex.containsMatchIn(line)) {
                    val (find) = timesVisitedRegex.find(line)?.destructured ?: continue
                    timesVisited = find.replace(",", "").toIntOrNull() ?: 0
                }
            }

            val texture =
                item.tagCompound
                    ?.getCompoundTag("SkullOwner")
                    ?.getCompoundTag("Properties")
                    ?.getTagList("textures", 10)
                    ?.getCompoundTagAt(0)
                    ?.getString("Value") ?: ""

            val visitor = Visitor(displayName, rarity, texture, timesVisited, timesAccepted)
            data?.visitors?.set("$displayName+$texture", visitor)
        }

        Thread(
            {
                save()
            },
            "Visitor Data Save",
        ).start()
    }

    fun getVisitor(name: String): Visitor? {
        val visitor = data?.visitors?.get(name)
        if (visitor == null) {
            for (visitor in (data?.visitors?.values) ?: ArrayList()) {
                if (visitor.name == name) {
                    return visitor
                }
            }
            return null
        } else {
            return visitor
        }
    }

    fun getAllVisitors(): MutableCollection<Visitor> = data?.visitors?.values ?: ArrayList()

    fun isVisitorLogbook(screen: GuiScreen): Boolean {
        if (screen !is GuiChest) {
            return false
        }
        val logbook = screen.containerInventory
        return logbook.displayName.formattedText.removeColorCodes().contains("Visitor's Logbook")
    }

    class Visitor(
        val displayName: String,
        val rarity: Rarity,
        val headTexture: String,
        val timesVisited: Int,
        val timesAccepted: Int,
    ) {
        val name get() = displayName.removeColorCodes()
    }

    class VisitorLogBookJson {
        @Expose
        val visitors = HashMap<String, Visitor>()
    }
}
