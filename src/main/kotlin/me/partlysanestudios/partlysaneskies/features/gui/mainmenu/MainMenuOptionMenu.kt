package me.partlysanestudios.partlysaneskies.features.gui.mainmenu

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSButton
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import java.awt.Color

class MainMenuOptionMenu(nextRunnable: Runnable): WindowScreen(ElementaVersion.V5) {

    private val backgroundBox = UIBlock().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent
        height = 100.percent
        color = Color(0, 0, 0).constraint

    } childOf window

    private val transparentBlock = UIBlock().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent
        height = 100.percent
        color = Color(0, 0, 0, 0).constraint
    } childOf backgroundBox

    private val headingText = UIWrappedText("Like the new main menu?", centered = true).constrain {
        x = CenterConstraint()
        y = 30.percent
        width = 40.percent
        textScale = 2.scaledPixels
    } childOf transparentBlock

    private val yesButton = PSSButton(Color.green)
        .setX(33.percent)
        .setY(66.percent)
        .setWidth(15.percent)
        .setHeight(5.percent)
        .setTextScale(1.scaledPixels)
        .setText("Yes, keep the Partly Sane Skies Menu")
        .onMouseClickConsumer {
            nextRunnable.run()
        }

    private val noButton = PSSButton(Color.red)
        .setX(33.percent)
        .setY(66.percent)
        .setWidth(15.percent)
        .setHeight(5.percent)
        .setTextScale(1.scaledPixels)
        .setText("No, return to default menu")
        .onMouseClickConsumer {
            config.customMainMenu = false
            nextRunnable.run()
        }


}