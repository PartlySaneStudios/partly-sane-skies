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
import me.partlysanestudios.partlysaneskies.SkyblockItem;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.guicomponents.PSSButton;
import me.partlysanestudios.partlysaneskies.utils.guicomponents.PSSToggle;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.*;

public class ProfitMinionCalculator extends WindowScreen {
    MinionData.MinionFuel selectedFuel = null;
    MinionData.Minion.Upgrade[] upgrades = {};
    double hours = 24;
    String selectedCategory;

    UIComponent backgroundBox;

    ScrollComponent mainTextScrollComponent;
    UIComponent backgroundImage;
    UIComponent leftBar;
    UIComponent rightBar;
    UIComponent categoriesBar;

    HashMap<String, PSSToggle> fuelToggles;
    ArrayList<UIComponent> minionTexts;
    HashMap<MinionData.Minion.Upgrade, PSSToggle> upgradeToggleMap;

    static final String[] categories = {"ALL", "FORAGING", "MINING", "FISHING", "FARMING", "COMBAT"};
    static final HashMap<String, String> categoriesColorMap = new HashMap<String, String>();

    public ProfitMinionCalculator(ElementaVersion version) {
        super(version);
        categoriesColorMap.put("ALL", "§b");
        categoriesColorMap.put("FORAGING", "§6");
        categoriesColorMap.put("MINING", "§7");
        categoriesColorMap.put("FISHING", "§9");
        categoriesColorMap.put("FARMING", "§a");
        categoriesColorMap.put("COMBAT", "§c");

        setUpBackground();
        this.minionTexts = addMinionBreakdownText("ALL");
        this.fuelToggles = addFuelButtons();
        this.upgradeToggleMap = addMinionUpgradeButtons();
        getBestMinionSettings();
        addCategories();
    }

    public void setUpBackground() {
        this.backgroundBox = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setHeight(fromWidthScaleFactor(400))
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

        float categoriesBarHeight = fromWidthScaleFactor(40).getValue();
        float categoriesBarPad = fromWidthScaleFactor(7).getValue();
        this.categoriesBar = Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies:textures/gui/base_color_background.png"))
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(backgroundBox.getTop() -(categoriesBarHeight + categoriesBarPad)))
                .setWidth(new PixelConstraint(backgroundBox.getWidth()))
                .setHeight(new PixelConstraint(categoriesBarHeight))
                .setChildOf(getWindow());
    }

//    Adds the most profitable minion's text to the middle section. Also returns a list with the objects in order
    public ArrayList<UIComponent> addMinionBreakdownText(String category) {
        selectedCategory = category;
        ArrayList<UIComponent> components = new ArrayList<>();
//        Most profitable minions
        HashMap<MinionData.Minion, Double> mostProfitableMinions = MinionData.getMostProfitMinion(this.upgrades, this.selectedFuel);
        int i = 1; // Rank

//        Starting y location
        float yPos = fromWidthScaleFactor(10).getValue();
//        Offset between the top of the text and the bar
        float barOffset = fromWidthScaleFactor(10).getValue();
//        Distance between the bar and the edge of the middle section
        float barNegation = fromWidthScaleFactor(66).getValue();

        for (Map.Entry<MinionData.Minion, Double> en : mostProfitableMinions.entrySet()) {
            if (!(category.equals("ALL") || en.getKey().category.equals(category))) {
                continue;
            }
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

            yPos = border.getBottom() + barOffset - mainTextScrollComponent.getTop();
            i++;
        }

        return components;
    }

    public HashMap<String, PSSToggle> addFuelButtons() {
        HashMap<String, PSSToggle> components = new HashMap<>();

        float yPos = fromWidthScaleFactor(5).getValue();

        float textPad = fromWidthScaleFactor(5).getValue();

        float buttonPad = fromWidthScaleFactor(7).getValue();

        for (Map.Entry<String, MinionData.MinionFuel> en : MinionData.fuelMap.entrySet()) {

            String fuelId = en.getValue().id;
            SkyblockItem fuelItem = SkyblockItem.getItem(fuelId);

            if (fuelItem == null) {
                Utils.visPrint(en.getKey());
                continue;
            }

            UIComponent fuelContainer = new UIBlock()
                    .setX(fromWidthScaleFactor(10))
                    .setY(new PixelConstraint(yPos))
                    .setWidth(new PixelConstraint(leftBar.getLeft() - mainTextScrollComponent.getLeft() - fromWidthScaleFactor(20).getValue()))
                    .setHeight(fromWidthScaleFactor(40))
                    .setColor(new Color(255, 255, 255, 0))
                    .setChildOf(mainTextScrollComponent);

            PSSToggle toggle = new PSSToggle()
                    .setX(fromWidthScaleFactor(0))
                    .setY(new CenterConstraint())
                    .setWidth(fromWidthScaleFactor(25).getValue())
                    .setHeight(fromWidthScaleFactor(25).getValue())
                    .setChildOf(fuelContainer);
            float textXPos = toggle.getComponent().getWidth() + textPad;

            String fuelDisplayName = fuelItem.getName();

            String fuelRarityColor = fuelItem.getRarityColorCode();

            UIWrappedText text = (UIWrappedText) new UIWrappedText(fuelRarityColor + fuelDisplayName)
                    .setX(new PixelConstraint(textXPos))
                    .setY(new CenterConstraint())
                    .setWidth(new PixelConstraint(backgroundBox.getWidth() - textXPos))
                    .setColor(Color.white)
                    .setTextScale(fromWidthScaleFactor(1))
                    .setChildOf(fuelContainer);

            fuelContainer.onMouseClickConsumer(s -> {
                toggle.toggleState();
                changeFuel(fuelId, toggle.getState());
            });
            components.put(fuelId, toggle);

            yPos = fuelContainer.getBottom() + buttonPad - mainTextScrollComponent.getTop();
        }

        return components;
    }

