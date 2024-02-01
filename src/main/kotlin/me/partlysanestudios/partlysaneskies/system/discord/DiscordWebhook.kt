package me.partlysanestudios.partlysaneskies.system.discord

import kotlinx.serialization.json.Json
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import org.apache.logging.log4j.Level
import java.io.DataOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


data class DiscordWebhook(
    var url: String,
    var content: String? = null,
    var embeds: List<DiscordEmbed>? = null
) {
    fun send() {
        if (content == null && embeds == null) {
            throw IllegalArgumentException("Either content or embeds must be set")
        }

        if (url.isEmpty()) {
            ChatUtils.sendClientMessage("Discord Webhook URL is not set")
            return
        }

        val urlObj = try {
            URL(url)
        } catch (expt: Exception) {
            ChatUtils.sendClientMessage("Discord Webhook URL is invalid")
            return
        }


    }
}
