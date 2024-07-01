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
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedData
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.EmbedField
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.Webhook
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.WebhookData
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSItemRender
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.trueUnformattedMessage
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.asHex
import me.partlysanestudios.partlysaneskies.utils.StringUtils.romanNumeralToInt
import me.partlysanestudios.partlysaneskies.utils.StringUtils.toRoman
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object BestiaryMilestoneWebhook: Webhook() {
    override val icon = PSSItemRender(ItemStack(Items.golden_sword), true)
        .setX(CenterConstraint())
        .setY(CenterConstraint())
        .setWidth(90.percent)

    override val id = "bestiaryMilestone"
    override val name = "Bestiary Milestone"
    override val description = "Send a webhook whenever you earn a new Bestiary Milestone"

    init {
        config.registerOption("multipleOf5", Toggle("Send only multiples of 5", "Only send multiples of 5 (Lvl 5, 10, 15, etc.)", false))
        config.registerOption("multipleOf10", Toggle("Send only multiples of 10", "Only send multiples of 10 (Lvl 10, 20, 30, etc.)", false))
        config.registerOption("useRomanNumerals", Toggle("Use Roman Numerals", "Use Roman Numerals instead of Arabic Numerals in the message", false))
    }


    private var headingMessageSent = false

    private val regex = "§8(\\w+)➡§e(\\w+)".toRegex()
    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        val message = event.message.formattedText
        if (message.contains("§lBESTIARY MILESTONE")) {
            headingMessageSent = true
            return
        }

        if (!regex.containsMatchIn(message)) {
            return
        }

        headingMessageSent = false

        val (oldLevel, newLevel) = regex.find(message)?.destructured ?: return
        val oldLevelInt = oldLevel.romanNumeralToInt()
        val newLevelInt = newLevel.romanNumeralToInt()
        if (config.find("multipleOf5")?.asBoolean == true && newLevelInt % 5 == 0) {
            trigger(oldLevelInt, newLevelInt)
        } else if (config.find("multipleOf10")?.asBoolean == true && newLevelInt % 10 == 0) {
            trigger(oldLevelInt, newLevelInt)
        } else if (config.find("multipleOf5")?.asBoolean == false && config.find("multipleOf10")?.asBoolean == false) {
            trigger(oldLevelInt, newLevelInt)
        }
    }

    private fun trigger(oldLevel: Int, newLevel: Int) {

        val oldLevelString = if (config.find("useRomanNumerals")?.asBoolean == true) {
            oldLevel.toRoman()
        } else {
            oldLevel.toString()
        }

        val newLevelString = if (config.find("useRomanNumerals")?.asBoolean == true) {
            newLevel.toRoman()
        } else {
            newLevel.toString()
        }

        WebhookData(
            url = PartlySaneSkies.config.discordWebhookURL,
            content = " ",
            embedData = listOf(
                EmbedData(
                    title = "Bestiary Level Up!",
                    color = Color(255, 195, 0).asHex,
                    fields = listOf(
                        EmbedField(
                            name = "Milestone",
                            value = ":tada: $oldLevelString ➜ $newLevelString :tada:",
                            inline = true
                        )
                    )
                )
            )
        ).send()
    }
}