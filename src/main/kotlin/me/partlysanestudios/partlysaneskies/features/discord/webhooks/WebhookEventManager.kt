//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.discord.webhooks

import java.util.LinkedList

object WebhookEventManager {
    val webhookEvents: MutableList<WebhookEvent> = LinkedList()
    fun registerWebhook(webhookEvent: WebhookEvent) {
        webhookEvents.add(webhookEvent)

        if (!webhookEvent.hidden) {
            WebhookMenu.webhookEvents.add(webhookEvent)
        }
    }
}