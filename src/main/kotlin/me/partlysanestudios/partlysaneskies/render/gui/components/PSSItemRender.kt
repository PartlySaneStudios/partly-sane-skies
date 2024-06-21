//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.render.gui.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.constraints.ScaleConstraint
import gg.essential.elementa.constraints.WidthConstraint
import gg.essential.elementa.dsl.div
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack

class PSSItemRender(var item: ItemStack?, val autoScaleWidth: Boolean = false) : UIComponent() {

    private var itemScale = 1f

    override fun draw(matrixStack: UMatrixStack) {
        beforeDrawCompat(matrixStack)
        super.draw(matrixStack)
        drawItemStack(item, Math.round(getLeft()), Math.round(getTop()), componentName)
        super.afterDraw(matrixStack)
    }

    private fun drawItemStack(stack: ItemStack?, x: Int, y: Int, altText: String) {
        if (autoScaleWidth) {
            setScaleBasedOnWidth(this.constraints.width)
        }
        val itemRenderer = minecraft.renderItem
        GlStateManager.pushMatrix()
        GlStateManager.scale(itemScale, itemScale, 1f)
        itemRenderer.zLevel = 200f
        var font: FontRenderer? = null
        if (stack != null) font = stack.item.getFontRenderer(stack)
        if (font == null) font = minecraft.fontRendererObj
        itemRenderer.renderItemAndEffectIntoGUI(stack, Math.round(x / itemScale), Math.round(y / itemScale))
        GlStateManager.popMatrix()
    }

    fun setItem(item: ItemStack): PSSItemRender {
        this.item = item
        return this
    }

    fun setScaleBasedOnWidth(pixelConstraint: WidthConstraint): UIComponent {
        setItemScale(pixelConstraint / 16)
        return this
    }

    fun setItemScale(constraint: ScaleConstraint): UIComponent {
        itemScale = constraint.value
        return this
    }
}
