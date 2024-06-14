//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.discord.webhooks

object WebhookSerializer {
    fun serialize(webhook: WebhookData): String {
        val builder = StringBuilder()
        builder.append("{")
        if (webhook.content != null) {
            builder.append("\"content\":\"${webhook.content}\"")
        }
        if (webhook.embedData != null) {
            if (webhook.content != null) {
                builder.append(",")
            }
            builder.append("\"embeds\":[")
            for (i in webhook.embedData!!.indices) {
                builder.append(DiscordEmbedSerializer.serialize(webhook.embedData!![i]))
                if (i != webhook.embedData!!.size - 1) {
                    builder.append(",")
                }
            }
            builder.append("]")
        }
        builder.append("}")
        return builder.toString()
    }

    object DiscordEmbedSerializer {
            fun serialize(embedData: EmbedData): String {
                val builder = StringBuilder()
                builder.append("{")
                if (embedData.title != null) {
                    builder.append("\"title\":\"${embedData.title}\"")
                }
                if (embedData.description != null) {
                    if (embedData.title != null) {
                        builder.append(",")
                    }
                    builder.append("\"description\":\"${embedData.description}\"")
                }
                if (embedData.url != null) {
                    if (embedData.title != null || embedData.description != null) {
                        builder.append(",")
                    }
                    builder.append("\"url\":\"${embedData.url}\"")
                }
                if (embedData.color != null) {
                    if (embedData.title != null || embedData.description != null || embedData.url != null) {
                        builder.append(",")
                    }
                    builder.append("\"color\":${embedData.color!!}")
                }
                if (embedData.fields != null) {
                    if (embedData.title != null || embedData.description != null || embedData.url != null || embedData.color != null) {
                        builder.append(",")
                    }
                    builder.append("\"fields\":[")
                    for (i in embedData.fields!!.indices) {
                        builder.append(DiscordEmbedFieldSerializer.serialize(embedData.fields!![i]))
                        if (i != embedData.fields!!.size - 1) {
                            builder.append(",")
                        }
                    }
                    builder.append("]")
                }
                builder.append("}")
                return builder.toString()
            }
    }

    object DiscordEmbedFieldSerializer {
            fun serialize(field: EmbedField): String {
                val builder = StringBuilder()
                builder.append("{")
                builder.append("\"name\":\"${field.name}\"")
                builder.append(",")
                builder.append("\"value\":\"${field.value}\"")
                builder.append(",")
                builder.append("\"inline\":${field.inline}")
                builder.append("}")
                return builder.toString()
            }
    }
}