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
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.primaryColor
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSButton
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import java.awt.Color

object RareDropGUI : WindowScreen(ElementaVersion.V5) {

    private var searchBar: UITextInput? = null

    fun update() {
        window.clearChildren()

        val backgroundBox by UIBlock().constrain {
            width = 75.percent
            height = 75.percent
            x = CenterConstraint()
            y = CenterConstraint()
            color = Color(0, 0, 0, 0).constraint
        } childOf window

        updateActiveFilters(backgroundBox)

        val createFilterContainer = UIBlock().constrain {
            width = 60.percent
            height = 75.percent
            x = 40.percent
            y = 0.percent
        }.applyBackground() childOf backgroundBox

        updatePreset(backgroundBox)

        window.draw(UMatrixStack())
    }

    private fun updateActiveFilters(backgroundBox: UIBlock) {
        val activeFiltersContainer = UIBlock().constrain {
            width = 35.percent
            height = 100.percent
            x = 0.percent
            y = 0.percent
        }.applyBackground() childOf backgroundBox

        searchBar = (UITextInput("Search...").constrain {
            width = 90.percent
            height = 10.percent
            x = 5.percent
            y = 3.percent
        }.onMouseClick {
            grabWindowFocus()
        } childOf activeFiltersContainer) as UITextInput

        val scrollbar = ScrollComponent(
            scrollIconColor = primaryColor.toJavaColor(),
            innerPadding = 10f
        ).constrain {
            width = 100.percent
            height = 90.percent
            x = 0.percent
            y = 10.percent
        } childOf activeFiltersContainer

        val filterList = UIBlock(Color(0, 0, 0, 0)).constrain {
            width = 100.percent
            height = 100.percent
            x = 0.percent
            y = 0.percent
        } childOf scrollbar

        fun updateFilterList() {
            filterList.clearChildren()

            RareDropGUIManager.filters
                .filter { it.contains(searchBar!!.getText(), ignoreCase = true) }
                .forEach { filter ->
                    UIText(filter).constrain {
                        x = 0.percent
                        y = SiblingConstraint(1f)
                    } childOf filterList
                }
        }

        updateFilterList()

        searchBar!!.onKeyType { _, _ ->
            updateFilterList()
        }
    }

    private fun updatePreset(backgroundBox: UIBlock) {
        val presetsContainer by UIBlock().constrain {
            width = 60.percent
            height = 20.percent
            x = 40.percent
            y = 80.percent
        }.applyBackground() childOf backgroundBox

        RareDropGUIManager.presets.forEachIndexed { columnIndex, (presetName, items) ->
            PSSButton()
                .setText(presetName)
                .setX((15 * columnIndex + 5).percent)
                .setY(CenterConstraint())
                .setWidth(50f)
                .setHeight(50f)
                .setChildOf(presetsContainer)
                .onMouseClickConsumer {
                    RareDropGUIManager.addFilter(*items.toTypedArray())
                }
        }
    }
}