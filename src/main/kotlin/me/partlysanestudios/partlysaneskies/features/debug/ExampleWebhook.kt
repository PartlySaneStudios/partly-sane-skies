//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.debug

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UICircle
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedData
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedField
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.WebhookData
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.WebhookEvent
import java.awt.Color

object ExampleWebhook: WebhookEvent() {
    // Optional override
    // In this case, because we have a config option additionally, I decided to sync them
    override var enabled: Boolean
        get() = config.debugSendDiscordWebhook
        set(value) { config.debugSendDiscordWebhook = value }


    // Use events for this. In this case, it's debug key.
    // Debug key event when
    // TODO: Debug key event
    fun trigger() {
        if (!enabled) {
            return
        }

        WebhookData(
            config.discordWebhookURL,
            "Test Content",
            listOf(
                EmbedData(
                    title = "Test Title",
                    description = "Test Description",
                    url = "https://www.google.com",
                    color = 0xFF00FF,
                    fields = listOf(
                        EmbedField(
                            name = "Test Field Name",
                            value = "Test Field Value",
                            inline = true
                        )
                    )
                )
            )
        ).send()
    }

    override val icon: UIComponent = UICircle().setColor(Color(255, 0, 0, 100))
}