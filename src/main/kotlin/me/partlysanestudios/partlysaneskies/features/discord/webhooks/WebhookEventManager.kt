//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.discord.webhooks

object WebhookEventManager {
    val webhookEvents = ArrayList<Webhook>()

    fun registerWebhook(webhookEvent: Webhook) {
        webhookEvents.add(webhookEvent)
    }
}
