//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.debug

import com.google.gson.*
import kotlinx.serialization.json.Json
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.features.farming.endoffarmnotifer.EndOfFarmNotifier
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.visPrint
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getItemstackList
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getSeparateUpperLowerInventories
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import java.io.File
import java.io.FileWriter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object PercyMode {
    private fun getCurrentScreenDump(): JsonElement {
        val jsonObject = JsonObject()

        val screenObj = JsonObject()
        if (PartlySaneSkies.minecraft.currentScreen is GuiChest) {
            val inventories = PartlySaneSkies.minecraft.currentScreen.getSeparateUpperLowerInventories()
            for (inventory in inventories) {
                val invArray = JsonArray()

                for (item in inventory?.getItemstackList() ?: listOf()) {

                    val nbt = NBTTagCompound()
                    item.writeToNBT(nbt)
                    invArray.add(Gson().toJsonTree(nbt)?.asJsonObject?: JsonObject())
                }

                screenObj.add(inventory?.name ?: "", invArray)
            }
        }

        jsonObject.add("inventories", screenObj)
        jsonObject.add("class", Gson().toJsonTree(PartlySaneSkies.minecraft.currentScreen?.javaClass?.name ?: ""))

        return jsonObject
    }

    private fun getEntityDump(): JsonElement {
        val jsonArray = JsonArray()
        val entities = ArrayList(PartlySaneSkies.minecraft.theWorld?.getLoadedEntityList()?: ArrayList())
        for (entity in entities) {
            val jsonObject = JsonObject()
            val name = entity.name?: ""
            try{
                val nbt = NBTTagCompound()

                entity.writeToNBT(nbt)

                jsonObject.add("entityData", Gson().toJsonTree(nbt))

                jsonObject.add("class", Gson().toJsonTree(entity.javaClass.name))
            } catch (exception: Exception) {
                sendClientMessage("Error getting data for entity $name")
                jsonArray.add(jsonObject)
            }

            jsonArray.add(jsonObject)
        }

        return jsonArray
    }

    private fun getInventoryDump(): JsonObject {
        val nbt = NBTTagList()
        PartlySaneSkies.minecraft.thePlayer?.inventory?.writeToNBT(nbt)
        return Gson().toJsonTree(nbt)?.asJsonObject?: JsonObject()
    }

    private fun getPlayerDump(): JsonObject {
        val nbt = NBTTagCompound()
        PartlySaneSkies.minecraft.thePlayer?.writeToNBT(nbt)
        return Gson().toJsonTree(nbt)?.asJsonObject?: JsonObject()
    }

    private fun getTimeDump(): JsonObject {
        val fullObject = JsonObject()
        // Format the Instant to a human-readable date and time
        // Convert epoch time to LocalDateTime
        val dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())

        // Format the LocalDateTime to a human-readable date and time
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDate = dateTime.format(formatter)

        fullObject.add("unix", JsonPrimitive(PartlySaneSkies.time))
        formatter.withZone(ZoneId.systemDefault())
        fullObject.add("formattedUtc", JsonPrimitive(formattedDate))

        return fullObject
    }

    private fun getFullDump(): JsonObject {
        val fullObject = JsonObject()

        fullObject.add("time", getTimeDump())
        if (config.dev.debugCurrentScreenDump) fullObject.add("currentScreen", getCurrentScreenDump())
        if (config.dev.debugEntityDump) fullObject.add("entities", getEntityDump())
        if (config.dev.debugInventoryDump) fullObject.add("inventory", getInventoryDump())
        if (config.dev.debugPlayerDump) fullObject.add("player", getPlayerDump())

        return fullObject
    }


    private fun dumpToFile(jsonObject: JsonObject) {
        // Format the Instant to a human-readable date and time
        // Convert epoch time to LocalDateTime
        val dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())

        // Format the LocalDateTime to a human-readable date and time
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
        val formattedDate = dateTime.format(formatter)

        File("./config/partly-sane-skies/dumps/").mkdirs()
        // Declares the file
        val file = File("./config/partly-sane-skies/dumps/percy-dump-${formattedDate}.json")
        file.createNewFile()
        file.setWritable(true)
        // Creates a new Gson object to save the data
        // Saves teh data to the file
        val json = GsonBuilder().setPrettyPrinting().create().toJson(jsonObject)
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()
    }

    private fun dumpToClipboard(jsonObject: JsonObject) {
        SystemUtils.copyStringToClipboard(Gson().toJson(jsonObject))
    }


    fun dump() {
        dumpToFile(getFullDump())
        dumpToClipboard(getFullDump())
    }
}