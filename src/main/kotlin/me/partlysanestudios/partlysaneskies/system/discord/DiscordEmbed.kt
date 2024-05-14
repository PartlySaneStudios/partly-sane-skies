package me.partlysanestudios.partlysaneskies.system.discord

data class DiscordEmbed(
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var color: Int? = null,
    var fields: List<DiscordEmbedField>? = null
)