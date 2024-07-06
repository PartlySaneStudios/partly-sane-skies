//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.config.psconfig

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import org.apache.logging.log4j.Level
import java.io.FileWriter
import java.nio.file.Files
import kotlin.io.path.Path

object ConfigManager {

    private val configs = HashMap<String, Config>()

    // Save path should not include the /config/partly-sane-skies/
    fun registerNewConfig(savePath: String, config: Config) {
        var path = savePath
        if (savePath.startsWith("/")) {
            path = savePath.substring(1)
        }
        if (!savePath.endsWith(".json")) {
            path = "$savePath.json"
        }
        config.savePath = path
        configs[path] = config
    }

    fun saveAllConfigs() {
        for (config in configs.entries) {
            saveConfig(config.key, config.value)
        }
    }

    fun loadAllConfigs() {
        for (config in configs.entries) {
            loadConfig(config.key, config.value)
        }
    }

    fun saveConfig(savePath: String, config: Config) {
        val path = Path("./config/partly-sane-skies/$savePath")
        val file = path.toFile()
        file.parentFile.mkdirs()
        file.createNewFile()
        file.setWritable(true)

        val builder = GsonBuilder()
        builder.setPrettyPrinting()
        builder.serializeSpecialFloatingPointValues()
        val gson = builder.create()

        // Converts list to JSON string
        val json = gson.toJson(config.saveToJson())

        // Writes string to file
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()
    }

    private fun loadConfig(savePath: String, config: Config) {
        val path = Path("./config/partly-sane-skies/$savePath")

        val file = path.toFile()
        file.parentFile.mkdirs()
        file.setWritable(true)
        file.setReadable(true)

        // If it is the first time creating a file, we insert the default values in
        if (file.createNewFile()) {
            saveConfig(savePath, config)
        } else { // Else, we load the data
            val element = JsonParser().parse(file.reader())
            try {
                config.loadFromJson(element)
            } catch (e: Exception) {
                log(Level.ERROR, "Error loading config with path $savePath")
                e.printStackTrace()
            }
        }

    }
}