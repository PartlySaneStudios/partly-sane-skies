package me.partlysanestudios.partlysaneskies.economy.minioncalculator;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfitMinionCalculator extends WindowScreen {
    private MinionData.MinionFuel fuel = null;
    private MinionData.Minion.Upgrade[] upgrades = {};
    private double hours = 24;

    private UIComponent backgroundBox;

    private UIComponent mainTextScrollComponent;
    private UIComponent backgroundImage;
    private UIComponent leftBar;
    private UIComponent rightBar;
    public ProfitMinionCalculator(ElementaVersion version) {
        super(version);

        upgrades = new MinionData.Minion.Upgrade[] {MinionData.Minion.Upgrade.DIAMOND_SPREADING};
        fuel = MinionData.fuelMap.get("ENCHANTED_LAVA_BUCKET");

        setUpBackground();
        addItems();
    }

    public void setUpBackground() {
        this.backgroundBox = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setHeight(fromWidthScaleFactor(500))
                .setWidth(fromWidthScaleFactor(850))
                .setColor(Color.red)
                .setChildOf(getWindow());

        this.backgroundImage = Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies:textures/gui/base_color_background.png"))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(backgroundBox.getHeight()))
                .setWidth(new PixelConstraint(backgroundBox.getWidth()))
                .setChildOf(backgroundBox);

        this.mainTextScrollComponent = new ScrollComponent()
                .setY(new CenterConstraint())
                .setX(new CenterConstraint())
                .setWidth(new PixelConstraint(backgroundBox.getWidth()))
                .setHeight(new PixelConstraint(backgroundBox.getHeight()))
                .setColor(Color.red)
                .setChildOf(backgroundImage);

        this.leftBar = new UIBlock()
                .setX(new PixelConstraint(backgroundImage.getWidth() * (1/5f)))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(backgroundImage.getHeight() * .85f))
                .setWidth(fromWidthScaleFactor(2f))
                .setColor(PartlySaneSkies.ACCENT_COLOR)
                .setChildOf(backgroundImage);

        this.rightBar = new UIBlock()
                .setX(new PixelConstraint(backgroundImage.getWidth() * (4/5f)))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(backgroundImage.getHeight() * .85f))
                .setWidth(fromWidthScaleFactor(2f))
                .setColor(PartlySaneSkies.ACCENT_COLOR)
                .setChildOf(backgroundImage);

//        MinionData.Minion.Upgrade[] upgrades = {MinionData.Minion.Upgrade.DIAMOND_SPREADING};
//        new UIWrappedText(MinionData.getMostProfitMinionString(24, upgrades, MinionData.fuelMap.get("ENCHANTED_LAVA_BUCKET")))
//                .setX(new CenterConstraint())
//                .setY(fromWidthScaleFactor(10))
//                .setWidth(fromWidthScaleFactor(500))
//                .setColor(Color.white)
//                .setChildOf(mainTextScrollComponent);
    }

    public ArrayList<UIComponent> addItems() {
        ArrayList<UIComponent> components = new ArrayList<>();

        HashMap<MinionData.Minion, Double> mostProfitableMinions = MinionData.getMostProfitMinion(this.upgrades, this.fuel);
        int i = 1;

        float yPos = fromWidthScaleFactor(50).getValue();
        float barOffset = fromWidthScaleFactor(10).getValue();

        float barNegation = fromWidthScaleFactor(50).getValue();

        for (Map.Entry<MinionData.Minion, Double> en : mostProfitableMinions.entrySet()) {
            String str = "§7"+ i + ". " +  en.getKey().costBreakdown(en.getKey().maxTier, this.hours, this.upgrades, this.fuel);

            UIComponent text = new UIWrappedText(str)
                    .setText(str)
                    .setX(new PixelConstraint(leftBar.getRight() + fromWidthScaleFactor(7).getValue())) // sets it 7 scale pixels off the right side of the left bar
                    .setY(new PixelConstraint(yPos))
                    .setWidth(new PixelConstraint(rightBar.getLeft() - leftBar.getRight() - fromWidthScaleFactor(14).getValue())) // set the width to the distance between the two bars with 7 scale pixels of padding on either side
                    .setTextScale(fromWidthScaleFactor(1)) //Sets the text scale to 1 scale unit
                    .setColor(Color.WHITE)
                    .setChildOf(mainTextScrollComponent);

            UIComponent border = new UIBlock()
                    .setX(new PixelConstraint(leftBar.getRight() - text.getLeft() + barNegation))
                    .setY(new PixelConstraint(text.getHeight() + barOffset))
                    .setWidth(new PixelConstraint(rightBar.getLeft() - leftBar.getRight() - 2 * barNegation)) // set the width to the distance between the two bars with barNegation scale pixels of padding on either side
                    .setHeight(fromWidthScaleFactor(1f))
                    .setColor(PartlySaneSkies.ACCENT_COLOR)
                    .setChildOf(text);

            yPos = border.getBottom() + barOffset;
            i++;
        }

        return components;
    }

    private PixelConstraint fromWidthScaleFactor(float pos) {
        return new PixelConstraint( (float) (pos * (this.getWindow().getWidth() / 1000d)));
    }
}
