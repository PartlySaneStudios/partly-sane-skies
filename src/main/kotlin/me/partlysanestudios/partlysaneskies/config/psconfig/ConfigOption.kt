//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.config.psconfig

import com.google.gson.JsonElement

abstract class ConfigOption {

    abstract fun loadFromJson(element: JsonElement)
    abstract fun saveToJson(): JsonElement

    open var parent: ConfigOption? = null
}