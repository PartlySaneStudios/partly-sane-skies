//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

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
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockItem;
import me.partlysanestudios.partlysaneskies.system.ThemeManager;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.system.guicomponents.PSSButton;
import me.partlysanestudios.partlysaneskies.system.guicomponents.PSSToggle;

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
    UIComponent bestMinionBar;

    HashMap<String, PSSToggle> fuelToggles;
    ArrayList<UIComponent> minionTexts;
    HashMap<MinionData.Minion.Upgrade, PSSToggle> upgradeToggleMap;
    public int upgradeSlotsUnavailable = 0;

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
        addCategories();
        addBestMinionCalculator();
    }

    public void setUpBackground() {
        this.backgroundBox = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setHeight(fromWidthScaleFactor(333))
                .setWidth(fromWidthScaleFactor(850))
                .setColor(Color.red)
                .setChildOf(getWindow());

        this.backgroundImage = ThemeManager.getCurrentBackgroundUIImage()
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
                .setColor(ThemeManager.getAccentColor().toJavaColor())
                .setChildOf(backgroundImage);

        this.rightBar = new UIBlock()
                .setX(new PixelConstraint(backgroundImage.getWidth() * (4/5f)))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(backgroundImage.getHeight() * .85f))
                .setWidth(fromWidthScaleFactor(2f))
                .setColor(ThemeManager.getAccentColor().toJavaColor())
                .setChildOf(backgroundImage);

        float categoriesBarHeight = fromWidthScaleFactor(75).getValue();
        float categoriesBarPad = fromWidthScaleFactor(5).getValue();
        this.categoriesBar = ThemeManager.getCurrentBackgroundUIImage()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(backgroundBox.getTop() -(categoriesBarHeight + categoriesBarPad)))
                .setWidth(new PixelConstraint(backgroundBox.getWidth()))
                .setHeight(new PixelConstraint(categoriesBarHeight))
                .setChildOf(getWindow());

        this.bestMinionBar = ThemeManager.getCurrentBackgroundUIImage()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(backgroundBox.getBottom() + categoriesBarPad))
                .setWidth(new PixelConstraint(backgroundBox.getWidth()/3f))
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
                    .setX(new PixelConstraint(leftBar.getRight() + fromWidthScaleFactor(7).getValue())) // sets it 7 scales pixels off the right side of the left bar
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
                    .setColor(ThemeManager.getAccentColor().toJavaColor())
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
            SkyblockItem fuelItem = SkyblockDataManager.getItem(fuelId);

            if (fuelItem == null) {

                continue;
            }

            PSSButton fuelContainer = new PSSButton()
                    .setX(fromWidthScaleFactor(10))
                    .setY(new PixelConstraint(yPos))
                    .setWidth(leftBar.getLeft() - mainTextScrollComponent.getLeft() - fromWidthScaleFactor(20).getValue())
                    .setHeight(fromWidthScaleFactor(40).getValue())
                    .setChildOf(mainTextScrollComponent);

            PSSToggle toggle = new PSSToggle()
                    .setX(fromWidthScaleFactor(0))
                    .setY(new CenterConstraint())
                    .setWidth(fromWidthScaleFactor(25).getValue())
                    .setHeight(fromWidthScaleFactor(25).getValue())
                    .setChildOf(fuelContainer.getComponent());
            float textXPos = toggle.getComponent().getWidth() + textPad;

            String fuelDisplayName = fuelItem.getName();

            String fuelRarityColor = fuelItem.getRarityColorCode();

            UIWrappedText text = (UIWrappedText) new UIWrappedText(fuelRarityColor + fuelDisplayName)
                    .setX(new PixelConstraint(textXPos))
                    .setY(new CenterConstraint())
                    .setWidth(new PixelConstraint(backgroundBox.getWidth() - textXPos))
                    .setColor(Color.white)
                    .setTextScale(fromWidthScaleFactor(1))
                    .setChildOf(fuelContainer.getComponent());

            fuelContainer.onMouseClickConsumer(s -> {
                toggle.toggleState();
                changeFuel(fuelId, toggle.getState());
            });
            components.put(fuelId, toggle);

            yPos = fuelContainer.getComponent().getBottom() + buttonPad - mainTextScrollComponent.getTop();
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

            SkyblockItem upgradeItem = SkyblockDataManager.getItem(upgradeId);
            if (upgradeItem == null) {
                continue;
            }

            PSSButton upgradeContainer = new PSSButton()
                    .setX(new PixelConstraint(rightBar.getRight() - rightBar.getWidth() - mainTextScrollComponent.getLeft() + fromWidthScaleFactor(10).getValue()))
                    .setY(new PixelConstraint(yPos))
                    .setWidth(mainTextScrollComponent.getRight() - rightBar.getRight() - rightBar.getWidth() - fromWidthScaleFactor(20).getValue())
                    .setHeight(fromWidthScaleFactor(40).getValue())
                    .setChildOf(mainTextScrollComponent);

            PSSToggle toggle = new PSSToggle()
                    .setX(fromWidthScaleFactor(0))
                    .setY(new CenterConstraint())
                    .setWidth(fromWidthScaleFactor(25).getValue())
                    .setHeight(fromWidthScaleFactor(25).getValue())
                    .setChildOf(upgradeContainer.getComponent());
            float textXPos = toggle.getComponent().getWidth() + textPad;


            String upgradeItemName = upgradeItem.getName();

            String upgradeItemColor = upgradeItem.getRarityColorCode();

            UIWrappedText text = (UIWrappedText) new UIWrappedText(upgradeItemColor + upgradeItemName)
                    .setX(new PixelConstraint(textXPos))
                    .setY(new CenterConstraint())
                    .setColor(Color.white)
                    .setTextScale(fromWidthScaleFactor(1))
                    .setChildOf(upgradeContainer.getComponent());

            upgradeContainer.onMouseClickConsumer(s -> {
                toggle.toggleState();
                changeUpgrade(upgrade, toggle.getState());
            });

            components.put(upgrade, toggle);

            yPos = upgradeContainer.getComponent().getBottom() + buttonPad - mainTextScrollComponent.getTop();
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

            button.setText(categoriesColorMap.get(category) + StringUtils.INSTANCE.titleCase(category));

            button.onMouseClickConsumer(s -> {
                selectedCategory = category;
                this.minionTexts = updateMinionData();
            });

            xPos += blockWidth + pad ;
        }
    }

    public static void registerCommand() {
        new PSSCommand("minioncalculator")
                .addAlias("minioncalc")
                .addAlias("bestminion")
                .addAlias("mc")
                .addAlias("bm")
                .setDescription("Opens the best minion calculator")
                .setRunnable((s,a) -> {
                    ChatUtils.INSTANCE.sendClientMessage("§bOpening Minion Calculator...");
                    new Thread(() -> PartlySaneSkies.minecraft.addScheduledTask(() -> {
                        // Code to test the minion classes
                        ProfitMinionCalculator calc = new ProfitMinionCalculator(ElementaVersion.V2);
                        PartlySaneSkies.minecraft.displayGuiScreen(calc);
                    })).start();
                })
                .register();
    }

