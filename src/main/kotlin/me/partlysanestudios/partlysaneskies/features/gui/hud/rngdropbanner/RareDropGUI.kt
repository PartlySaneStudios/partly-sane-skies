//
// Written by J10an15.
// See LICENSE for copyright and license notices.
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
import gg.essential.elementa.constraints.CramSiblingConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner.RareDropGUIManager.currentFilter
import me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner.RareDropGUIManager.currentFilterType
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

    private val createFiltersInput = UITextInput("Create a ${currentFilterType.displayName} Filter...").constrain {
        width = 90.percent
        height = 5.percent
        x = 5.percent
        y = 7.percent
    }.onMouseClick {
        grabWindowFocus()
    } as UITextInput childOf createFilterContainer

    private val createFiltersScrollComponent = ScrollComponent(
        scrollIconColor = primaryColor.toJavaColor(),
        innerPadding = 10f,
        scrollAcceleration = 2f,
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

    private val activeFiltersHeading = UIWrappedText("${currentFilterType.displayName} Filters:").constrain {
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
        innerPadding = 10f,
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
        addFilterCreationButtons()
    }

    private fun updateFilterList() {
        activeFiltersScrollComponent.clearChildren()

        currentFilter
            .filter { it.contains(activeFiltersSearchBar.getText(), ignoreCase = true) }
            .forEach { filter ->
                UIText("§cx").constrain {
                    x = 0.percent
                    y = SiblingConstraint(4f)
                }.onMouseClick {
                    currentFilter -= filter
                    RareDropGUIManager.saveData()
                    updateFilterList()
                } childOf activeFiltersScrollComponent

                UIText(filter).constrain {
                    x = 5.percent
                    y = CramSiblingConstraint()
                } childOf activeFiltersScrollComponent
            }
    }

    private fun addFilterCreationButtons() {
        PSSButton()
            .setText("Add ${currentFilterType.displayName} Filter")
            .setX(5.percent)
            .setY(15.percent)
            .setHeight(50.pixels)
            .setWidth(60.pixels)
            .setChildOf(createFilterContainer)
            .onMouseClickConsumer {
                val text = createFiltersInput.getText()
                if (text.isBlank()) return@onMouseClickConsumer
                RareDropGUIManager.addFilter(text)
                createFiltersInput.setText("")
                updateFilterList()
            }

        val opposite = RareDropGUIManager.FilterType.entries.first { it != currentFilterType }
        PSSButton()
            .setText("Switch to ${opposite.displayName}")
            .setX(5.percent)
            .setY(SiblingConstraint(5f))
            .setHeight(50.pixels)
            .setWidth(60.pixels)
            .setChildOf(createFilterContainer)
            .onMouseClickConsumer {
                currentFilterType = opposite
                RareDropGUIManager.saveData()
                update()
                activeFiltersHeading.setText("${currentFilterType.displayName} Filters:")
            }
    }

    private fun addPresets() {
        RareDropGUIManager.presets.forEachIndexed { columnIndex, (presetName, items) ->
            PSSButton()
                .setText(presetName)
                .setX((15 * columnIndex + 5).percent)
                .setY(CenterConstraint())
                .setHeight(50.pixels)
                .setWidth(60.pixels)
                .setChildOf(presetsContainer)
                .onMouseClickConsumer {
                    RareDropGUIManager.addFilter(*items.toTypedArray())
                    updateFilterList()
                }
        }
    }
}
