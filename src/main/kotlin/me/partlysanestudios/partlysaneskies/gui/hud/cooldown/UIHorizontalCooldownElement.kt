package me.partlysanestudios.partlysaneskies.gui.hud.cooldown

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import me.partlysanestudios.partlysaneskies.gui.components.PSSItemRender
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.weightedAverage
import java.awt.Color

class UIHorizontalCooldownElement(val xConstraint: XConstraint, val yConstraint: YConstraint, val widthConstraint: WidthConstraint, val heightConstraint: HeightConstraint) {

    var cooldown: Cooldown? = null



    val boundingBox = UIBlock().constrain {
        x = xConstraint
        y = yConstraint
        height = heightConstraint
        width = widthConstraint
        color = Color(0,0,0,0).constraint
    }


    val displayBox = UIBlock().constrain {
        x = 0f.pixels
        y = 0f.pixels
        height = 100f.percent
        width = 0f.pixels
        color = Color(255, 0, 0).constraint
    } childOf boundingBox

    val itemRender = PSSItemRender(null)
        .setScaleBasedOnWidth((boundingBox.getHeight() * 1.75).pixels)
        .setX((-40).percent)
        .setY(CenterConstraint())
        .setHeight((boundingBox.getHeight() * 1.75).pixels)
        .setWidth((boundingBox.getHeight() * 1.75).pixels)
        .setChildOf(boundingBox) as PSSItemRender

    fun setChildOf(parent: UIComponent): UIHorizontalCooldownElement {
        boundingBox.setChildOf(parent)
        return this;

    }

    fun setX(xConstraint: XConstraint): UIHorizontalCooldownElement {
        boundingBox.setX(xConstraint)
        return this;

    }

    fun setY(yConstraint: YConstraint): UIHorizontalCooldownElement {
        boundingBox.setY(yConstraint)
        return this;
    }

    fun setCooldownToDisplay(cooldown: Cooldown?) {
        this.cooldown = cooldown
    }


    fun tick() {
        if (cooldown == null)  {
            displayBox.setColor(Color(0, 0, 0, 0).constraint)
            boundingBox.setColor(Color(0, 0, 0, 0).constraint)
            itemRender.item = null

            return
        }

        if (!cooldown!!.isCooldownActive()) {
            cooldown = null
            return
        }
        val percentRemaining = cooldown!!.getTimeRemaining().toFloat() / cooldown!!.getTotalTime().toFloat()

        val percentComplete = 1 - percentRemaining

        val displayBoxColor = Color.RED.weightedAverage(percentRemaining, Color.GREEN, percentComplete)

        itemRender.item = cooldown!!.getItemToDisplay()
        boundingBox.setColor(Color(0f, 0f, 0f, .4f))
        displayBox.setColor(displayBoxColor)
        displayBox.setWidth((percentComplete * 100).percent)

    }


}