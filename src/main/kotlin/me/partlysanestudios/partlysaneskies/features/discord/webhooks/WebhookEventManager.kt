//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.discord.webhooks

import java.util.LinkedList

object WebhookEventManager {
    private val webhookEvents: MutableList<WebhookEvent> = LinkedList()
    fun registerWebhook(webhookEvent: WebhookEvent) {
        webhookEvents.add(webhookEvent)
    }
}