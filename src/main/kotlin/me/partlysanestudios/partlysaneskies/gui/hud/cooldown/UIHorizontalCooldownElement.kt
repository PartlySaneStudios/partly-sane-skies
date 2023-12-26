package me.partlysanestudios.partlysaneskies.gui.hud.cooldown

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.HeightConstraint
import gg.essential.elementa.constraints.WidthConstraint
import gg.essential.elementa.constraints.XConstraint
import gg.essential.elementa.constraints.YConstraint
import gg.essential.elementa.dsl.*
import java.awt.Color

class UIHorizontalCooldownElement(val xConstraint: XConstraint, val yConstraint: YConstraint, val widthConstraint: WidthConstraint, val heightConstraint: HeightConstraint) {

    var cooldown = null

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

    fun setCooldownToDisplay(cooldown: Cooldown) {

    }


}