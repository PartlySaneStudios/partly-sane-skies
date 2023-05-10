package me.partlysanestudios.partlysaneskies.economy.minioncalculator;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.SkyblockItem;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.guicomponents.PSSToggle;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfitMinionCalculator extends WindowScreen {
    private MinionData.MinionFuel selectedFuel = null;
    private MinionData.Minion.Upgrade[] upgrades = {};
    private double hours = 24;

    private UIComponent backgroundBox;

    private ScrollComponent mainTextScrollComponent;
    private UIComponent backgroundImage;
    private UIComponent leftBar;
    private UIComponent rightBar;

    private ArrayList<PSSToggle> fuelToggles;
    private ArrayList<UIComponent> minionTexts;
    private HashMap<MinionData.Minion.Upgrade, PSSToggle> upgradeToggleMap;
    public ProfitMinionCalculator(ElementaVersion version) {
        super(version);

        setUpBackground();
        this.minionTexts = addMinionBreakdownText();
        this.fuelToggles = addFuelButtons();
        this.upgradeToggleMap = addMinionUpgradeButtons();
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

        this.mainTextScrollComponent = (ScrollComponent) new ScrollComponent()
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
    }

//    Adds the most profitable minion's text to the middle section. Also returns a list with the objects in order
    public ArrayList<UIComponent> addMinionBreakdownText() {
        ArrayList<UIComponent> components = new ArrayList<>();
//        Most profitable minions
        HashMap<MinionData.Minion, Double> mostProfitableMinions = MinionData.getMostProfitMinion(this.upgrades, this.selectedFuel);
        int i = 1; // Rank

//        Starting y location
        float yPos = fromWidthScaleFactor(50).getValue();
//        Offset between the top of the text and the bar
        float barOffset = fromWidthScaleFactor(10).getValue();
//        Distance between the bar and the edge of the middle section
        float barNegation = fromWidthScaleFactor(66).getValue();

        for (Map.Entry<MinionData.Minion, Double> en : mostProfitableMinions.entrySet()) {
//            Creates a string with the Minion name, and it's cost breakdown
            String str = "§7"+ i + ". " +  en.getKey().costBreakdown(en.getKey().maxTier, this.hours, this.upgrades, this.selectedFuel);

//            Creates a WrappedText object with said string
            UIComponent text = new UIWrappedText(str)
                    .setText(str)
                    .setX(new PixelConstraint(leftBar.getRight() + fromWidthScaleFactor(7).getValue())) // sets it 7 scale pixels off the right side of the left bar
                    .setY(new PixelConstraint(yPos))
                    .setWidth(new PixelConstraint(rightBar.getLeft() - leftBar.getRight() - fromWidthScaleFactor(14).getValue())) // set the width to the distance between the two bars with 7 scale pixels of padding on either side
                    .setTextScale(fromWidthScaleFactor(1)) //Sets the text scale to 1 scale unit
                    .setColor(Color.WHITE)
                    .setTextScale(fromWidthScaleFactor(1))
                    .setChildOf(mainTextScrollComponent);

//            Creates a line separating the text
            UIComponent border = new UIBlock()
                    .setX(new PixelConstraint(leftBar.getRight() - text.getLeft() + barNegation))
                    .setY(new PixelConstraint(text.getHeight() + barOffset))
                    .setWidth(new PixelConstraint(rightBar.getLeft() - leftBar.getRight() - 2 * barNegation)) // set the width to the distance between the two bars with barNegation scale pixels of padding on either side
                    .setHeight(fromWidthScaleFactor(1f))
                    .setColor(PartlySaneSkies.ACCENT_COLOR)
                    .setChildOf(text);

            components.add(text);

            yPos = border.getBottom() + barOffset;
            i++;
        }

        return components;
    }

    public ArrayList<PSSToggle> addFuelButtons() {
        ArrayList<PSSToggle> components = new ArrayList<>();

        float yPos = fromWidthScaleFactor(10).getValue();

        float textPad = fromWidthScaleFactor(5).getValue();

        float buttonPad = fromWidthScaleFactor(15).getValue();

        for (Map.Entry<String, MinionData.MinionFuel> en : MinionData.fuelMap.entrySet()) {

            String fuelId = en.getValue().id;
            SkyblockItem fuelItem = SkyblockItem.getItem(fuelId);

            if (fuelItem == null) {
                Utils.visPrint(en.getKey());
                continue;
            }

            PSSToggle toggle = new PSSToggle()
                    .setX(fromWidthScaleFactor(10))
                    .setY(new PixelConstraint(yPos))
                    .setWidth(fromWidthScaleFactor(20).getValue())
                    .setHeight(fromWidthScaleFactor(20).getValue())
                    .setChildOf(mainTextScrollComponent);
            float textXPos = toggle.getComponent().getWidth() + textPad;

            String fuelDisplayName = fuelItem.getName();

            String fuelRarityColor = fuelItem.getRarityColorCode();

            UIWrappedText text = (UIWrappedText) new UIWrappedText(fuelRarityColor + fuelDisplayName)
                    .setX(new PixelConstraint(textXPos))
                    .setY(new CenterConstraint())
                    .setWidth(new PixelConstraint(leftBar.getLeft() - mainTextScrollComponent.getLeft() - textXPos - textPad))
                    .setColor(Color.white)
                    .setTextScale(fromWidthScaleFactor(1))
                    .setChildOf(toggle.getComponent());

            toggle.onMouseClickConsumer(s -> {
                changeFuel(toggle, fuelId);
            });

            text.onMouseClickConsumer(s -> {
                changeFuel(toggle, fuelId);
            });
            components.add(toggle);

            yPos = toggle.getComponent().getBottom() + buttonPad;
        }

        return components;
    }

public HashMap<MinionData.Minion.Upgrade, PSSToggle> addMinionUpgradeButtons() {
        HashMap<MinionData.Minion.Upgrade, PSSToggle> components = new HashMap<>();

        float yPos = fromWidthScaleFactor(10).getValue();

        float textPad = fromWidthScaleFactor(5).getValue();

        float buttonPad = fromWidthScaleFactor(15).getValue();

        for (MinionData.Minion.Upgrade upgrade : MinionData.Minion.Upgrade.values()) {
            PSSToggle toggle = new PSSToggle()
                    .setX(new PixelConstraint(rightBar.getRight() - mainTextScrollComponent.getLeft() + fromWidthScaleFactor(2).getValue()))
                    .setY(new PixelConstraint(yPos))
                    .setWidth(fromWidthScaleFactor(20).getValue())
                    .setHeight(fromWidthScaleFactor(20).getValue())
                    .setChildOf(mainTextScrollComponent);

            float textXPos = toggle.getComponent().getWidth() + textPad;

            String upgradeId = upgrade.toString();

            SkyblockItem upgradeItem = SkyblockItem.getItem(upgradeId);

            if (upgradeItem == null) {
                Utils.visPrint(upgrade.toString());
                continue;
            }

            String upgradeItemName = upgradeItem.getName();

            String upgradeItemColor = upgradeItem.getRarityColorCode();

            UIWrappedText text = (UIWrappedText) new UIWrappedText(upgradeItemColor + upgradeItemName)
                    .setX(new PixelConstraint(textXPos))
                    .setY(new CenterConstraint())
                    .setWidth(new PixelConstraint(rightBar.getRight() - textXPos - textPad))
                    .setColor(Color.white)
                    .setTextScale(fromWidthScaleFactor(1))
                    .setChildOf(toggle.getComponent());

            toggle.onMouseClickConsumer(s -> {
                changeUpgrade(toggle, upgrade);
            });

            text.onMouseClickConsumer(s -> {
                changeUpgrade(toggle, upgrade);
            });

            components.put(upgrade, toggle);

            yPos = toggle.getComponent().getBottom() + buttonPad;
        }

        return components;
    }

//    Sets the upgrades array to the current selected upgrade at index 0, and the
//    second most recently selected upgrade at index 1
    public void changeUpgrade(PSSToggle toggle, MinionData.Minion.Upgrade selectedUpgrade) {
        mainTextScrollComponent.scrollToTop(false);

        MinionData.Minion.Upgrade prevUprgade = null;
//        If the upgrade array has at least one item the previous upgrade is equal to the first item in the list
        if (upgrades.length > 0) {
            prevUprgade = upgrades[0];
        }
        //        If the upgrade is selected already selected, set it to null
        if (!toggle.getState()) {
            if (selectedUpgrade.equals(prevUprgade)) {
                prevUprgade = null;
            }
            Utils.visPrint("deleting");
            selectedUpgrade = null;
        }
        Utils.visPrint(toggle.getState());

        ArrayList< MinionData.Minion.Upgrade> temp = new ArrayList<>();



//        If the selected upgrade is not equal to null, add it
        if (selectedUpgrade != null) {
            temp.add(selectedUpgrade);
        }
        if (prevUprgade != null) {
            temp.add(prevUprgade);
        }

//        Creates a new upgrade array with the right size
        upgrades = new MinionData.Minion.Upgrade[temp.size()];

//        Adds all selected upgrades to the ugrades array
        for (int i = 0; i < temp.size(); i++) {
            upgrades[i] = temp.get(i);
        }

//        Resets all the toggles
        resetUpgradeToggles();

//        Enables the selected toggles
        for (MinionData.Minion.Upgrade up : upgrades) {
            upgradeToggleMap.get(up).setState(true);
        }

//        Refreshes minion data
        this.minionTexts = updateMinionData();

    }

    public void changeFuel(PSSToggle toggle, String fuelId) {
        mainTextScrollComponent.scrollToTop(false);
        boolean newState = toggle.getState();
        resetFuelToggles();
        toggle.setState(newState);
        if (!toggle.getState()) {
            selectedFuel = null;
        }
        else {
            this.selectedFuel = MinionData.fuelMap.get(fuelId);
        }
        this.minionTexts = updateMinionData();
    }

    public void resetFuelToggles() {
        for (PSSToggle toggle : this.fuelToggles) {
            toggle.setState(false);
        }
    }

    public void resetUpgradeToggles() {
        for (PSSToggle toggle : this.upgradeToggleMap.values()) {
            toggle.setState(false);
        }
    }

    public ArrayList<UIComponent> updateMinionData() {
        for (UIComponent minionText : minionTexts) {
            minionText.clearChildren();
            minionText.parent.removeChild(minionText);
        }
        minionTexts.clear();

        return addMinionBreakdownText();
    }

    private PixelConstraint fromWidthScaleFactor(float pos) {
        return new PixelConstraint( (float) (pos * (this.getWindow().getWidth() / 1000d)));
    }
}
