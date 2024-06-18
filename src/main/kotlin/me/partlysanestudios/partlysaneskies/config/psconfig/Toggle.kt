package me.partlysanestudios.partlysaneskies.config.psconfig

import com.google.gson.JsonElement
import com.google.gson.JsonParser

class Toggle(
    val name: String,
    val description: String = "",
    defaultState: Boolean = false
): ConfigOption() {
    companion object {
        val ConfigOption.asToggle: Toggle
            get() {
                return this as Toggle
            }

        val ConfigOption.asBoolean: Boolean
            get() {
                return this.asToggle.state
            }
    }

    var state = defaultState
    override fun loadFromJson(element: JsonElement) {
        state = element.asBoolean
    }

    override fun saveToJson(): JsonElement {
        return JsonParser().parse(state.toString())
    }


}