//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.discord.webhooks

data class EmbedField(
    var name: String,
    var value: String,
    var inline: Boolean = false
)