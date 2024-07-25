//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.render.gui.components

import cc.polyfrost.oneconfig.config.core.OneColor
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.HeightConstraint
import gg.essential.elementa.constraints.WidthConstraint
import gg.essential.elementa.constraints.XConstraint
import gg.essential.elementa.constraints.YConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.events.UIClickEvent
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.currentButtonUIImage
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.getCurrentButtonUIImage
import java.awt.Color
import java.util.function.Consumer

class PSSButton {

    private var color: OneColor
    private var text: String = ""
    private val onMouseClick = ArrayList<Consumer<UIClickEvent>>()

    private val backgroundBlock = UIBlock().constrain {
        color = Color(0, 0, 0, 0).constraint
    }.onMouseClick {
        for (method in onMouseClick) {
            method.accept(it)
        }
    }

    private var buttonTexture = currentButtonUIImage.constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent
        height = 100.percent
    }.onMouseEnter {
        this.setWidth(105.percent)
        this.setHeight(105.percent)
    }.onMouseLeave {
        this.setWidth(100.percent)
        this.setHeight(100.percent)
    } childOf backgroundBlock

    private val textComponent = UIWrappedText(text, false, Color(0, 0, 0, 0), true).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent
        color = Color.lightGray.constraint
    } childOf backgroundBlock

    constructor() {
        text = ""
        color = OneColor(0, 0, 0, 0)
    }

    constructor(color: Color) : this(OneColor(color))

    constructor(color: OneColor) {
        text = ""
        backgroundBlock.setColor(color.toJavaColor())
        this.color = color
    }

    fun setHeight(height: HeightConstraint): PSSButton {
        backgroundBlock.setHeight(height)
        return this
    }

    fun setWidth(width: WidthConstraint): PSSButton {
        backgroundBlock.setWidth(width)
        return this
    }

    fun setX(xPos: XConstraint): PSSButton {
        backgroundBlock.setX(xPos)
        return this
    }

    fun setY(yPos: YConstraint): PSSButton {
        backgroundBlock.setY(yPos)
        return this
    }

    fun setChildOf(parent: UIComponent): PSSButton {
        backgroundBlock.setChildOf(parent)
        return this
    }

    fun setColor(color: Color): PSSButton {
        return setColor(OneColor(color))
    }

    fun setColor(color: OneColor): PSSButton {
        backgroundBlock.removeChild(buttonTexture)
        buttonTexture = getCurrentButtonUIImage(color).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 100.percent
            height = 100.percent
        } childOf backgroundBlock

        textComponent childOf buttonTexture

        backgroundBlock.setColor(color.toJavaColor())
        return this
    }

    fun setDefaultColor(): PSSButton {
        backgroundBlock.removeChild(buttonTexture)
        buttonTexture = currentButtonUIImage.constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 100.percent
            height = 100.percent
        } childOf backgroundBlock

        textComponent childOf buttonTexture

        backgroundBlock.setColor(Color(0, 0, 0, 0))
        return this
    }

    fun setText(text: String): PSSButton {
        textComponent.setText(text)
        this.text = text
        return this
    }

    fun setTextScale(scale: HeightConstraint): PSSButton {
        textComponent.setTextScale(scale)
        return this
    }

    fun onMouseClickConsumer(method: Consumer<UIClickEvent>): PSSButton {
        onMouseClick.add(method)
        return this
    }

    val component: UIComponent
        get() = buttonTexture

    fun setBackgroundVisibility(value: Boolean): PSSButton {
        if (value) {
            buttonTexture.unhide(true)
            for (child in buttonTexture.children) {
                child.unhide(true)
            }
        } else {
            buttonTexture.hide()
            for (child in buttonTexture.children) {
                child.unhide(true)
            }
        }
        return this
    }

    fun insertComponentBeforeBackground(component: UIComponent): PSSButton {
        backgroundBlock.insertChildBefore(component, buttonTexture)
        return this
    }
}
