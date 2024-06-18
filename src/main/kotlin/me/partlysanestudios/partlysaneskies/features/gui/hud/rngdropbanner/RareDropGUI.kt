//
// Written by J10an15.
// See LICENSE for copyright and license notices.
//
// Time Spent on this god awful GUI: 4h
//


package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import java.awt.Color

object RareDropGUI : WindowScreen(ElementaVersion.V5) {

    fun update() {
        window.clearChildren()

        val backgroundBox by UIBlock().constrain {
            width = 75.percent
            height = 75.percent
            x = CenterConstraint()
            y = CenterConstraint()
            color = Color(0, 0, 0, 0).constraint
        } childOf window

        val activeFiltersContainer = UIBlock().constrain {
            width = 35.percent
            height = 100.percent
            x = 0.percent
            y = 0.percent
        }.applyBackground() childOf backgroundBox

        val createFilterContainer = UIBlock().constrain {
            width = 60.percent
            height = 75.percent
            x = 40.percent
            y = 0.percent
        }.applyBackground() childOf backgroundBox

        val presetsContainer = UIBlock().constrain {
            width = 60.percent
            height = 20.percent
            x = 40.percent
            y = 80.percent
        }.applyBackground() childOf backgroundBox


        RareDropGUIManager.presets.chunked(2).forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, (presetName, items) ->
                val presetButton = UIBlock().constrain {
                    width = 50.percent
                    height = 50.percent
                    x = (50 * columnIndex).percent
                    y = (50 * rowIndex).percent
                }.onMouseClick {
                    //RareDropGUIManager.addFilters(items)
                } childOf presetsContainer

                UIText(presetName).constrain {
                    x = CenterConstraint()
                    y = CenterConstraint()
                } childOf presetButton
            }
        }

        window.draw(UMatrixStack())
    }
}