//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.discord.webhooks

import gg.essential.elementa.UIComponent

abstract class WebhookEvent {
    open var enabled = false
    abstract val icon: UIComponent

    fun register() {
        WebhookEventManager.registerWebhook(this)
    }
}