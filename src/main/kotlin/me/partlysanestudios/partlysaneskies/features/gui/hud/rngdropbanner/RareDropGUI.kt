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
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.primaryColor
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSButton
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import java.awt.Color

class RareDropGUI : WindowScreen(ElementaVersion.V5) {
    private val backgroundBox = UIBlock().constrain {
        width = 75.percent
        height = 75.percent
        x = CenterConstraint()
        y = CenterConstraint()
        color = Color(0, 0, 0, 0).constraint
    } childOf window

    private val createFilterContainer = ThemeManager.currentBackgroundUIImage.constrain {
        width = 60.percent
        height = 75.percent
        x = 40.percent
        y = 0.percent
    } childOf backgroundBox

    private val createFiltersHeading = UIWrappedText("Create Filters:").constrain {
        width = 90.percent
        x = 5.percent
        y = 2.percent
        textScale = 1.5.scaledPixels
        color = Color.gray.constraint
    } childOf createFilterContainer

    private val createFiltersSearchBar: UITextInput = UITextInput("AASearch...").constrain {
        width = 90.percent
        height = 5.percent
        x = 5.percent
        y = 7.percent
    }.onMouseClick {
        grabWindowFocus()
    }.onKeyType { _, _ ->
        updateFilterList()
    } as UITextInput childOf createFilterContainer

    private val createFiltersScrollComponent = ScrollComponent(
        scrollIconColor = primaryColor.toJavaColor(),
        innerPadding = 10f
    ).constrain {
        width = 100.percent
        height = 90.percent
        x = 0.percent
        y = 10.percent
    } childOf createFilterContainer

    private val activeFiltersContainer = ThemeManager.currentBackgroundUIImage.constrain {
        width = 35.percent
        height = 100.percent
        x = 0.percent
        y = 0.percent
    } childOf backgroundBox

    private val activeFiltersHeading = UIWrappedText("Active Filters:").constrain {
        width = 90.percent
        x = 5.percent
        y = 2.percent
        textScale = 1.5.scaledPixels
        color = Color.green.constraint
    } childOf activeFiltersContainer

    private val activeFiltersSearchBar: UITextInput = UITextInput("Search...").constrain {
        width = 90.percent
        height = 5.percent
        x = 5.percent
        y = 7.percent
    }.onMouseClick {
        grabWindowFocus()
    }.onKeyType { _, _ ->
        updateFilterList()
    } as UITextInput childOf activeFiltersContainer

    private val activeFiltersScrollComponent = ScrollComponent(
        scrollIconColor = primaryColor.toJavaColor(),
        innerPadding = 10f
    ).constrain {
        width = 100.percent
        height = 90.percent
        x = 0.percent
        y = 10.percent
    } childOf activeFiltersContainer

    private val presetsContainer = ThemeManager.currentBackgroundUIImage.constrain {
        width = 60.percent
        height = 20.percent
        x = 40.percent
        y = 80.percent
    } childOf backgroundBox

    init {
        update()
    }
    fun update() {
        updateFilterList()
        addPresets()
    }

    private fun updateFilterList() {
        activeFiltersScrollComponent.clearChildren()

        RareDropGUIManager.filters
            .filter { it.contains(activeFiltersSearchBar.getText(), ignoreCase = true) }
            .forEach { filter ->
                UIText(filter).constrain {
                    x = 0.percent
                    y = SiblingConstraint(1f)
                } childOf activeFiltersScrollComponent
            }
    }

    private fun addPresets() {
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
                    updateFilterList()
                }
        }
    }
}