//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.skills

import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.percent
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asBoolean
import me.partlysanestudios.partlysaneskies.data.cache.PetData
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity.Companion.getRarityFromColorCode
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedData
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedField
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.Webhook
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.WebhookData
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSItemRender
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.trueUnformattedMessage
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.asHex
import me.partlysanestudios.partlysaneskies.utils.StringUtils.colorCodeToColor
import me.partlysanestudios.partlysaneskies.utils.StringUtils.romanNumeralToInt
import me.partlysanestudios.partlysaneskies.utils.StringUtils.toRoman
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object PetLevelUpWebhook: Webhook() {
    override val icon = PSSItemRender(ItemStack(Items.bone), true)
        .setX(CenterConstraint())
        .setY(CenterConstraint())
        .setWidth(90.percent)

    override val id = "skillLevelUp"
    override val name = "Pet Level Up"
    override val description = "Send a webhook whenever you level up a pet"

    init {
        config.registerOption("multipleOf10", Toggle("Send only multiples of 10", "Only send multiples of 10 (Lvl 10, 20, 30, etc.)", false))
        config.registerOption("level100", Toggle("Send only when pet reaches level 100", "Only send a webhook when the pet reaches level 100", false))
        config.registerOption("useRomanNumerals", Toggle("Use Roman Numerals", "Use Roman Numerals instead of Arabic Numerals in the message", false))

        val rarities = arrayOf(Rarity.COMMON, Rarity.UNCOMMON, Rarity.RARE, Rarity.EPIC, Rarity.LEGENDARY, Rarity.MYTHIC)
        for (rarity in rarities) {
            val displayName = rarity.displayName
            config.registerOption("send$displayName", Toggle("Send $displayName Pet", "Allow the webhook to send level ups for pets of ${displayName.lowercase()} rarity.", true))
        }
    }

    val regex = "§r§aYour §r(§.)((\\w+(\\s\\w+)*)( ✦)?) §r§aleveled up to level §r§9(\\d+)§r§a!§r".toRegex()
    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        val message = event.message.formattedText

        if (!regex.containsMatchIn(message)) {
            return
        }

        regex.find(message)?.let {
            val colorCode = it.groupValues[1]
            val petName = it.groupValues[2]
            val level = it.groupValues[6].toInt()

            val rarity = colorCode.getRarityFromColorCode()

            if (config.find("send${rarity.displayName}")?.asBoolean != true) {
                return
            }

            if (config.find("level100")?.asBoolean == true && level == 100) {
                trigger(petName, level, rarity)
            } else if (config.find("multipleOf10")?.asBoolean == true && level % 10 == 0) {
                trigger(petName, level, rarity)
            } else if (config.find("level100")?.asBoolean == false && config.find("multipleOf10")?.asBoolean == false) {
                trigger(petName, level, rarity)
            }
        }
    }

    private fun trigger(name: String, level: Int, rarity: Rarity) {
        val levelString = if (config.find("useRomanNumerals")?.asBoolean == true) {
            level.toRoman()
        } else {
            level.toString()
        }

        WebhookData(
            url = PartlySaneSkies.config.discordWebhookURL,
            content = " ",
            embedData = listOf(
                EmbedData(
                    title = "Pet Level Up!",
                    color = rarity.colorCode.colorCodeToColor().asHex,
                    fields = listOf(
                        EmbedField(
                            name = "${rarity.displayName} $name",
                            value = ":tada: $levelString :tada:",
                            inline = true
                        )
                    )
                )
            )
        ).send()
    }
}