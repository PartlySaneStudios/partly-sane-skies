//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.render.gui.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.constraints.XConstraint
import gg.essential.elementa.constraints.YConstraint
import gg.essential.elementa.events.UIClickEvent
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.getCurrentToggleUIImage
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.util.function.Consumer

class PSSToggle {

    private var width = 0f
    private var height = 0f
    private var xConstraint: XConstraint? = null
    private var yConstraint: YConstraint? = null
    private var state = false
    private var backgroundBlock: UIBlock
    private var buttonTexture: UIImage

    init {
        backgroundBlock = UIBlock()
            .setColor(Color(0, 0, 0, 0)) as UIBlock
        buttonTexture = getCurrentToggleUIImage(false)
            .setChildOf(backgroundBlock) as UIImage
        buttonTexture.onMouseClickConsumer { s: UIClickEvent? -> }
    }

    fun toggleState(): PSSToggle {
        return setState(!state)
    }

    fun setState(state: Boolean): PSSToggle {
        this.state = state
        return updateState()
    }

    fun getState(): Boolean {
        return this.state
    }

    fun updateState(): PSSToggle {
        val children = buttonTexture.children
        backgroundBlock.removeChild(buttonTexture)
        if (state) {
            buttonTexture = getCurrentToggleUIImage(true)
                .setWidth(PixelConstraint(width))
                .setHeight(PixelConstraint(height))
                .setX(CenterConstraint())
                .setY(CenterConstraint())
                .setChildOf(backgroundBlock) as UIImage
        } else {
            buttonTexture = getCurrentToggleUIImage(false)
                .setWidth(PixelConstraint(width))
                .setHeight(PixelConstraint(height))
                .setX(CenterConstraint())
                .setY(CenterConstraint())
                .setChildOf(backgroundBlock) as UIImage
        }
        for (child in children) {
            child.setChildOf(buttonTexture)
        }
        return this
    }

    fun setHeight(height: Float): PSSToggle {
        backgroundBlock.setHeight(PixelConstraint(height))
        buttonTexture.setHeight(PixelConstraint(height))
            .setX(CenterConstraint())
            .setY(CenterConstraint())
        this.height = height
        return this
    }

    fun setWidth(width: Float): PSSToggle {
        backgroundBlock.setWidth(PixelConstraint(width))
        buttonTexture.setWidth(PixelConstraint(width))
            .setX(CenterConstraint())
            .setY(CenterConstraint())
        this.width = width
        return this
    }

    fun setX(xPos: XConstraint?): PSSToggle {
        backgroundBlock.setX(xPos!!)
        buttonTexture.setX(CenterConstraint())
        xConstraint = xPos
        return this
    }

    fun setY(yPos: YConstraint?): PSSToggle {
        backgroundBlock.setY(yPos!!)
        buttonTexture.setY(CenterConstraint())
        yConstraint = yPos
        return this
    }

    // This method fires when the button has been clicked
    fun onMouseClickConsumer(method: Consumer<UIClickEvent>): PSSToggle {
        backgroundBlock.onMouseClickConsumer(method)
        return this
    }

    val component: UIComponent
        get() = buttonTexture

    fun insertComponentBeforeBackground(component: UIComponent?): PSSToggle {
        backgroundBlock.insertChildBefore(component!!, buttonTexture)
        return this
    }

    fun setChildOf(parent: UIComponent?): PSSToggle {
        backgroundBlock.setChildOf(parent!!)
        return this
    }
}
