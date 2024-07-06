//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.debug

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UICircle
import gg.essential.elementa.dsl.percent
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedData
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedField
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.WebhookData
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.Webhook
import java.awt.Color

object ExampleWebhook: Webhook() {
    // Use events for this. In this case, it's debug key.
    // Debug key event when
    // TODO: Debug key event
    fun trigger() {
        if (!enabled) {
            return
        }

        WebhookData(
            PartlySaneSkies.config.discordWebhookURL,
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

    override val icon: UIComponent = UICircle().setColor(Color(255, 0, 0, 100)).setRadius(75.percent)
    override val id = "example"
    override val name = "Example Webhook"
    override val description = ""
    override val hidden = true

    init {
        config.registerOption("testOption", Toggle("Test option", "This is a test description", false))
        config.registerOption("testOption2", Toggle("Test option 2", "This is a test description for 2", false))
        config.registerOption("testOption3", Toggle("Test option 3", "This is a test description for option 3", false))
    }
}