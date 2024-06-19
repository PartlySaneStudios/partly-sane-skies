package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asBoolean
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asToggle
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity.Companion.getRarityFromColorCode
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockItem
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedData
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedField
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.Webhook
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.WebhookData
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.asHex
import me.partlysanestudios.partlysaneskies.utils.StringUtils.colorCodeToColor
import java.awt.Color
import java.util.*

object DropWebhook: Webhook() {
    override val icon get() = UIBlock().setColor(Color.green)
    override val id: String = "rngdrop"
    override val name: String = "RNG Drop"
    override val description: String = "Automatically send a Discord message\nwhenever a rare item has dropped"

    init {
        for (rarity in Rarity.entries) {
            if (rarity == Rarity.UNKNOWN) {
                continue
            }
            val displayName = rarity.displayName
            config.registerOption("send$displayName", Toggle("Send $displayName Drops", "Allow the webhook to drops of ${displayName.lowercase()} rarity.", true))
        }
    }
    fun trigger(drop: Drop) {
        if (config.find("enabled")?.asBoolean != true) {
            return
        }

        if (shouldBlockDrop(drop.dropRarity)) {
            return
        }

        val title = drop.dropCategory
        val name = drop.name
        val description = "Magic Find: ${drop.magicFind}"
        WebhookData(
            url = PartlySaneSkies.config.discordWebhookURL,
            content = " ",
            embedData = listOf(
                EmbedData(
                    title = title,
                    color = drop.dropRarity.colorCode.colorCodeToColor().asHex,
                    fields = listOf(
                        EmbedField(
                            name = name,
                            value = description,
                            inline = true
                        )
                    )
                )
            )
        ).send()
    }

    private fun shouldBlockDrop(rarity: Rarity): Boolean {
        return config.find("send${rarity.displayName}")?.asBoolean == true
    }
}