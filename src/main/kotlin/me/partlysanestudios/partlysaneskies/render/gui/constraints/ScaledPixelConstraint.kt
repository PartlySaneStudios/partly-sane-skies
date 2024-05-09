package me.partlysanestudios.partlysaneskies.render.gui.constraints

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.ConstraintType
import gg.essential.elementa.constraints.MasterConstraint
import gg.essential.elementa.constraints.resolution.ConstraintVisitor
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils

class ScaledPixelConstraint(var value: Float, val alignOpposite: Boolean = false, val alignOutside: Boolean = false): MasterConstraint {
    companion object {
        val Number.scaledPixels: ScaledPixelConstraint get() {
            return ScaledPixelConstraint((this.toDouble()).toFloat(), alignOpposite = false, alignOutside = false)
        }

        fun Number.scaledPixels(alignOpposite: Boolean = false, alignOutside: Boolean = false): ScaledPixelConstraint {
            return ScaledPixelConstraint((this.toDouble()).toFloat(), alignOpposite, alignOutside)
        }
    }
    override var cachedValue = 0f
    override var constrainTo: UIComponent? = null
    override var recalculate = true

    override fun getHeightImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels(alignOpposite, alignOutside).getHeightImpl(component)
    }

    override fun getRadiusImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels(alignOpposite, alignOutside).getRadiusImpl(component)
    }

    override fun getWidthImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels(alignOpposite, alignOutside).getWidthImpl(component)
    }

    override fun getXPositionImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels(alignOpposite, alignOutside).getXPositionImpl(component)
    }

    override fun getYPositionImpl(component: UIComponent): Float {
        return (value * ElementaUtils.scaleFactor).pixels(alignOpposite, alignOutside).getYPositionImpl(component)
    }

    override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {
        return (value * ElementaUtils.scaleFactor).pixels(alignOpposite, alignOutside).visitImpl(visitor, type)
    }

}