//    Upgrades that don't actually modify anything
    String[] uselessUpgrades = {"Auto-Smelter", "Compactor", "Super Compactor", "Dwarven Super Compactor"};
    public void addBestMinionCalculator() {
        float heightPos = 0;
        float yPad = fromWidthScaleFactor(6).getValue();

        for (String upgrade : uselessUpgrades) {
            heightPos += yPad;
            PSSToggle toggle = new PSSToggle()
                    .setX(fromWidthScaleFactor(10))
                    .setY(new PixelConstraint(heightPos))
                    .setHeight(fromWidthScaleFactor(12).getValue())
                    .setWidth(fromWidthScaleFactor(12).getValue())
                    .setChildOf(bestMinionBar);

            toggle.onMouseClickConsumer(s -> {
                toggle.toggleState();

                if (toggle.getState()) {
                    upgradeSlotsUnavailable++;
                }
                else {
                    upgradeSlotsUnavailable--;
                }
            });

            UIWrappedText text = (UIWrappedText) new UIWrappedText(upgrade)
                    .setX(fromWidthScaleFactor(13))
                    .setY(new CenterConstraint())
                    .setHeight(fromWidthScaleFactor(5))
                    .setTextScale(fromWidthScaleFactor(.75f))
                    .setColor(Color.gray)
                    .setChildOf(toggle.getComponent());

            heightPos += fromWidthScaleFactor(12).getValue();
        }

        PSSButton button = new PSSButton(Color.green)
                .setX(fromWidthScaleFactor(180))
                .setY(new CenterConstraint())
                .setHeight(fromWidthScaleFactor(60).getValue())
                .setWidth(fromWidthScaleFactor(100).getValue())
                .setText("Calculate Best Minion")
                .setTextScale(fromWidthScaleFactor(1).getValue())

                .setChildOf(bestMinionBar);

        button.onMouseClickConsumer(s -> {
            getBestMinionSettings();
        });
    }