public HashMap<MinionData.Minion.Upgrade, PSSToggle> addMinionUpgradeButtons() {
        HashMap<MinionData.Minion.Upgrade, PSSToggle> components = new HashMap<>();

        float yPos = fromWidthScaleFactor(5).getValue();

        float textPad = fromWidthScaleFactor(5).getValue();

        float buttonPad = fromWidthScaleFactor(7).getValue();

        for (MinionData.Minion.Upgrade upgrade : MinionData.Minion.Upgrade.values()) {
            String upgradeId = upgrade.toString();

            SkyblockItem upgradeItem = SkyblockItem.getItem(upgradeId);
            if (upgradeItem == null) {
                Utils.visPrint(upgrade.toString());
                continue;
            }

            UIComponent upgradeContainer = new UIBlock()
                    .setX(new PixelConstraint(rightBar.getRight() - mainTextScrollComponent.getLeft() + fromWidthScaleFactor(2).getValue()))
                    .setY(new PixelConstraint(yPos))
                    .setWidth(new PixelConstraint(rightBar.getRight() - mainTextScrollComponent.getLeft() - fromWidthScaleFactor(20).getValue()))
                    .setHeight(fromWidthScaleFactor(40))
                    .setColor(new Color(255, 255, 255, 0))
                    .setChildOf(mainTextScrollComponent);

            PSSToggle toggle = new PSSToggle()
                    .setX(fromWidthScaleFactor(0))
                    .setY(new CenterConstraint())
                    .setWidth(fromWidthScaleFactor(25).getValue())
                    .setHeight(fromWidthScaleFactor(25).getValue())
                    .setChildOf(upgradeContainer);
            float textXPos = toggle.getComponent().getWidth() + textPad;


            String upgradeItemName = upgradeItem.getName();

            String upgradeItemColor = upgradeItem.getRarityColorCode();

            UIWrappedText text = (UIWrappedText) new UIWrappedText(upgradeItemColor + upgradeItemName)
                    .setX(new PixelConstraint(textXPos))
                    .setY(new CenterConstraint())
                    .setWidth(new PixelConstraint(backgroundBox.getWidth() - textXPos))
                    .setColor(Color.white)
                    .setTextScale(fromWidthScaleFactor(1))
                    .setChildOf(upgradeContainer);

            upgradeContainer.onMouseClickConsumer(s -> {
                toggle.toggleState();
                changeUpgrade(upgrade, toggle.getState());
            });

            components.put(upgrade, toggle);

            yPos = upgradeContainer.getBottom() + buttonPad - mainTextScrollComponent.getTop();
        }

        return components;
    }


    public void addCategories() {
        float pad = fromWidthScaleFactor(10).getValue();
        float blockWidth = (categoriesBar.getWidth() - (categories.length + 1) * pad) / categories.length;

        float xPos = pad;
        for (String category : categories) {
            PSSButton button = new PSSButton()
                    .setX(new PixelConstraint(xPos))
                    .setY(new CenterConstraint())
                    .setWidth(blockWidth)
                    .setHeight(categoriesBar.getHeight() * .9f)
                    .setChildOf(categoriesBar);

            button.setText(categoriesColorMap.get(category) + StringUtils.titleCase(category));

            button.onMouseClickConsumer(s -> {
                selectedCategory = category;
                this.minionTexts = updateMinionData();
            });

            xPos += blockWidth + pad ;
        }
    }

