//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.discord.webhooks

import gg.essential.elementa.UIComponent
import me.partlysanestudios.partlysaneskies.config.psconfig.Config
import me.partlysanestudios.partlysaneskies.config.psconfig.ConfigManager
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asBoolean
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asToggle

abstract class Webhook(defaultEnabledState: Boolean = false) {
    companion object {
        private const val WEBHOOK_FOLDER_PATH = "webhooks/"
    }
    var enabled
        get() = config.find("enabled")?.asBoolean ?: false
        set(value) { config.find("enabled")?.asToggle?.state = value }

    abstract val icon: UIComponent
    abstract val id: String
    abstract val name: String
    abstract val description: String
    val config: Config = Config()
    open val hidden = false


    init {
        config.registerOption("enabled", Toggle("Enabled", description = "Enable the webhook", defaultState = defaultEnabledState))
    }


    // Registers the config and the webhook
    fun register() {
        ConfigManager.registerNewConfig(WEBHOOK_FOLDER_PATH + id, config)
        WebhookEventManager.registerWebhook(this)
    }
}