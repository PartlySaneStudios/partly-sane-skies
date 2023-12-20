//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.system

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.ImageUtils
import me.partlysanestudios.partlysaneskies.utils.MathUtils

import net.minecraft.client.gui.Gui
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object BannerRenderer: Gui() {
    private var bannerList = ArrayList<PSSBanner>()

    var window = Window(ElementaVersion.V2)

    private var displayText: UIText = UIText("{EMPTY BANNER}")
        .constrain {
            textScale = 5F.pixels
            x = CenterConstraint()
            y = CenterConstraint()
            color = Color(255, 255, 255, 0).constraint

        }.setColor(Color(255, 255, 255, 0)) as UIText childOf window


    @SubscribeEvent
    fun onScreenRender(event: RenderGameOverlayEvent.Text)  {
        if (bannerList.isEmpty()) {
            return
        }

        cleanOutOfDateBanners()


        val bannerToRender = getMostRecentlyCreatedBanner()

        if (bannerToRender.text == "") {
            return
        }

        if (displayText.getText() != bannerToRender.text) {
            displayText.setText(bannerToRender.text)
                .constrain {
                    textScale = bannerToRender.textScale.pixels
                    x = CenterConstraint()
                    y = (window.getHeight() * .125f).pixels
                    color = bannerToRender.getFadedColor().constraint
                } childOf window
        }

//        ChatUtils.sendClientMessage(bannerToRender.getFadedColor().alpha.toString())
        displayText.setColor(bannerToRender.getFadedColor())

        window.draw(UMatrixStack())


        //while (bannerToRender.isOutOfDate()) {
        //    bannerList.remove(bannerToRender)
        //}
    }

    private fun cleanOutOfDateBanners() {
        for (i in bannerList.size - 1 downTo 0) {
            val banner = bannerList[i]

            if (banner.isOutOfDate()) {
//                ChatUtils.sendClientMessage("Banner: ${banner.text} is out of date ${banner.renderStartTime}, ${banner.lengthOfTimeToRender}")
                bannerList.removeAt(i)
            }

        }
    }

    private fun getMostRecentlyCreatedBanner(): PSSBanner {
        if (bannerList.isEmpty()) {
            return PSSBanner("", 0)
        }

        var mostRecentlyCreatedBanner = bannerList[0]

        for (banner in bannerList) {
            if (mostRecentlyCreatedBanner.creationTime < banner.creationTime) {
                mostRecentlyCreatedBanner = banner
            }
        }

        return mostRecentlyCreatedBanner
    }

    fun renderNewBanner(banner: PSSBanner) {
        banner.setRenderStartTimeNow()
        bannerList.add(banner)
    }
}

class PSSBanner(val text: String, val lengthOfTimeToRender: Long, val textScale: Float = 5f, private val color: Color = Color.red) {
    val creationTime = PartlySaneSkies.getTime()

    var renderStartTime = -1L

    fun setRenderStartTimeNow() {
        this.renderStartTime = PartlySaneSkies.getTime()
    }

    fun isOutOfDate(): Boolean {
        return !MathUtils.onCooldown(renderStartTime, lengthOfTimeToRender)
    }
    fun hasStartedRendering(): Boolean {
        return renderStartTime == -1L
    }

    fun getFadedColor(): Color {
        val alpha = getAlpha(renderStartTime, lengthOfTimeToRender / 1000.0).toInt()


        val color = ImageUtils.applyOpacityToColor(color, alpha)
//        ChatUtils.sendClientMessage("${alpha},  ${color.alpha}")

        return color
    }

    private fun getAlpha(timeStarted: Long, displayLengthSeconds: Double): Short {
        val displayLength = displayLengthSeconds * 1000

//        ChatUtils.sendClientMessage("DisplayLengthSeconds${displayLength}")
//        ChatUtils.sendClientMessage("DisplayLength${displayLength}")

        val fadeLength = displayLength * (1 / 6.0)
        val timeSinceStarted = PartlySaneSkies.getTime() - timeStarted

//        ChatUtils.sendClientMessage(fadeLength.toString())
        return if (0 > timeSinceStarted) {
//            ChatUtils.sendClientMessage("Less than 0")
            0
        } else if (0 < timeSinceStarted && timeSinceStarted < fadeLength) {
//            ChatUtils.sendClientMessage("Fading")
            Math.round(timeSinceStarted / fadeLength * 255).toShort()
        } else if (fadeLength < timeSinceStarted && timeSinceStarted <= displayLength - fadeLength) {
//            ChatUtils.sendClientMessage("normal")
            255
        } else if (displayLength - fadeLength < timeSinceStarted && timeSinceStarted <= displayLength) {
//            ChatUtils.sendClientMessage("end fade")
            Math.round((-timeSinceStarted + displayLength) / fadeLength * 255).toShort()
        } else {
//            ChatUtils.sendClientMessage("none")
            0
        }
    }

}


