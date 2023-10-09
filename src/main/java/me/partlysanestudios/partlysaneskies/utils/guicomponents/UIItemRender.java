/*
 * Partly Sane Skies: A Hypixel Skyblock QOL and Economy mod
 * Created by Su386#9878 (Su386yt) and FlagMaster#1516 (FlagHater), the Partly Sane Studios team
 * Copyright (C) ©️ Su386 and FlagMaster 2023
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.partlysanestudios.partlysaneskies.utils.guicomponents;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class UIItemRender extends UIComponent {
    ItemStack item;
    float itemScale = 1;

    public UIItemRender(ItemStack item) {
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
