package me.partlysanestudios.partlysaneskies.general.economy.AhSniper;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.Main;
import net.minecraft.item.ItemStack;

public class AhGui extends WindowScreen {

    UIComponent mainBox = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(543.2f))
            .setHeight(widthScaledConstraint(407.4f))
            .setColor(Main.BASE_DARK_COLOR)
            .setChildOf(getWindow());

    public AhGui(ElementaVersion version) {
        super(version);
    }

    public void displayItem(float x, float y, ItemStack item, UIComponent parent) {
        itemRender.renderItemIntoGUI(item, (int) x, (int) y);
    }

    public static void clickOnSlot(int slot) {
        Main.minecraft.playerController.windowClick(Main.minecraft.thePlayer.openContainer.windowId, slot, 0, 3,
                Main.minecraft.thePlayer);
    }

    public float getWindowWidth() {
        return this.getWindow().getWidth();
    };

    private float getWidthScaleFactor() {
        return this.getWindow().getWidth() / 1097f;
    }

    private float getHeightScaleFactor() {
        return this.getWindow().getHeight() / 582f;
    }

    private PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor());
    }

    private PixelConstraint heightScaledConstraint(float value) {
        return new PixelConstraint(value * getHeightScaleFactor());
    }
}
