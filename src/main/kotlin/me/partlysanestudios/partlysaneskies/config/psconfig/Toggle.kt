//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.config.psconfig

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.config.psconfig.Config.Companion.asConfig

class Toggle(
    val name: String,
    val description: String = "",
    defaultState: Boolean = false
): ConfigOption() {
    companion object {
        val ConfigOption.asToggle get() = this as Toggle
        val ConfigOption.asBoolean get() = this.asToggle.state
    }

    private var cachedState = defaultState
    var state: Boolean get() = cachedState
        set(value) {
            cachedState = value
            parent?.asConfig?.save()
        }

    override fun loadFromJson(element: JsonElement) {
        state = element.asBoolean
    }

    override fun saveToJson(): JsonElement {
        return JsonParser().parse(state.toString())
    }


}