package me.partlysanestudios.partlysaneskies.features.gui.mainmenu

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.uiImage
import net.minecraft.util.ResourceLocation
import java.awt.Color

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

    private val headingText = UIWrappedText("Like the new main menu?", centered = true).constrain {
        x = CenterConstraint()
        y = 30.percent
        width = 40.percent
        textScale = 2.scaledPixels
        color = Color(79, 103, 150).constraint
    } childOf transparentBlock

    private val subheadingText = UIWrappedText("You can always disable this menu, or find other backgrounds later on in the config. (/pssc)", centered = true).constrain {
        x = CenterConstraint()
        y = 35.percent
        width = 50.percent
        textScale = 1.scaledPixels
        color = Color.lightGray.constraint
    } childOf transparentBlock

    private val yesButton = UIRoundedRectangle(5.0f).constrain {
        x = CenterConstraint() + 33.percent
        y = 55.percent
        width = 15.percent
        height = 5.percent
        color = Color(90, 150, 100, 75).constraint
    }.onMouseClickConsumer {
        config.customMainMenu = true
        nextRunnable.run()
    } childOf transparentBlock

    private val yesText = UIWrappedText("Yes, keep the Partly Sane Skies menu", centered = true).constrain {
        width = 90.percent
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 1.scaledPixels
        color = Color.lightGray.constraint
    } childOf yesButton

    private val noButton = UIRoundedRectangle(5.0f).constrain {
        x = CenterConstraint() - 33.percent
        y = 55.percent
        width = 15.percent
        height = 5.percent
        color = Color(75, 37, 45, 75).constraint
    }.onMouseClickConsumer {
        config.customMainMenu = false
        nextRunnable.run()
    } childOf transparentBlock

    private val noText = UIWrappedText("No, return to default menu", centered = true).constrain {
        width = 90.percent
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 1.scaledPixels
        color = Color.lightGray.constraint
    } childOf noButton
}