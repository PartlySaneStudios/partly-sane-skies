package me.partlysanestudios.partlysaneskies.utils.guicomponents;

import gg.essential.elementa.UIComponent;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.Main;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class UIItemRender extends UIComponent {
    ItemStack item;
    public UIItemRender(ItemStack item) {
        this.item = item;
    }

    @Override
    public void draw(UMatrixStack matrixStack) {
        beforeDrawCompat(matrixStack);

        drawItemStack(item, Math.round(this.getLeft()), Math.round(this.getTop()), getComponentName());


        super.draw(matrixStack);
    }

    public void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
        RenderItem itemRenderer = Main.minecraft.getRenderItem();
        
        
        
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        itemRenderer.zLevel = 200.0F;
        itemRenderer.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = null;
        if (stack != null) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = Main.minecraft.fontRendererObj;
        ItemStack draggedStack = null;
        itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y - (draggedStack == null ? 0 : 8), altText);
        itemRenderer.zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }
}
