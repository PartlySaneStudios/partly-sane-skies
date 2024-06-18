package me.partlysanestudios.partlysaneskies.config.psconfig

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import org.apache.logging.log4j.Level
import java.io.FileWriter
import kotlin.io.path.Path

object ConfigManager {

    private val configs = HashMap<String, Config>()
    fun registerNewConfig(savePath: String, config: Config) {
        config.savePath = savePath
        configs[savePath] = config
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
        // Creates a new file and Gson instance
        val file = Path(savePath).toFile()
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
        val data = PublicDataManager.getFile(savePath)
        val element = JsonParser().parse(data)
        try {
            config.loadFromJson(element)
        } catch (e: Exception) {
            log(Level.ERROR, "Error loading config with path $savePath")
            e.printStackTrace()
        }
    }
}