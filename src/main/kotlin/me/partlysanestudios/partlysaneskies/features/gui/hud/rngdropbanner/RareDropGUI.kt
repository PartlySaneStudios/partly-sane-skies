//
// Written by J10an15.
// See LICENSE for copyright and license notices.
//
// Time Spent on this god awful GUI: 4h
//

package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.primaryColor
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import java.awt.Color

object RareDropGUI : WindowScreen(ElementaVersion.V5) {

    private val backgroundBox by UIBlock().constrain {
        width = 100.percent
        height = 100.percent
        x = CenterConstraint()
        y = CenterConstraint()
        color = Color(0, 0, 0, 0).constraint
    } childOf window

    private val list by ScrollComponent(scrollIconColor = primaryColor.toJavaColor())
        .constrain {
            width = 100.percent
            height = 100.percent
            x = CenterConstraint()
            y = CenterConstraint()
        } childOf backgroundBox

    fun populateGui() {
        val topBarBlock by UIBlock().constrain {
            width = 90.percent
            height = 10.percent
            x = CenterConstraint()
            y = 10.scaledPixels
        } childOf list

        topBarBlock.applyBackground()

        val text by UIText("Rare Drop GUI").constrain {
            x = CenterConstraint()
            y = 13.scaledPixels
            color = Color.WHITE.constraint
        } childOf list

        window.draw(UMatrixStack())
    }
}