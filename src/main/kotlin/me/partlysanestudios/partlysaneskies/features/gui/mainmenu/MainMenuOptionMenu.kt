//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.gui.mainmenu

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.main
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.uiImage
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.full.primaryConstructor

class MainMenuOptionMenu(nextRunnable: Runnable): WindowScreen(ElementaVersion.V5) {

    private val backgroundBox = UIBlock().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent
        height = 100.percent
        color = Color(0, 0, 0).constraint
    } childOf window

    private val backgroundImage = ResourceLocation("partlysaneskies", "textures/gui/main_menu/image_3_blurred.png").uiImage.constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent
        height = 100.percent
    } childOf backgroundBox

    private val transparentBlock = UIBlock().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent
        height = 100.percent
        color = Color(0, 0, 0, 0).constraint
    } childOf backgroundImage

    private val darkerRectangle = UIRoundedRectangle(7.5f).constrain {
        x = CenterConstraint()
        y = CenterConstraint() - 7.25.percent
        width = 60.percent
        height = 54.percent
        color = Color(15, 15, 15, 115).constraint
    } childOf transparentBlock

    private val headingText = UIWrappedText("Keep the new main menu?", centered = true).constrain {
        x = CenterConstraint()
        y = 30.percent
        width = 40.percent
        textScale = 2.scaledPixels
        color = Color(100, 196, 255).constraint
    } childOf transparentBlock

    private val subheadingText = UIWrappedText("You can always disable it later, or find other backgrounds later on in the Partly Sane Skies config. (/pssc)", centered = true).constrain {
        x = CenterConstraint()
        y = 35.percent
        width = 50.percent
        textScale = 1.scaledPixels
        color = Color.white.constraint
    } childOf transparentBlock

    private val yesButton = UIRoundedRectangle(5.0f).constrain {
        x = CenterConstraint() - 12.percent
        y = 40.percent
        width = 20.percent
        height = 25.percent
        color = Color(90, 150, 100, 90).constraint
    }.onMouseClickConsumer {
        config.customMainMenu = true
        nextRunnable.run()
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(200, 200, 200))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    }  childOf transparentBlock

    private val yesText = UIWrappedText("Yes, keep the new main menu", centered = true).constrain {
        width = 90.percent
        x = CenterConstraint()
        y = 10.percent
        textScale = 1.scaledPixels
        color = Color.white.constraint
    } childOf yesButton

    private val pssExample = ResourceLocation("partlysaneskies", "textures/gui/main_menu/selection/pssexample.png").uiImage.constrain {
        x = CenterConstraint()
        y = 25.percent
        width = 90.percent
        height = 70.percent
    } childOf yesButton

    private val noButton = UIRoundedRectangle(5.0f).constrain {
        x = CenterConstraint() + 12.percent
        y = 40.percent
        width = 20.percent
        height = 25.percent
        color = Color(75, 37, 45, 90).constraint
    }.onMouseClickConsumer {
        config.customMainMenu = false
        nextRunnable.run()
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(200, 200, 200))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    } childOf transparentBlock

    private val sccExample = ResourceLocation("partlysaneskies", "textures/gui/main_menu/selection/sccexample.png").uiImage.constrain {
        x = CenterConstraint()
        y = 25.percent
        width = 90.percent
        height = 70.percent
    } childOf noButton

    private val sccTint = UIBlock().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent
        height = 100.percent
        color = Color(0, 0, 0, 100).constraint
    } childOf sccExample

    private val noText = UIWrappedText("No, return to the default menu", centered = true).constrain {
        width = 90.percent
        x = CenterConstraint()
        y = 10.percent
        textScale = 1.scaledPixels
        color = Color.white.constraint
    } childOf noButton

    private val timeText = UIWrappedText(centered = true).constrain {
        x = CenterConstraint()
        y = 10.scaledPixels(alignOpposite = true)
        color = Color.white.constraint
        textScale = 1.scaledPixels
    } childOf transparentBlock

    private val discordText = UIWrappedText("Discord: discord.gg/${PartlySaneSkies.discordCode}").constrain {
        x = 10.scaledPixels
        y = 10.scaledPixels(alignOpposite = true)
        textScale = 1.scaledPixels
        color = Color(69, 79, 191).constraint
    }.onMouseClick {
        SystemUtils.openLink("https://discord.gg/${PartlySaneSkies.discordCode}")
    } childOf transparentBlock

    private val partlySaneSkiesText = UIWrappedText("Made by: Partly Sane Skies").constrain {
        x = 10.scaledPixels(alignOpposite = true)
        y = 10.scaledPixels(alignOpposite = true)
        textScale = 1.scaledPixels
        color = ThemeManager.accentColor.toJavaColor().constraint
    }.onMouseClick {
        SystemUtils.openLink("https://github.com/PartlySaneStudios/partly-sane-skies")
    } childOf transparentBlock

    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)

        val userZoneId = ZoneId.systemDefault()
        val currentTime = LocalDateTime.now(userZoneId)
        var timeString = currentTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a  dd MMMM yyyy", Locale.ENGLISH))
        if (config.hour24time) {
            timeString = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd MMMM yyyy", Locale.ENGLISH))
        }
        timeText.setText(timeString)

        partlySaneSkiesText.setColor(ThemeManager.accentColor.toJavaColor())
    }
}