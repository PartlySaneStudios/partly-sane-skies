//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.render.gui.components

import cc.polyfrost.oneconfig.config.core.OneColor
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.constraints.XConstraint
import gg.essential.elementa.constraints.YConstraint
import gg.essential.elementa.events.UIClickEvent
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.currentButtonUIImage
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.getCurrentButtonUIImage
import java.awt.Color
import java.util.function.Consumer

class PSSButton {
    private var backgroundBlock: UIBlock
    private var buttonTexture: UIImage
    private var textComponent: UIWrappedText
    private var width = 0f
    private var height = 0f
    private var xConstraint: XConstraint? = null
    private var yConstraint: YConstraint? = null
    private var color: OneColor
    private var text: String

    constructor() {
        text = ""
        backgroundBlock = UIBlock()
            .setColor(Color(0, 0, 0, 0)) as UIBlock
        buttonTexture = currentButtonUIImage
            .setChildOf(backgroundBlock) as UIImage
        textComponent = UIWrappedText(text, false, Color(0, 0, 0, 0), true)
            .setColor(Color(255, 255, 255, 255))
            .setChildOf(buttonTexture) as UIWrappedText
        color = OneColor(0, 0, 0, 0)
    }

    constructor(color: Color?) : this(OneColor(color!!))
    constructor(color: OneColor) {
        text = ""
        backgroundBlock = UIBlock()
            .setColor(color.toJavaColor()) as UIBlock
        buttonTexture = getCurrentButtonUIImage(color)
            .setChildOf(backgroundBlock) as UIImage
        textComponent = UIWrappedText(text, false, Color(0, 0, 0, 0), true)
            .setColor(Color(255, 255, 255, 255))
            .setChildOf(buttonTexture) as UIWrappedText
        this.color = color
    }

    fun setHeight(height: Float): PSSButton {
        backgroundBlock.setHeight(PixelConstraint(height))
        buttonTexture.setHeight(PixelConstraint(height))
        this.height = height
        return this
    }

    fun setWidth(width: Float): PSSButton {
        backgroundBlock.setWidth(PixelConstraint(width))
        buttonTexture.setWidth(PixelConstraint(width))
        textComponent.setWidth(PixelConstraint(width))
        this.width = width
        return this
    }

    fun setX(xPos: XConstraint?): PSSButton {
        backgroundBlock.setX(xPos!!)
        buttonTexture.setX(CenterConstraint())
        textComponent.setX(CenterConstraint())
        xConstraint = xPos
        return this
    }

    fun setY(yPos: YConstraint?): PSSButton {
        backgroundBlock.setY(yPos!!)
        buttonTexture.setY(CenterConstraint())
        textComponent.setY(CenterConstraint())
        yConstraint = yPos
        return this
    }

    fun setChildOf(parent: UIComponent?): PSSButton {
        backgroundBlock.setChildOf(parent!!)
        return this
    }

    fun setColor(color: Color?): PSSButton {
        return setColor(OneColor(color!!))
    }

    fun setColor(color: OneColor): PSSButton {
        backgroundBlock.removeChild(buttonTexture)
        buttonTexture = getCurrentButtonUIImage(color)
            .setWidth(PixelConstraint(width))
            .setHeight(PixelConstraint(height))
            .setX(CenterConstraint())
            .setY(CenterConstraint())
            .setChildOf(backgroundBlock) as UIImage
        backgroundBlock.setColor(color.toJavaColor())
        return this
    }

    fun setDefaultColor(): PSSButton {
        backgroundBlock.removeChild(buttonTexture)
        buttonTexture = currentButtonUIImage
            .setWidth(PixelConstraint(width))
            .setHeight(PixelConstraint(height))
            .setX(CenterConstraint())
            .setY(CenterConstraint())
            .setChildOf(backgroundBlock) as UIImage
        backgroundBlock.setColor(Color(0, 0, 0, 0))
        color = OneColor(0, 0, 0, 0)
        return this
    }

    fun setText(text: String): PSSButton {
        textComponent.setText(text)
        this.text = text
        return this
    }

    fun setTextScale(scale: Float): PSSButton {
        textComponent.setTextScale(PixelConstraint(scale))
        return this
    }

    fun onMouseClickConsumer(method: Consumer<UIClickEvent>): PSSButton {
        backgroundBlock.onMouseClickConsumer(method)
        return this
    }

    val component: UIComponent
        get() = buttonTexture

    fun setBackgroundVisibility(`val`: Boolean): PSSButton {
        if (`val`) {
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

    fun insertComponentBeforeBackground(component: UIComponent?): PSSButton {
        backgroundBlock.insertChildBefore(component!!, buttonTexture)
        return this
    }
}
