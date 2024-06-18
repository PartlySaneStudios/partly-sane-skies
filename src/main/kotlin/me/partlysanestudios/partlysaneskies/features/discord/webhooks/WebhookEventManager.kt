//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.discord.webhooks

import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import org.apache.logging.log4j.Level
import java.util.LinkedList

object WebhookEventManager {
    val webhookEvents = ArrayList<WebhookEvent>()

    fun registerWebhook(webhookEvent: WebhookEvent) {
        webhookEvents.add(webhookEvent)
    }
}