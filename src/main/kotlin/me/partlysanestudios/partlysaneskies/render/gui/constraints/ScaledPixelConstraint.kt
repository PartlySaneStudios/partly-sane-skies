package me.partlysanestudios.partlysaneskies.render.gui.constraints

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.ConstraintType
import gg.essential.elementa.constraints.MasterConstraint
import gg.essential.elementa.constraints.resolution.ConstraintVisitor
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils

class ScaledPixelConstraint(var value: Float): MasterConstraint {
    companion object {
        val Number.scaledPixels: ScaledPixelConstraint get() {
            return ScaledPixelConstraint((this.toDouble()).toFloat())
        }
    }
    override var cachedValue = 0f
    override var constrainTo: UIComponent? = null
    override var recalculate = true

    override fun getHeightImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels.getHeightImpl(component)
    }

    override fun getRadiusImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels.getRadiusImpl(component)
    }

    override fun getWidthImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels.getWidthImpl(component)
    }

    override fun getXPositionImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels.getXPositionImpl(component)
    }

    override fun getYPositionImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels.getYPositionImpl(component)
    }

    override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {
        return (value * ElementaUtils.scaleFactor).pixels.visitImpl(visitor, type)
    }

}