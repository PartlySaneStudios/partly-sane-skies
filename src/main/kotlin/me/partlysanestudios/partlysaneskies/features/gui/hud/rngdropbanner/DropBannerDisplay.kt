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

class DropBannerDisplay() {
    var SMALL_TEXT_SCALE = 2.5f
    var BIG_TEXT_SCALE = 5f

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        val formattedMessage = event.message.formattedText
        if (!isRareDrop(formattedMessage)) {
            return
        }
        if (config.rareDropBannerSound) {
            minecraft.thePlayer.playSound("partlysaneskies:rngdropjingle", 100f, 1f)
        }
        if (config.rareDropBanner) {
            val unformattedMessage = event.message.unformattedText

            // Gets the name of the drop category
            val dropCategory = unformattedMessage.substring(0, unformattedMessage.indexOf("! ") + 1)

            // Gets the colour of the drop category
            val dropCategoryHex = colorCodeToColor(formattedMessage.substring(2, 4))

            // // Finds the amount of magic find from the message
            val name = formattedMessage.substring(formattedMessage.indexOf("! ") + 2)
            drop = Drop(name, dropCategory, 1, time, dropCategoryHex)
        }
    }

    var window = Window(ElementaVersion.V2)
    var topString = "empty"
    var dropNameString = "empty"
    var magicFindString = "empty" // Apparently we never use the magic find string
    var topText = UIWrappedText(dropNameString, true, Color(0, 0, 0, 0), true)
        .setTextScale(PixelConstraint(BIG_TEXT_SCALE / 672 * window.getHeight() * config.bannerSize))
        .setWidth(PixelConstraint(window.getWidth()))
        .setX(CenterConstraint())
        .setY(PixelConstraint(window.getHeight() * .333f))
        .setChildOf(window) as UIWrappedText
    var dropNameText = UIWrappedText(dropNameString, true, Color(0, 0, 0, 0), true)
        .setTextScale(PixelConstraint(SMALL_TEXT_SCALE / 672 * window.getHeight() * config.bannerSize))
        .setWidth(PixelConstraint(window.getWidth()))
        .setX(CenterConstraint())
        .setY(PixelConstraint(topText.getBottom() + window.getHeight() * .05f))
        .setChildOf(window) as UIWrappedText

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
            dropNameString = "x" + drop!!.amount + " " + drop!!.name
            topString = drop!!.dropCategory

            // It should blink after a third of the rare drop time, and before 5/6ths
            if ((time - drop!!.timeDropped > 1f / 3f * config.rareDropBannerTime * 1000
                        && time
                        - drop!!.timeDropped < (10f / 12f * config.rareDropBannerTime * 1000))
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
            .setY(PixelConstraint(window.getHeight() * .3f))
            .setColor((categoryColor)!!)
        dropNameText
            .setText(dropNameString)
            .setTextScale(PixelConstraint((SMALL_TEXT_SCALE / 672) * window.getHeight() * scale))
            .setWidth(PixelConstraint(window.getWidth()))
            .setX(CenterConstraint())
            .setY(PixelConstraint(topText.getBottom() + window.getHeight() * (.05f * scale)))
        window.draw(UMatrixStack())
    }

    companion object {
        var drop: Drop? = null
        fun isRareDrop(formattedMessage: String): Boolean {
            // §6§lRARE DROP! §r§fCarrot §r§b(+§r§b130% §r§b✯ Magic Find§r§b)
            val regex = Regex("(§.)+(\\w)*(RARE|PET) DROP!.*")
            return formattedMessage.matches(regex.toString().toRegex())
        }
    }
}
