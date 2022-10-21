package me.partlysanestudios.partlysaneskies.general.economy.AhSniper;

import java.awt.Color;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.Main;
import net.minecraft.item.ItemStack;

public class AhGui extends WindowScreen {

    float mainBoxHeight = 407.4f;
    float mainBoxWidth = mainBoxHeight * (5f / 4f);

    UIComponent mainBox = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(mainBoxWidth))
            .setHeight(widthScaledConstraint(mainBoxHeight))
            .setColor(Main.BASE_DARK_COLOR)
            .setChildOf(getWindow());

    int numOfColumns = 5;
    int numOfRows = 4;
    float pad = 60;
    float boxSide = (mainBoxWidth - ((numOfColumns) * pad)) / numOfColumns;

    public AhGui(ElementaVersion version) {
        super(version);

        boolean highlight = true;

        for (int row = 0; row < numOfRows; row++) {
            for (int column = 0; column < numOfColumns; column++) {
                float x = (boxSide + pad) * column + pad/2;
                float y = (boxSide + pad) * row + pad/2;
                makeItemBox(null, x, y, mainBox, highlight);

                highlight = !highlight;
            }
        }

    }

    public void displayItem(float x, float y, ItemStack item) {
        itemRender.renderItemIntoGUI(item, (int) x, (int) y);
    }

    public void makeItemBox(ItemStack item, float x, float y, UIComponent parent, boolean highlight) {
        Color boxColor;

        if (highlight) {
            boxColor = Main.ACCENT_COLOR;
        } else {
            boxColor = Main.BASE_LIGHT_COLOR;
        }

        new UIBlock()
                .setX(widthScaledConstraint(x))
                .setY(widthScaledConstraint(y))
                .setWidth(widthScaledConstraint(boxSide))
                .setHeight(widthScaledConstraint(boxSide))
                .setColor(boxColor)
                .setChildOf(mainBox);

        // itemRender.renderItemIntoGUI(item, (int) itemBox.getLeft(), (int)
        // itemBox.getTop());
    }

    public float getWindowWidth() {
        return this.getWindow().getWidth();
    }

    private float getWidthScaleFactor() {
        return this.getWindow().getWidth() / 1097f;
    }

    // private float getHeightScaleFactor() {
    //     return this.getWindow().getHeight() / 582f;
    // }

    private PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor());
    }

    // private PixelConstraint heightScaledConstraint(float value) {
    //     return new PixelConstraint(value * getHeightScaleFactor());
    // }
}
