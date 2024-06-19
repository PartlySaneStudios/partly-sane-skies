//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.config.psconfig

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import org.apache.logging.log4j.Level

class Config : ConfigOption() {

    companion object {
        val ConfigOption.asConfig: Config
            get() {
                return this as Config
            }
    }
    // Recursively find paths for options
    fun find(path: String): ConfigOption? {
        val indexOfSplit = path.indexOf("/")

        if (indexOfSplit == -1) {
            return options[path]
        }

        val firstKey = path.substring(0, indexOfSplit)

        val newConfig = options[firstKey]
        if (newConfig !is ConfigOption) {
            return null
        }
        return newConfig.asConfig.find(path)
    }

    private val options = LinkedHashMap<String, ConfigOption>()
    // Recursively create new options to get to the path
    fun registerOption(path: String, configOption: ConfigOption) {
        val indexOfSplit = path.indexOf("/")

        if (indexOfSplit == -1) {
            options[path] = configOption
            configOption.parent = this
            return
        }

        val firstKey = path.substring(0, indexOfSplit)

        val newConfig = Config()
        options[firstKey] = newConfig
        newConfig.registerOption(path.substring(indexOfSplit), configOption)
    }

    fun getAllOptions(): LinkedHashMap<String, ConfigOption> {
        return options.clone() as LinkedHashMap<String, ConfigOption>
    }

    override fun loadFromJson(element: JsonElement) {
        val obj = element.asJsonObject

        for (option in options.entries) {
            // If the parameter exists
            if (obj.has(option.key)) {
                try {
                    option.value.loadFromJson(obj.get(option.key))

                } catch (e: Exception) {
                    log(Level.ERROR, "Error loading option ${option.key}")
                    throw e
                }
            }
        }
    }

    override fun saveToJson(): JsonElement {
        val obj = JsonObject()

        for (option in options.entries) {
            obj.add(option.key, option.value.saveToJson())
        }
        return obj
    }

    var savePath: String? = null
    fun save() {
        if (parent == null) {
            ConfigManager.saveConfig(savePath ?: throw IllegalArgumentException("Uable to Save. No save path provided. Config is not registered."), this)
        } else {
            (parent as? Config)?.save() ?: throw IllegalArgumentException("Unable to save. Parent of config is not a config.")
        }
    }
}