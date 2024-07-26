//
// Written by J10an15 and Su386.
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
import gg.essential.elementa.dsl.plus
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner.RareDropGUIManager.currentFilter
import me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner.RareDropGUIManager.currentFilterType
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.primaryColor
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSButton
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.minus
import me.partlysanestudios.partlysaneskies.utils.StringUtils.colorCodeToColor
import org.lwjgl.input.Keyboard
import java.awt.Color

class RareDropGUI : WindowScreen(ElementaVersion.V5) {
    private val backgroundBox = UIBlock().constrain {
        width = 75.percent
        height = 75.percent
        x = CenterConstraint()
        y = CenterConstraint()
        color = Color(0, 0, 0, 0).constraint
    } childOf window

    /**
     * Create Filter Container
     */
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
        textScale = 2.5.scaledPixels
        color = Color.lightGray.constraint
    } childOf createFilterContainer

    private val createFiltersSearchBar = UITextInput("Search Items...").constrain {
        width = 90.percent
        height = 3.percent
        x = 5.percent
        y = SiblingConstraint() + 2.percent
        textScale = 1.5.scaledPixels
        color = Color.gray.constraint
    }.onMouseClick {
        grabWindowFocus()
    }.onKeyType {_, key ->
        if (key == Keyboard.KEY_NUMPADENTER || key == Keyboard.KEY_RETURN){
            val text = (this as UITextInput).getText()
            if (text.isBlank()) return@onKeyType
            RareDropGUIManager.addFilter(text)
            this.setText("")
            updateFilterList()
        }

        this.setColor(Color.lightGray)

        updateCreateFilterAutocomplete()
    }.onMouseEnter {
        this.setColor(Color.lightGray)
    }.onMouseLeave {
        if ((this as UITextInput).getText().isEmpty()) {
            this.setColor(Color.gray)
        }
    } as UITextInput childOf createFilterContainer

    private val createFilterButton = PSSButton()
        .setText("Add to ${currentFilterType.displayName} Filter")
        .setX(CramSiblingConstraint() + 5.percent)
        .setY(CramSiblingConstraint() + 2.percent)
        .setHeight(50.scaledPixels)
        .setWidth(60.scaledPixels)
        .setChildOf(createFilterContainer)
        .onMouseClickConsumer {
            val text = createFiltersSearchBar.getText()
            if (text.isBlank()) return@onMouseClickConsumer
            RareDropGUIManager.addFilter(text)
            createFiltersSearchBar.setText("")
            updateFilterList()
        }

    private var opposite = RareDropGUIManager.FilterType.entries.first { it != currentFilterType }
    private val switchTypeButton = PSSButton()
        .setText("Switch to ${opposite.displayName}")
        .setX(CramSiblingConstraint() + 2.percent)
        .setY(CramSiblingConstraint())
        .setHeight(50.scaledPixels)
        .setWidth(90.scaledPixels)
        .setChildOf(createFilterContainer)
        .onMouseClickConsumer {
            currentFilterType = opposite
            opposite = RareDropGUIManager.FilterType.entries.first { it != currentFilterType }
            RareDropGUIManager.saveData()
            update()
        }

    private val autoCompleteScrollComponent = ScrollComponent().constrain {
        x = CenterConstraint()
        y = SiblingConstraint() + 2.percent
        width = 90.percent
        height = 70.percent
        color = Color.red.constraint
    } childOf createFilterContainer

    /**
     * Active Filter Container
     */
    private val activeFiltersContainer = ThemeManager.currentBackgroundUIImage.constrain {
        width = 35.percent
        height = 100.percent
        x = 0.percent
        y = 0.percent
    } childOf backgroundBox

    private val activeFiltersHeading = UIWrappedText("Active ${currentFilterType.displayName} Filters:").constrain {
        width = 90.percent
        x = 5.percent
        y = 2.percent
        textScale = 2.5.scaledPixels
        color = Color.green.constraint
    } childOf activeFiltersContainer

    private val activeFiltersSearchBar: UITextInput = UITextInput("Search Filters...").constrain {
        width = 90.percent
        height = 3.percent
        x = 5.percent
        y = SiblingConstraint() + 2.percent
        textScale = 1.5.scaledPixels
        color = Color.gray.constraint
    }.onMouseClick {
        grabWindowFocus()
    }.onKeyType { _, _ ->
        updateFilterList()
    }.onMouseEnter {
        this.setColor(Color.lightGray)
    }.onMouseLeave {
        if ((this as UITextInput).getText().isEmpty()) {
            this.setColor(Color.gray)
        }
    } as UITextInput childOf activeFiltersContainer

    private val activeFiltersScrollComponent = ScrollComponent(
        scrollIconColor = primaryColor.toJavaColor(),
        innerPadding = 10f,
    ).constrain {
        width = 100.percent
        height = 90.percent
        x = 0.percent
        y = SiblingConstraint() + 1.percent
    } childOf activeFiltersContainer


    /**
     * Presets Container
     */
    private val presetsContainer = ThemeManager.currentBackgroundUIImage.constrain {
        width = 60.percent
        height = 20.percent
        x = 40.percent
        y = 80.percent
    } childOf backgroundBox

    private val presetsHeading = UIWrappedText("Add Bulk Items:").constrain {
        x = 4.percent
        y = 10.percent
        width = 92.percent
        textScale = 1.5.scaledPixels
        color = Color.green.constraint
    } childOf presetsContainer

    private val presetButtons = RareDropGUIManager.presets.forEachIndexed { columnIndex, (presetName, items) ->
        PSSButton()
            .setText(presetName)
            .setX((15 * columnIndex + 5).percent)
            .setY(CenterConstraint() + 10.percent)
            .setHeight(50.scaledPixels)
            .setWidth(60.scaledPixels)
            .setChildOf(presetsContainer)
            .onMouseClickConsumer {
                RareDropGUIManager.addFilter(*items.toTypedArray())
                updateFilterList()
            }
    }

    init {
        update()
    }

    private fun createAutoCompletedItem(itemName: String, itemColor: Color) {
        val boundingBox = UIBlock().constrain {
            x = CramSiblingConstraint(10f)
            y = CramSiblingConstraint(10f)
            width = 48.percent
            height = 8.5.percent
            color = Color(0,0 ,0, 0).constraint
        } childOf autoCompleteScrollComponent


        val text = UIWrappedText(itemName).constrain {
            x = 0.pixels
            y = CenterConstraint()
            width = 100.percent
            textScale = 1.5.scaledPixels
            color = itemColor.constraint
        }

        val textHitbox = UIBlock().constrain {
            x = 0.pixels
            y = CenterConstraint()
            width = 90.percent
            height = 100.percent
            color = Color(0, 0, 0, 0).constraint
        }.onMouseClick {
            if (it.mouseButton != 0) {
                return@onMouseClick
            }

            RareDropGUIManager.addFilter(itemName)
            updateFilterList()
        }.onMouseEnter {
            text.setColor(itemColor - Color(66, 66, 66))
        }.onMouseLeave {
            text.setColor(itemColor)
        } childOf boundingBox

        text childOf textHitbox

        val fillArrow = UIWrappedText("↖", centered = true).constrain {
            x = 0.pixels(alignOpposite = true)
            y = CenterConstraint()
            width = 10.percent
            height = 100.percent
            textScale = 3.scaledPixels
            color = Color.lightGray.constraint
        }.onMouseClick {
            if (it.mouseButton != 0) {
                return@onMouseClick
            }
            createFiltersSearchBar.setText(itemName)
            updateCreateFilterAutocomplete()
        }.onMouseEnter {
            this.setColor(Color.gray)
        }.onMouseLeave {
            this.setColor(Color.lightGray)
        } childOf boundingBox
    }

    private fun updateCreateFilterAutocomplete() {
        autoCompleteScrollComponent.clearChildren()
        val searchText = createFiltersSearchBar.getText()

        createFiltersSearchBar.setColor(if (searchText.isBlank()) Color.gray else Color.lightGray)

        val addedNames = mutableSetOf<String>()

        SkyblockDataManager.getAllItems()
            .sorted()
            .asSequence()
            .mapNotNull { id ->
                SkyblockDataManager.getItem(id)?.takeIf {
                    id.contains(searchText, ignoreCase = true) || it.name.contains(searchText, ignoreCase = true)
                }?.let { item ->
                    item.name to if (item.rarity == Rarity.UNKNOWN) {
                        Color.white
                    } else {
                        item.rarity.colorCode.colorCodeToColor()
                    }
                }
            }
            .filter { (name, _) ->
                addedNames.add(name)
            }
            .take(100)
            .forEach { (name, color) ->
                createAutoCompletedItem(name, color)
            }
    }

    private fun update() {
        updateFilterList()
        updateTitles()
        updateCreateFilterAutocomplete()
    }

    private fun updateTitles() {
        activeFiltersHeading.setText("Active ${currentFilterType.displayName} Filters:")
        createFilterButton.setText("Add to ${currentFilterType.displayName}")
        switchTypeButton.setText("Switch to ${opposite.displayName} Mode")
    }

    private fun updateFilterList() {
        activeFiltersScrollComponent.clearChildren()

        if (activeFiltersSearchBar.getText().isBlank()) {
            activeFiltersSearchBar.setColor(Color.gray)
        } else {
            activeFiltersSearchBar.setColor(Color.lightGray)
        }

        currentFilter
            .filter { it.contains(activeFiltersSearchBar.getText(), ignoreCase = true) }
            .forEach { filter ->
                val box = UIBlock() childOf activeFiltersScrollComponent
                val xText = UIWrappedText("§cx", centered = true).constrain {
                    x = 0.pixels
                    y = CenterConstraint()
                    width = 10.percent
                } childOf box

                val filterText = UIWrappedText(filter).constrain {
                    x = SiblingConstraint(4f)
                    y = CenterConstraint()
                    textScale = 1.5.scaledPixels
                    width = 90.percent
                } childOf box

                box.constrain {
                    x = 0.percent
                    y = SiblingConstraint(4f)
                    height = 4.percent
                    width = 90.percent
                    color = Color(0, 0, 0, 0).constraint
                }.onMouseClick {
                    currentFilter -= filter
                    RareDropGUIManager.saveData()
                    updateFilterList()
                }.onMouseEnter {
                    filterText.setText("§m$filter")
                }.onMouseLeave {
                    filterText.setText(filter)
                }

                val id = SkyblockDataManager.getId(filter)
                val color = if (id.isNotBlank()) {
                    SkyblockDataManager.getItem(id)?.rarity?.colorCode?.colorCodeToColor() ?: Color.lightGray
                } else {
                    Color.lightGray
                }
                filterText.setColor(color)
            }
    }
}
