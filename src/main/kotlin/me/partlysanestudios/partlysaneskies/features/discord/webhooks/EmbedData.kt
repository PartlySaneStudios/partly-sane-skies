//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.discord.webhooks

data class EmbedData(
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var color: Int? = null,
    var fields: List<EmbedField>? = null,
)