//    Sets the upgrades array to the current selected upgrade at index 0, and the
//    second most recently selected upgrade at index 1
    public void changeUpgrade(MinionData.Minion.Upgrade selectedUpgrade, boolean state) {
        PSSToggle toggle = upgradeToggleMap.get(selectedUpgrade);
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

    public void changeFuel(String fuelId, boolean state) {
        mainTextScrollComponent.scrollToTop(false);
        resetFuelToggles();
        PSSToggle toggle = fuelToggles.get(fuelId);
        this.selectedFuel = null;
        toggle.setState(state);
        if (state) {
            this.selectedFuel = MinionData.fuelMap.get(fuelId);
        }

        this.minionTexts = updateMinionData();


    }

    public void resetFuelToggles() {
        for (PSSToggle toggle : this.fuelToggles.values()) {
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

        return addMinionBreakdownText(this.selectedCategory);
    }

    private PixelConstraint fromWidthScaleFactor(float pos) {
        return new PixelConstraint( (float) (pos * (this.getWindow().getWidth() / 1000d)));
    }

    public void getBestMinionSettings() {
        long startTime = PartlySaneSkies.getTime();
        Utils.visPrint("Start Time: " + startTime);
        double bestProfit = -Integer.MIN_VALUE;

        int bestTier = -1;
        MinionData.Minion.Upgrade[] bestUpgrades = new MinionData.Minion.Upgrade[2];
        MinionData.MinionFuel bestMinionFuel = null;

        long possibleCombos = 0;
        for (int i = 1; i <= 12; i++) { // Best tier
            for (MinionData.MinionFuel fuel : MinionData.fuelMap.values()) { // Best fuel
                for (MinionData.Minion.Upgrade upgrade1 : MinionData.Minion.Upgrade.values()) { // Best upgrade 1
                    for (MinionData.Minion.Upgrade upgrade2 : MinionData.Minion.Upgrade.values()) { // Best Upgrade 2;
                        possibleCombos ++;
                        if (upgrade1.equals(upgrade2)) {
                            continue;
                        }
                        if (upgrade1.equals(MinionData.Minion.Upgrade.LESSER_SOULFLOW_ENGINE) && upgrade2.equals(MinionData.Minion.Upgrade.SOULFLOW_ENGINE)) {
                            continue;
                        }
                        if (upgrade2.equals(MinionData.Minion.Upgrade.LESSER_SOULFLOW_ENGINE) && upgrade1.equals(MinionData.Minion.Upgrade.SOULFLOW_ENGINE)) {
                            continue;
                        }

                        MinionData.Minion.Upgrade[] testUpgrades = {upgrade1, upgrade2};

                        LinkedHashMap< MinionData.Minion, Double> bestMinionsMap = MinionData.getMostProfitMinion(testUpgrades, fuel);

                    double testMinionPrice = Integer.MIN_VALUE;
                        for (double val : bestMinionsMap.values()) {
                            if (val > testMinionPrice) {
                                testMinionPrice = val;
                            }
                        }

                        if (testMinionPrice > bestProfit) {
                            bestTier = i;
                            bestUpgrades = testUpgrades;
                            bestMinionFuel = fuel;
                        }

                    }
                }

            }
        }


        Utils.visPrint("Best Tier: " + bestTier + "\nBest Fuel: " + bestMinionFuel.id + "Best Upgrades" + bestUpgrades[0] + " " + bestUpgrades[1]);

        long endTime = PartlySaneSkies.getTime();
        changeFuel(bestMinionFuel.id, true);
        changeUpgrade(bestUpgrades[0], true);
        changeUpgrade(bestUpgrades[1], true);
        Utils.visPrint("Possible Combos: " + possibleCombos);
        Utils.visPrint("End Time: " + endTime);
        Utils.visPrint("Total Time: " + (endTime - startTime));
    }
}
