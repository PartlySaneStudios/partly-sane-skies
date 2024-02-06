//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.utils.MathUtils.onCooldown
import me.partlysanestudios.partlysaneskies.utils.StringUtils.colorCodeToColor
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

class DropBannerDisplay {
    companion object {
        var drop: Drop? = null

        const val SMALL_TEXT_SCALE = 2.5f
        const val BIG_TEXT_SCALE = 5f
        private const val BANNER_HEIGHT_FACTOR = 0.333f
        private const val TEXT_SPACING_FACTOR = 0.05f
        private const val TEXT_BLINK_START_FACTOR = 1f / 3f
        private const val TEXT_BLINK_END_FACTOR = 10f / 12f

        // https://regex101.com/r/lPUJeH/1
        val RARE_DROP_REGEX = "/(?:§.)+(?<dropTitle>[\\w\\s]*[CD]ROP!) (?:§.)+(?:\\()?(?:§.)*(?:\\d+x )?(?:§.)*(?<dropColor>§.)(?<dropName>[\\s\\w]+)(?:§.)+(?:\\((?:\\+(?:§.)*(?<mf>\\d+)% (?:§.)+✯ Magic Find(?:§.)*|[\\w\\s]+)\\)|\\))?/gm".toRegex()
    }

    var window = Window(ElementaVersion.V2)

    var topString = "empty"
    var dropNameString = "empty"
    var magicFindString = "empty"

    var topText = UIWrappedText(dropNameString, true, Color(0, 0, 0, 0), true).apply {
        setTextScale(PixelConstraint(BIG_TEXT_SCALE / 672 * window.getHeight() * config.bannerSize))
        setWidth(PixelConstraint(window.getWidth()))
        setX(CenterConstraint())
        setY(PixelConstraint(window.getHeight() * BANNER_HEIGHT_FACTOR))
        setChildOf(window)
    }
    var dropNameText = UIWrappedText(dropNameString, true, Color(0, 0, 0, 0), true).apply {
        setTextScale(PixelConstraint(SMALL_TEXT_SCALE / 672 * window.getHeight() * config.bannerSize))
        setWidth(PixelConstraint(window.getWidth()))
        setX(CenterConstraint())
        setY(PixelConstraint(topText.getBottom() + window.getHeight() * TEXT_SPACING_FACTOR))
        setChildOf(window)
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        val formattedMessage = event.message.formattedText
        val unformattedMessage = event.message.unformattedText

        if (!isRareDrop(formattedMessage)) {
            return
        }

        // TODO: add check for blocked drop

        // TODO: add check for disallowed rarity of drop

        if (config.rareDropBannerSound) {
            minecraft.thePlayer.playSound("partlysaneskies:rngdropjingle", 100f, 1f)
        }

        if (config.rareDropBanner) {
            val dropCategory = unformattedMessage.substring(0, unformattedMessage.indexOf("! ") + 1)
            val dropCategoryHex = colorCodeToColor(formattedMessage.substring(2, 4))
            val name = formattedMessage.substring(formattedMessage.indexOf("! ") + 2)
            drop = Drop(name, dropCategory, time, dropCategoryHex)
        }
    }

    @SubscribeEvent
    fun renderText(event: RenderGameOverlayEvent.Text?) {
        var categoryColor: Color?
        if (drop == null) {
            dropNameString = ""
            topString = ""
            magicFindString = ""
            categoryColor = Color(255, 255, 255, 0)
        } else {
            categoryColor = drop!!.dropCategoryColor
            dropNameString = drop!!.name
            topString = drop!!.dropCategory

            if ((time - drop!!.timeDropped > TEXT_BLINK_START_FACTOR * config.rareDropBannerTime * 1000)
                && (time - drop!!.timeDropped < TEXT_BLINK_END_FACTOR * config.rareDropBannerTime * 1000)
            ) {
                categoryColor = if (Math.round((drop!!.timeDropped - time) / 1000f * 4) % 2 == 0) {
                    Color.white
                } else {
                    drop!!.dropCategoryColor
                }
            }

            if (!onCooldown(drop!!.timeDropped, (config.rareDropBannerTime * 1000).toLong())) {
                drop = null
            }
        }

        if (topText.getText().isEmpty() && topString.isEmpty() && dropNameText.getText()
                .isEmpty() && dropNameString.isEmpty()
        ) {
            return
        }

        val scale = config.bannerSize
        topText
            .setText(topString)
            .setTextScale(PixelConstraint((BIG_TEXT_SCALE / 672) * window.getHeight() * scale))
            .setWidth(PixelConstraint(window.getWidth()))
            .setX(CenterConstraint())
            .setY(PixelConstraint(window.getHeight() * BANNER_HEIGHT_FACTOR))
            .setColor(categoryColor!!)
        dropNameText
            .setText(dropNameString)
            .setTextScale(PixelConstraint((SMALL_TEXT_SCALE / 672) * window.getHeight() * scale))
            .setWidth(PixelConstraint(window.getWidth()))
            .setX(CenterConstraint())
            .setY(PixelConstraint(topText.getBottom() + window.getHeight() * (TEXT_SPACING_FACTOR * scale)))
        window.draw(UMatrixStack())
    }


    private fun isRareDrop(formattedMessage: String): Boolean {
        return formattedMessage.matches(RARE_DROP_REGEX)
    }
}