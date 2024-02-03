package me.partlysanestudios.partlysaneskies.system.discord

object DiscordWebhookSerializer {
    fun serialize(webhook: DiscordWebhook): String {
        val builder = StringBuilder()
        builder.append("{")
        if (webhook.content != null) {
            builder.append("\"content\":\"${webhook.content}\"")
        }
        if (webhook.embeds != null) {
            if (webhook.content != null) {
                builder.append(",")
            }
            builder.append("\"embeds\":[")
            for (i in webhook.embeds!!.indices) {
                builder.append(DiscordEmbedSerializer.serialize(webhook.embeds!![i]))
                if (i != webhook.embeds!!.size - 1) {
                    builder.append(",")
                }
            }
            builder.append("]")
        }
        builder.append("}")
        return builder.toString()
    }

    object DiscordEmbedSerializer {
            fun serialize(embed: DiscordEmbed): String {
                val builder = StringBuilder()
                builder.append("{")
                if (embed.title != null) {
                    builder.append("\"title\":\"${embed.title}\"")
                }
                if (embed.description != null) {
                    if (embed.title != null) {
                        builder.append(",")
                    }
                    builder.append("\"description\":\"${embed.description}\"")
                }
                if (embed.url != null) {
                    if (embed.title != null || embed.description != null) {
                        builder.append(",")
                    }
                    builder.append("\"url\":\"${embed.url}\"")
                }
                if (embed.color != null) {
                    if (embed.title != null || embed.description != null || embed.url != null) {
                        builder.append(",")
                    }
                    builder.append("\"color\":${embed.color!!}")
                }
                if (embed.fields != null) {
                    if (embed.title != null || embed.description != null || embed.url != null || embed.color != null) {
                        builder.append(",")
                    }
                    builder.append("\"fields\":[")
                    for (i in embed.fields!!.indices) {
                        builder.append(DiscordEmbedFieldSerializer.serialize(embed.fields!![i]))
                        if (i != embed.fields!!.size - 1) {
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
            fun serialize(field: DiscordEmbedField): String {
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