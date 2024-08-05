package me.partlysanestudios.partlysaneskies.features.items.rngdrop

import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asBoolean
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedData
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedField
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.Webhook
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.WebhookData
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSItemRender
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.asHex
import me.partlysanestudios.partlysaneskies.utils.StringUtils.colorCodeToColor
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import java.awt.Color

object DropWebhook : Webhook() {
    override val icon
        get() = PSSItemRender(ItemStack(Items.gold_ingot), true).constrain {
            width = 80.percent
            height = 80.percent
            x = CenterConstraint()
            y = CenterConstraint()
        }

    override val id: String = "rngdrop"
    override val name: String = "RNG Drop"
    override val description: String = "Automatically send a Discord message\nwhenever a rare item has dropped"

    init {
        val rarities = arrayOf(
            Rarity.COMMON,
            Rarity.UNCOMMON,
            Rarity.RARE,
            Rarity.EPIC,
            Rarity.LEGENDARY,
            Rarity.MYTHIC,
            Rarity.DIVINE
        )
        for (rarity in rarities) {
            val displayName = rarity.displayName
            config.registerOption(
                "send$displayName",
                Toggle(
                    "Send $displayName Drops",
                    "Allow the webhook to send drops of ${displayName.lowercase()} rarity.",
                    true
                ),
            )
        }
    }

    fun trigger(drop: Drop) {
        if (!enabled) return

        if (shouldBlockDrop(drop.dropRarity)) {
            return
        }

        val title = drop.dropCategory
        val name = drop.name
        val description = "${drop.magicFind}% ✯ Magic Find!"

        val color = if (drop.dropRarity == Rarity.UNKNOWN) Color.white.asHex else drop.dropRarity.colorCode.colorCodeToColor().asHex

        WebhookData(
            url = PartlySaneSkies.config.discordWebhookURL,
            content = " ",
            embedData = listOf(
                EmbedData(
                    title = title,
                    color = color,
                    fields =
                    listOf(
                        EmbedField(
                            name = name,
                            value = description,
                            inline = true,
                        ),
                    ),
                ),
            ),
        ).send()
    }

    private fun shouldBlockDrop(rarity: Rarity) = config.find("send${rarity.displayName}")?.asBoolean != true
}
