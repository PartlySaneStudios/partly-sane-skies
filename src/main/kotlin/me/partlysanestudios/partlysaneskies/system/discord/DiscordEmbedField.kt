package me.partlysanestudios.partlysaneskies.system.discord

data class DiscordEmbedField(
    var name: String,
    var value: String,
    var inline: Boolean = false
)