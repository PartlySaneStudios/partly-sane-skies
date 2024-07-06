//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.discord.webhooks

import me.partlysanestudios.partlysaneskies.data.api.PostRequest
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import java.net.URL

data class WebhookData(
    var url: String,
    var content: String? = null,
    var embedData: List<EmbedData>? = null,
) {
    fun send() {
        if (content == null && embedData == null) {
            throw IllegalArgumentException("Either content or embeds must be set")
        }

        if (url.isEmpty()) {
            ChatUtils.sendClientMessage("Discord Webhook URL is not set")
            return
        }

        val urlObj =
            try {
                URL(url)
            } catch (expt: Exception) {
                ChatUtils.sendClientMessage("Discord Webhook URL is invalid")
                return
            }

        RequestsManager.newRequest(
            PostRequest(
                urlObj,
                {
                    if (!it.hasSucceeded()) {
                        ChatUtils.sendClientMessage("Discord Webhook failed to send\nCopied to clipboard")
                        SystemUtils.copyStringToClipboard(WebhookSerializer.serialize(this))
                        return@PostRequest
                    }
                    ChatUtils.sendClientMessage("Discord Webhook sent")
                },
                WebhookSerializer.serialize(this),
                false,
                false,
                true,
            ),
        )
    }
}
