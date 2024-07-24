//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.render.gui.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown.Cooldown
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.weightedAverage
import java.awt.Color

class PSSHorizontalCooldown(
    private val xConstraint: XConstraint,
    private val yConstraint: YConstraint,
    private val widthConstraint: WidthConstraint,
    private val heightConstraint: HeightConstraint
) {
    private var cooldown: Cooldown? = null

    val boundingBox = UIRoundedRectangle(2f).constrain {
        radius = 4f.scaledPixels
        x = xConstraint
        y = yConstraint
        height = heightConstraint
        width = widthConstraint
        color = Color(0, 0, 0, 0).constraint
    }

    private val displayBox = UIRoundedRectangle(2f).constrain {
        radius = 4f.scaledPixels
        x = 0f.pixels
        y = 0f.pixels
        height = 100f.percent
        width = 0f.pixels
        color = Color(255, 0, 0).constraint
    } childOf boundingBox

    private val itemRender = PSSItemRender(null, autoScaleWidth = true).constrain {
        width = 1.75.percent
        x = (-35).percent
        y = CenterConstraint()
    } childOf boundingBox

    fun setChildOf(parent: UIComponent): PSSHorizontalCooldown {
        boundingBox.setChildOf(parent)
        return this
    }

    fun setX(xConstraint: XConstraint): PSSHorizontalCooldown {
        boundingBox.setX(xConstraint)
        return this
    }

    fun setY(yConstraint: YConstraint): PSSHorizontalCooldown {
        boundingBox.setY(yConstraint)
        return this
    }

    fun setCooldownToDisplay(cooldown: Cooldown?) {
        this.cooldown = cooldown
    }

    fun tick() {
        val cooldown = this.cooldown
        if (cooldown == null) {
            displayBox.setColor(Color(0, 0, 0, 0).constraint)
            boundingBox.setColor(Color(0, 0, 0, 0).constraint)
            itemRender.item = null

            return
        }

        if (!cooldown.isCooldownActive()) {
            this.cooldown = null
            return
        }

        val percentRemaining = cooldown.getTimeRemaining().toFloat() / cooldown.getTotalTime().toFloat()

        val percentComplete = 1 - percentRemaining

        val displayBoxColor = Color.RED.weightedAverage(percentRemaining, Color.GREEN, percentComplete)

        itemRender.item = cooldown.getItemToDisplay()
        boundingBox.setColor(Color(0f, 0f, 0f, .4f))
        displayBox.setColor(displayBoxColor)
        displayBox.setWidth((percentComplete * 100).percent)
    }
}
