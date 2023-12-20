//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.gui.components;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class PSSItemRender extends UIComponent {
    ItemStack item;
    float itemScale = 1;

    public PSSItemRender(ItemStack item) {
        this.item = item;
    }

    @Override
    public void draw(UMatrixStack matrixStack) {
        beforeDrawCompat(matrixStack);
        super.draw(matrixStack);
        drawItemStack(item, Math.round(this.getLeft()), Math.round(this.getTop()), getComponentName());
        super.afterDraw(matrixStack);
    }

    private void drawItemStack(ItemStack stack, int x, int y, String altText) {
        RenderItem itemRenderer = PartlySaneSkies.minecraft.getRenderItem();

        GlStateManager.pushMatrix();
        GlStateManager.scale(itemScale, itemScale, 1);
        itemRenderer.zLevel = 200f;
        net.minecraft.client.gui.FontRenderer font = null;
        if (stack != null)
            font = stack.getItem().getFontRenderer(stack);
        if (font == null)
            font = PartlySaneSkies.minecraft.fontRendererObj;
        itemRenderer.renderItemAndEffectIntoGUI(stack, Math.round(x / itemScale), Math.round(y / itemScale));
        GlStateManager.popMatrix();

    }

    public UIComponent setItemScale(PixelConstraint constraint) {
        this.itemScale = constraint.getValue();
        return this;
    }
}
