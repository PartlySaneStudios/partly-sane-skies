package me.partlysanestudios.partlysaneskies.config.psconfig

import com.google.gson.JsonElement
import java.util.LinkedList

abstract class ConfigOption {

    abstract fun loadFromJson(element: JsonElement)
    abstract fun saveToJson(): JsonElement

    open var parent: ConfigOption? = null
}