package me.partlysanestudios.partlysaneskies.general.economy.AhSniper;

import java.awt.Color;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.guicomponents.UIItemRender;

public class AhGui extends WindowScreen {

    float mainBoxHeight = 407.4f;
    float mainBoxWidth = mainBoxHeight * (6f / 4f);

    UIComponent mainBox = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(mainBoxWidth))
            .setHeight(widthScaledConstraint(mainBoxHeight))
            .setColor(Main.BASE_DARK_COLOR)
            .setChildOf(getWindow());

    int numOfColumns = 6;
    int numOfRows = 4;
    float pad = 70;
    float boxSide = (mainBoxWidth - ((numOfColumns) * pad)) / numOfColumns;

    public AhGui(ElementaVersion version) {
        super(version);

        refreshGui();
    }

    public void refreshGui() {
        Auction[][] auctions = AhSniper.getAuctions();
        for (int row = 0; row < numOfRows; row++) {
            for (int column = 0; column < numOfColumns; column++) {
                float x = (boxSide + pad) * column + pad / 2;
                float y = (boxSide + pad) * row + pad / 2;
                if (auctions[row][column] == null) {
                    continue;
                }
                try {
                    makeItemBox(auctions[row][column], x, y, mainBox);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Utils.visPrint("Slot " + x + ", " + y + "had an exception.");
                }
                
            }
        }
    }

    public void makeItemBox(Auction auction, float x, float y, UIComponent parent) throws NullPointerException {
        Color boxColor;

        if (auction.shouldHighlight()) {
            boxColor = Main.ACCENT_COLOR;
        } else {
            boxColor = Main.BASE_LIGHT_COLOR;
        }

        UIComponent box = new UIBlock()
                .setX(widthScaledConstraint(x))
                .setY(widthScaledConstraint(y))
                .setWidth(widthScaledConstraint(boxSide))
                .setHeight(widthScaledConstraint(boxSide))
                .setColor(boxColor)
                .setChildOf(mainBox);

        box.onMouseClickConsumer(event -> {
            auction.selectAuction();
        });

        UIComponent item = new UIItemRender(auction.getItem())
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(boxSide))
            .setHeight(widthScaledConstraint(boxSide))
            .setChildOf(box);

        auction.setBox(box);

        UIComponent subText =  new UIText() 
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(30))
            .setColor(Color.white)
            .setChildOf(box);

            ((UIText) subText).setText(auction.getName());
    }

    public float getWindowWidth() {
        return this.getWindow().getWidth();
    }

    private float getWidthScaleFactor() {
        return this.getWindow().getWidth() / 1097f;
    }

    // private float getHeightScaleFactor() {
    // return this.getWindow().getHeight() / 582f;
    // }

    private PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor());
    }

    // private PixelConstraint heightScaledConstraint(float value) {
    // return new PixelConstraint(value * getHeightScaleFactor());
    // }
}
