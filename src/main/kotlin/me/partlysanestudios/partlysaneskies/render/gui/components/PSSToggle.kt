//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.render.gui.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.HeightConstraint
import gg.essential.elementa.constraints.WidthConstraint
import gg.essential.elementa.constraints.XConstraint
import gg.essential.elementa.constraints.YConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.events.UIClickEvent
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.getCurrentToggleUIImage
import java.awt.Color
import java.util.function.Consumer

class PSSToggle {
    private var state = false
    private var backgroundBlock: UIBlock =
        UIBlock()
            .setColor(Color(0, 0, 0, 0)) as UIBlock

    private var buttonTexture: UIImage =
        getCurrentToggleUIImage(false).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 100.percent
            height = 100.percent
        } childOf backgroundBlock

    fun toggleState(): PSSToggle = setState(!state)

    fun setState(state: Boolean): PSSToggle {
        this.state = state
        return updateState()
    }

    fun getState(): Boolean = this.state

    private fun updateState(): PSSToggle {
        val children = buttonTexture.children
        backgroundBlock.removeChild(buttonTexture)

        buttonTexture =
            if (state) {
                getCurrentToggleUIImage(true)
            } else {
                getCurrentToggleUIImage(false)
            }.constrain {
                width = 100.percent
                height = 100.percent
                x = CenterConstraint()
                y = CenterConstraint()
            }

        backgroundBlock.insertChildAt(buttonTexture, 0)

        for (child in children) {
            child.setChildOf(buttonTexture)
        }
        return this
    }

    fun setHeight(height: HeightConstraint): PSSToggle {
        backgroundBlock.setHeight(height)
        return this
    }

    fun setWidth(width: WidthConstraint): PSSToggle {
        backgroundBlock.setWidth(width)
        return this
    }

    fun setX(xPos: XConstraint): PSSToggle {
        backgroundBlock.setX(xPos)
        return this
    }

    fun setY(yPos: YConstraint): PSSToggle {
        backgroundBlock.setY(yPos)
        return this
    }

    // This method fires when the button has been clicked
    fun onMouseClickConsumer(method: Consumer<UIClickEvent>): PSSToggle {
        backgroundBlock.onMouseClickConsumer(method)
        return this
    }

    val component: UIComponent
        get() = backgroundBlock

    fun insertComponentBeforeBackground(component: UIComponent): PSSToggle {
        backgroundBlock.insertChildBefore(component, buttonTexture)
        return this
    }

    fun hide(instantly: Boolean = false) {
        backgroundBlock.hide(instantly)
        buttonTexture.hide(instantly)
    }

    fun unhide(instantly: Boolean = false) {
        backgroundBlock.unhide(instantly)
        buttonTexture.unhide(instantly)
    }

    fun setChildOf(parent: UIComponent): PSSToggle {
        backgroundBlock.setChildOf(parent)
        return this
    }
}
