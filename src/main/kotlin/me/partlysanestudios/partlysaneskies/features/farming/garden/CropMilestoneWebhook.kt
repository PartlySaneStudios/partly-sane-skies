//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.farming.garden

import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.percent
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asBoolean
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedData
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedField
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.Webhook
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.WebhookData
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSItemRender
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.asHex
import me.partlysanestudios.partlysaneskies.utils.StringUtils.romanNumeralToInt
import me.partlysanestudios.partlysaneskies.utils.StringUtils.toRoman
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object CropMilestoneWebhook : Webhook() {
    override val icon =
        PSSItemRender(ItemStack(Items.reeds), true)
            .setX(CenterConstraint())
            .setY(CenterConstraint())
            .setWidth(90.percent)

    override val id = "cropMilestone"
    override val name = "Crop Milestone"
    override val description = "Send a webhook whenever you level to a new crop milestone"

    init {
        config.registerOption("multipleOf5", Toggle("Send only multiples of 5", "Only send multiples of 5 (Lvl 5, 10, 15, etc.)", false))
        config.registerOption(
            "multipleOf10",
            Toggle("Send only multiples of 10", "Only send multiples of 10 (Lvl 10, 20, 30, etc.)", false),
        )
        config.registerOption(
            "useRomanNumerals",
            Toggle("Use Roman Numerals", "Use Roman Numerals instead of Arabic Numerals in the message", false),
        )
    }

    val regex = "§lGARDEN MILESTONE §3(\\w+[\\s\\w+]*) §8(\\w+)➜§3(\\w+)".toRegex()

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if (!enabled) return

        val message = event.message.formattedText

        val (crop, oldLevel, newLevel) = regex.find(message)?.destructured ?: return
        val oldLevelInt =
            if ("\\d+".toRegex().containsMatchIn(oldLevel)) {
                oldLevel.toIntOrNull() ?: 0
            } else {
                oldLevel.romanNumeralToInt()
            }

        val newLevelInt =
            if ("\\d+".toRegex().containsMatchIn(newLevel)) {
                newLevel.toIntOrNull() ?: 0
            } else {
                newLevel.romanNumeralToInt()
            }

        if (config.find("multipleOf5")?.asBoolean == true && newLevelInt % 5 == 0) {
            trigger(crop, oldLevelInt, newLevelInt)
        } else if (config.find("multipleOf10")?.asBoolean == true && newLevelInt % 10 == 0) {
            trigger(crop, oldLevelInt, newLevelInt)
        } else if (config.find("multipleOf5")?.asBoolean == false && config.find("multipleOf10")?.asBoolean == false) {
            trigger(crop, oldLevelInt, newLevelInt)
        }
    }

    private fun trigger(crop: String, oldLevel: Int, newLevel: Int) {
        val oldLevelString =
            if (config.find("useRomanNumerals")?.asBoolean == true) {
                oldLevel.toRoman()
            } else {
                oldLevel.toString()
            }

        val newLevelString =
            if (config.find("useRomanNumerals")?.asBoolean == true) {
                newLevel.toRoman()
            } else {
                newLevel.toString()
            }

        WebhookData(
            url = PartlySaneSkies.config.discordWebhookURL,
            content = " ",
            embedData =
                listOf(
                    EmbedData(
                        title = "Garden Milestone!",
                        color = Color(125, 255, 125).asHex,
                        fields =
                            listOf(
                                EmbedField(
                                    name = crop,
                                    value = ":tada: $oldLevelString ➜ $newLevelString :tada:",
                                ),
                            ),
                    ),
                ),
        ).send()
    }
}
