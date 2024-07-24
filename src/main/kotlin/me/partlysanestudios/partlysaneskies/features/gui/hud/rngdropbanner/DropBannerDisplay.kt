//
// Written by Su386 & J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity.Companion.getRarityFromColorCode
import me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner.RareDropGUIManager.isAllowedDrop
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.PSSChatEvent
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.MathUtils.onCooldown
import me.partlysanestudios.partlysaneskies.utils.StringUtils.colorCodeToColor
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object DropBannerDisplay {
    var dropToRender: Drop? = null

    // Whenever someone updates the regex, please replace the regex in the following link:
    // Regex: https://regex101.com/r/lPUJeH/8
    private val RARE_DROP_REGEX = "(?:§r)*(?<dropCategoryColor>§.)(?:§.)+(?<dropCategory>[\\w\\s]*[CD]ROP!) (?:§.)+\\(?(?:§.)*(?:\\d+x )?(?:§.)*(?<dropColor>§.)(?<name>◆?[\\s\\w]+)(?:§.)+\\)? ?(?:(?:§.)+)?(?:\\((?:\\+(?:§.)*(?<magicFind>\\d+)% (?:§.)+✯ Magic Find(?:§.)*|[\\w\\s]+)\\))?".toRegex()


    private const val SMALL_TEXT_SCALE = 2.5f
    private const val BIG_TEXT_SCALE = 5f
    private const val BANNER_HEIGHT_FACTOR = 33.3f
    private const val TEXT_SPACING_FACTOR = 0.05f
    private const val TEXT_BLINK_START_FACTOR = 1f / 3f
    private const val TEXT_BLINK_END_FACTOR = 10f / 12f

    var window = Window(ElementaVersion.V5)

    private var topString = ""
    private var dropNameString = ""

    private var topText = UIWrappedText(centered = true).constrain {
        textScale = (BIG_TEXT_SCALE * config.bannerSize).scaledPixels
        width = 100.percent
        x = CenterConstraint()
        y = BANNER_HEIGHT_FACTOR.percent
    } childOf window

    private var dropNameText = UIWrappedText(centered = true).constrain {
        textScale = (SMALL_TEXT_SCALE * config.bannerSize).scaledPixels
        width = 100.percent
        x = CenterConstraint()
        y = PixelConstraint(topText.getBottom() + window.getHeight() * TEXT_SPACING_FACTOR)
    } childOf window

    @SubscribePSSEvent
    fun onChatMessage(event: PSSChatEvent) {
        val formattedMessage = event.message

        val match = RARE_DROP_REGEX.find(formattedMessage) ?: return
        val (dropCategoryColor, dropCategory, dropColor, name, magicFind) = match.destructured

        val rarity = dropColor.getRarityFromColorCode()
        if (checkRarity(rarity)) {
            return
        }

        if (!isAllowedDrop(name.trim())) {
            return
        }

        if (config.rareDropBannerSound) {
            minecraft.thePlayer.playSound("partlysaneskies:rngdropjingle", 100f, 1f)
        }

        val dropCategoryHex = dropCategoryColor.colorCodeToColor()
        val localDrop = Drop(name, dropCategory, dropCategoryHex, rarity, magicFind.toIntOrNull() ?: 0, time)

        if (config.rareDropBanner) {
            dropToRender = localDrop
        }

        if (DropWebhook.enabled) {
            DropWebhook.trigger(localDrop)
        }
    }

    @SubscribeEvent
    fun renderText(event: RenderGameOverlayEvent.Text) {
        val item = dropToRender
        if (item == null) {
            dropNameString = ""
            topString = ""

            return
        }

        var categoryColor = item.dropCategoryColor
        dropNameString = "${item.dropRarity.colorCode}${item.name} ${if (item.magicFind > 0) "§b(+${item.magicFind}% ✯ Magic Find)" else ""}"
        topString = item.dropCategory

        if (
            (time - item.timeDropped > TEXT_BLINK_START_FACTOR * config.rareDropBannerTime * 1000)
            &&
            (time - item.timeDropped < TEXT_BLINK_END_FACTOR * config.rareDropBannerTime * 1000)
        ) {
            categoryColor = if (Math.round((item.timeDropped - time) / 1000f * 4) % 2 == 0) {
                Color.white
            } else {
                item.dropCategoryColor
            }
        }

        if (!onCooldown(item.timeDropped, (config.rareDropBannerTime * 1000).toLong())) {
            dropToRender = null
            return
        }

        if (topText.getText().isEmpty() && topString.isEmpty() && dropNameText.getText().isEmpty() && dropNameString.isEmpty()) {
            return
        }

        val scale = config.bannerSize
        topText
            .setText(topString)
            .setColor(categoryColor)

        dropNameText
            .setText(dropNameString)
            .setY(PixelConstraint(topText.getBottom() + window.getHeight() * (TEXT_SPACING_FACTOR * scale)))

        window.draw(UMatrixStack())
    }

    private fun checkRarity(rarity: Rarity): Boolean {
        return when {
            rarity == Rarity.COMMON && config.blockCommonDrops -> true
            rarity == Rarity.UNCOMMON && config.blockUncommonDrops -> true
            rarity == Rarity.RARE && config.blockRareDrops -> true
            rarity == Rarity.EPIC && config.blockEpicDrops -> true
            rarity == Rarity.LEGENDARY && config.blockLegendaryDrops -> true
            else -> false
        }
    }
}