//    Sets the upgrades array to the current selected upgrade at index 0, and the
//    second most recently selected upgrade at index 1
    public void changeUpgrade(MinionData.Minion.Upgrade selectedUpgrade, boolean state) {
        PSSToggle toggle = upgradeToggleMap.get(selectedUpgrade);
        mainTextScrollComponent.scrollToTop(false);

        MinionData.Minion.Upgrade prevUpgrade = null;
//        If the upgrade array has at least one item, the previous upgrade is equal to the first item in the list
        if (upgrades.length > 0) {
            prevUpgrade = upgrades[0];
        }

        resetUpgradeToggles();

        ArrayList< MinionData.Minion.Upgrade> temp = new ArrayList<>();
//        If the selected upgrade is not equal to null, add it
        if (selectedUpgrade != null) {
            temp.add(selectedUpgrade);
        }
        if (prevUpgrade != null) {
            temp.add(prevUpgrade);
        }

//        Creates a new upgrade array with the right size
        upgrades = new MinionData.Minion.Upgrade[temp.size()];

//        Adds all selected upgrades to the upgrade array
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

    public void resetFuels() {
        selectedFuel = null;
        resetFuelToggles();
    }

    public void resetUpgrades() {
        upgrades = new MinionData.Minion.Upgrade[] {};
        resetUpgradeToggles();
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
        double bestProfit = Integer.MIN_VALUE;

        int availableSlots = 2 - upgradeSlotsUnavailable;
        if (availableSlots < 0) {
            availableSlots = 0;
        }


        MinionData.Minion.Upgrade[] bestUpgrades = new MinionData.Minion.Upgrade[2];
        MinionData.MinionFuel bestMinionFuel = null;

        long possibleCombos = 0;
        for (MinionData.MinionFuel fuel : MinionData.fuelMap.values()) { // Best fuel
            for (MinionData.Minion.Upgrade upgrade1 : MinionData.Minion.Upgrade.values()) { // Best upgrade 1
                for (MinionData.Minion.Upgrade upgrade2 : MinionData.Minion.Upgrade.values()) { // Best Upgrade 2;
                    possibleCombos++;
                    if (upgrade1.equals(upgrade2)) {
                        continue;
                    }
                    if (upgrade1.equals(MinionData.Minion.Upgrade.LESSER_SOULFLOW_ENGINE) && upgrade2.equals(MinionData.Minion.Upgrade.SOULFLOW_ENGINE)) {
                        continue;
                    }
                    if (upgrade2.equals(MinionData.Minion.Upgrade.LESSER_SOULFLOW_ENGINE) && upgrade1.equals(MinionData.Minion.Upgrade.SOULFLOW_ENGINE)) {
                        continue;
                    }

                    MinionData.Minion.Upgrade[] testUpgrades = null;
                    switch (availableSlots) {
                        case 0:
                            testUpgrades = new MinionData.Minion.Upgrade[] {};
                            break;
                        case 1:
                            testUpgrades = new MinionData.Minion.Upgrade[] {upgrade1};
                            break;

                        case 2:
                            testUpgrades = new MinionData.Minion.Upgrade[] {upgrade1, upgrade2};
                            break;
                    }

                    LinkedHashMap<MinionData.Minion, Double> bestMinionsMap = MinionData.getMostProfitMinion(testUpgrades, fuel);

//                        Gets the price of the current test
                    double testMinionPrice = Integer.MIN_VALUE;
                    for (double val : bestMinionsMap.values()) {
                        if (val > testMinionPrice) {
                            testMinionPrice = val;
                        }
                    }

                    if (testMinionPrice > bestProfit) {
                        bestUpgrades = testUpgrades;
                        bestMinionFuel = fuel;
                    }

                    System.out.println(Arrays.asList(testUpgrades));
                }
            }
        }

        resetFuels();
        if (bestMinionFuel != null) {
            changeFuel(bestMinionFuel.id, true);
        }

        resetUpgrades();
        for (MinionData.Minion.Upgrade upgrade : bestUpgrades) {
            changeUpgrade(upgrade, true);
        }
    }
}
