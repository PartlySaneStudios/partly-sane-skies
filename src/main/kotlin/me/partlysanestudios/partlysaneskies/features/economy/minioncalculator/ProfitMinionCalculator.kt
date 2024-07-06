package me.partlysanestudios.partlysaneskies.features.economy.minioncalculator

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.getItem
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.accentColor
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.currentBackgroundUIImage
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSButton
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSToggle
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.titleCase
import java.awt.Color

class ProfitMinionCalculator(version: ElementaVersion) : WindowScreen(version) {
    companion object {
        val categories = listOf("ALL", "FORAGING", "MINING", "FISHING", "FARMING", "COMBAT")
        val categoriesColorMap = hashMapOf(
            "ALL" to "§b",
            "FORAGING" to "§6",
            "MINING" to "§7",
            "FISHING" to "§9",
            "FARMING" to "§a",
            "COMBAT" to "§c",
        )
        fun registerCommand() {
            PSSCommand("minioncalculator")
                .addAlias("minioncalc", "bestminion", "mc")
                .setDescription("Opens the Profit Minion Calculator")
                .setRunnable {
                    ChatUtils.sendClientMessage("§bOpening Minion Calculator...")
                    MinecraftUtils.displayGuiScreen(ProfitMinionCalculator(@Suppress("DEPRECATION") ElementaVersion.V2))
                }.register()
        }
    }

    private var upgradeSlotsUnavailable = 0
    private var selectedFuel: MinionData.MinionFuel? = null
    private var upgrades = listOf<MinionData.Minion.Upgrade>()
    private var hours = 24.0
    private var selectedCategory = "ALL"
    lateinit var backgroundBox: UIComponent
    lateinit var mainTextScrollComponent: ScrollComponent
    lateinit var backgroundImage: UIComponent
    lateinit var leftBar: UIComponent
    lateinit var rightBar: UIComponent
    lateinit var categoriesBar: UIComponent
    lateinit var bestMinionBar: UIComponent
    private var fuelToggles = HashMap<String, PSSToggle>()
    private var minionTexts = mutableListOf<UIComponent>()
    private var upgradeToggleMap = HashMap<MinionData.Minion.Upgrade, PSSToggle>()
    private val uselessUpgrades = setOf("Auto-Smelter", "Compactor", "Super Compactor", "Dwarven Super Compactor")

    init {
        setUpBackground()
        minionTexts = addMinionBreakdownText("ALL")
        fuelToggles = addFuelButtons()
        upgradeToggleMap = addMinionUpgradeButtons()
        addCategories()
        addBestMinionCalculator()
    }

    fun setUpBackground() {
        backgroundBox = UIBlock()
            .setX(CenterConstraint())
            .setY(CenterConstraint())
            .setHeight(fromWidthScaleFactor(333f))
            .setWidth(fromWidthScaleFactor(850f))
            .setColor(Color.red)
            .setChildOf(window)

        backgroundImage = currentBackgroundUIImage
            .setX(CenterConstraint())
            .setY(CenterConstraint())
            .setHeight(PixelConstraint(backgroundBox.getHeight()))
            .setWidth(PixelConstraint(backgroundBox.getWidth()))
            .setChildOf(backgroundBox)

        mainTextScrollComponent = (ScrollComponent()
            .setY(CenterConstraint())
            .setX(CenterConstraint())
            .setWidth(PixelConstraint(backgroundBox.getWidth()))
            .setHeight(PixelConstraint(backgroundBox.getHeight()))
            .setColor(Color.red)
            .setChildOf(backgroundImage) as ScrollComponent)

        leftBar = UIBlock()
            .setX(PixelConstraint(backgroundImage.getWidth() * (1 / 5f)))
            .setY(CenterConstraint())
            .setHeight(PixelConstraint(backgroundImage.getHeight() * .85f))
            .setWidth(fromWidthScaleFactor(2f))
            .setColor(accentColor.toJavaColor())
            .setChildOf(backgroundImage)

        rightBar = UIBlock()
            .setX(PixelConstraint(backgroundImage.getWidth() * (4 / 5f)))
            .setY(CenterConstraint())
            .setHeight(PixelConstraint(backgroundImage.getHeight() * .85f))
            .setWidth(fromWidthScaleFactor(2f))
            .setColor(accentColor.toJavaColor())
            .setChildOf(backgroundImage)

        val categoriesBarHeight = fromWidthScaleFactor(75f).value
        val categoriesBarPad = fromWidthScaleFactor(5f).value
        categoriesBar = currentBackgroundUIImage
            .setX(CenterConstraint())
            .setY(PixelConstraint(backgroundBox.getTop() - (categoriesBarHeight + categoriesBarPad)))
            .setWidth(PixelConstraint(backgroundBox.getWidth()))
            .setHeight(PixelConstraint(categoriesBarHeight))
            .setChildOf(window)

        bestMinionBar = currentBackgroundUIImage
            .setX(CenterConstraint())
            .setY(PixelConstraint(backgroundBox.getBottom() + categoriesBarPad))
            .setWidth(PixelConstraint(backgroundBox.getWidth() / 3f))
            .setHeight(PixelConstraint(categoriesBarHeight))
            .setChildOf(window)
    }

    //    Adds the most profitable minion's text to the middle section. Also returns a list with the objects in order
    fun addMinionBreakdownText(category: String): ArrayList<UIComponent> {
        selectedCategory = category
        val components = ArrayList<UIComponent>()
        //        Most profitable minions
        val mostProfitableMinions: java.util.HashMap<MinionData.Minion, Double?> = MinionData.getMostProfitMinion(
            upgrades,
            selectedFuel,
        )
        var i = 1 // Rank

        //        Starting y location
        var yPos = fromWidthScaleFactor(10f).value
        //        Offset between the top of the text and the bar
        val barOffset = fromWidthScaleFactor(10f).value
        //        Distance between the bar and the edge of the middle section
        val barNegation = fromWidthScaleFactor(66f).value

        for ((key) in mostProfitableMinions) {
            if (!(category == "ALL" || key.category == category)) {
                continue
            }
            //            Creates a string with the Minion name, and it's cost breakdown
            val str = "§7" + i + ". " + key.costBreakdown(key.maxTier, hours, upgrades, selectedFuel)

            //            Creates a WrappedText object with said string
            val text = UIWrappedText(str)
                .setText(str)
                .setX(PixelConstraint(leftBar.getRight() + fromWidthScaleFactor(7f).value)) // sets it 7 scales pixels off the right side of the left bar
                .setY(PixelConstraint(yPos))
                .setWidth(PixelConstraint(rightBar.getLeft() - leftBar.getRight() - fromWidthScaleFactor(14f).value)) // set the width to the distance between the two bars with 7 scale pixels of padding on either side
                .setTextScale(fromWidthScaleFactor(1f)) //Sets the text scale to 1 scale unit
                .setColor(Color.WHITE)
                .setTextScale(fromWidthScaleFactor(1f))
                .setChildOf(mainTextScrollComponent)

            //            Creates a line separating the text
            val border = UIBlock()
                .setX(PixelConstraint(leftBar.getRight() - text.getLeft() + barNegation))
                .setY(PixelConstraint(text.getHeight() + barOffset))
                .setWidth(PixelConstraint(rightBar.getLeft() - leftBar.getRight() - 2 * barNegation)) // set the width to the distance between the two bars with barNegation scale pixels of padding on either side
                .setHeight(fromWidthScaleFactor(1f))
                .setColor(accentColor.toJavaColor())
                .setChildOf(text)

            components.add(text)

            yPos = border.getBottom() + barOffset - mainTextScrollComponent.getTop()
            i++
        }

        return components
    }

    fun addFuelButtons(): java.util.HashMap<String, PSSToggle> {
        val components = java.util.HashMap<String, PSSToggle>()

        var yPos = fromWidthScaleFactor(5f).value
        val textPad = fromWidthScaleFactor(5f).value
        val buttonPad = fromWidthScaleFactor(7f).value

        for ((_, value) in MinionData.fuelMap) {
            val fuelId = value.id
            val fuelItem = getItem(fuelId) ?: continue

            val fuelContainer = PSSButton()
                .setX(fromWidthScaleFactor(10f))
                .setY(PixelConstraint(yPos))
                .setWidth(leftBar.getLeft() - mainTextScrollComponent.getLeft() - fromWidthScaleFactor(20f).value)
                .setHeight(fromWidthScaleFactor(40f).value)
                .setChildOf(mainTextScrollComponent)

            val toggle = PSSToggle()
                .setX(fromWidthScaleFactor(0f))
                .setY(CenterConstraint())
                .setWidth(fromWidthScaleFactor(25f))
                .setHeight(fromWidthScaleFactor(25f))
                .setChildOf(fuelContainer.component)
            val textXPos: Float = toggle.component.getWidth() + textPad

            val fuelDisplayName: String = fuelItem.name

            val fuelRarityColor: String = fuelItem.rarity.colorCode

            UIWrappedText(fuelRarityColor + fuelDisplayName)
                .setX(PixelConstraint(textXPos))
                .setY(CenterConstraint())
                .setWidth(PixelConstraint(backgroundBox.getWidth() - textXPos))
                .setColor(Color.white)
                .setTextScale(fromWidthScaleFactor(1f))
                .setChildOf(fuelContainer.component) as UIWrappedText

            fuelContainer.onMouseClickConsumer {
                toggle.toggleState()
                changeFuel(fuelId, toggle.getState())
            }
            components[fuelId] = toggle

            yPos = fuelContainer.component.getBottom() + buttonPad - mainTextScrollComponent.getTop()
        }

        return components
    }

    fun addMinionUpgradeButtons(): java.util.HashMap<MinionData.Minion.Upgrade, PSSToggle> {
        val components = java.util.HashMap<MinionData.Minion.Upgrade, PSSToggle>()

        var yPos = fromWidthScaleFactor(5f).value
        val textPad = fromWidthScaleFactor(5f).value
        val buttonPad = fromWidthScaleFactor(7f).value

        for (upgrade in MinionData.Minion.Upgrade.entries) {
            val upgradeId = upgrade.toString()

            val upgradeItem = getItem(upgradeId) ?: continue
            val upgradeContainer = PSSButton()
                .setX(
                    PixelConstraint(
                        rightBar.getRight() - rightBar.getWidth() - mainTextScrollComponent.getLeft() + fromWidthScaleFactor(
                            10f,
                        ).value,
                    ),
                )
                .setY(PixelConstraint(yPos))
                .setWidth(mainTextScrollComponent.getRight() - rightBar.getRight() - rightBar.getWidth() - fromWidthScaleFactor(20f).value)
                .setHeight(fromWidthScaleFactor(40f).value)
                .setChildOf(mainTextScrollComponent)

            val toggle = PSSToggle()
                .setX(fromWidthScaleFactor(0f))
                .setY(CenterConstraint())
                .setWidth(fromWidthScaleFactor(25f))
                .setHeight(fromWidthScaleFactor(25f))
                .setChildOf(upgradeContainer.component)

            val textXPos: Float = toggle.component.getWidth() + textPad


            val upgradeItemName: String = upgradeItem.name

            val upgradeItemColor: String = upgradeItem.rarity.colorCode

            UIWrappedText(upgradeItemColor + upgradeItemName)
                .setX(PixelConstraint(textXPos))
                .setY(CenterConstraint())
                .setColor(Color.white)
                .setTextScale(fromWidthScaleFactor(1f))
                .setChildOf(upgradeContainer.component) as UIWrappedText

            upgradeContainer.onMouseClickConsumer {
                toggle.toggleState()
                changeUpgrade(upgrade)
            }

            components[upgrade] = toggle

            yPos = upgradeContainer.component.getBottom() + buttonPad - mainTextScrollComponent.getTop()
        }

        return components
    }

    fun addCategories() {
        val pad = fromWidthScaleFactor(10f).value
        val blockWidth = (categoriesBar.getWidth() - (categories.size + 1) * pad) / categories.size

        var xPos = pad
        for (category in categories) {
            val button = PSSButton()
                .setX(PixelConstraint(xPos))
                .setY(CenterConstraint())
                .setWidth(blockWidth)
                .setHeight(categoriesBar.getHeight() * .9f)
                .setChildOf(categoriesBar)

            button.setText(categoriesColorMap[category] + category.titleCase())

            button.onMouseClickConsumer {
                selectedCategory = category
                minionTexts = updateMinionData()
            }

            xPos += blockWidth + pad
        }
    }

    fun addBestMinionCalculator() {
        var heightPos = 0f
        val yPad = fromWidthScaleFactor(6f).value

        for (upgrade in uselessUpgrades) {
            heightPos += yPad
            val toggle = PSSToggle()
                .setX(fromWidthScaleFactor(10f))
                .setY(PixelConstraint(heightPos))
                .setHeight(fromWidthScaleFactor(12f))
                .setWidth(fromWidthScaleFactor(12f))
                .setChildOf(bestMinionBar)

            toggle.onMouseClickConsumer {
                toggle.toggleState()
                if (toggle.getState()) {
                    upgradeSlotsUnavailable++
                } else {
                    upgradeSlotsUnavailable--
                }
            }

            UIWrappedText(upgrade)
                .setX(fromWidthScaleFactor(13f))
                .setY(CenterConstraint())
                .setHeight(fromWidthScaleFactor(5f))
                .setTextScale(fromWidthScaleFactor(.75f))
                .setColor(Color.gray)
                .setChildOf(toggle.component) as UIWrappedText

            heightPos += fromWidthScaleFactor(12f).value
        }

        val button = PSSButton(Color.green)
            .setX(fromWidthScaleFactor(180f))
            .setY(CenterConstraint())
            .setHeight(fromWidthScaleFactor(60f).value)
            .setWidth(fromWidthScaleFactor(100f).value)
            .setText("Calculate Best Minion")
            .setTextScale(fromWidthScaleFactor(1f).value)

            .setChildOf(bestMinionBar)

        button.onMouseClickConsumer {
            getBestMinionSettings()
        }
    }

    //    Sets the upgrades array to the current selected upgrade at index 0, and the
    //    second most recently selected upgrade at index 1
    fun changeUpgrade(selectedUpgrade: MinionData.Minion.Upgrade?) {
        mainTextScrollComponent.scrollToTop(false)

        val prevUpgrade = upgrades.firstOrNull()

        resetUpgradeToggles()

        val temp = java.util.ArrayList<MinionData.Minion.Upgrade>()
        //        If the selected upgrade is not equal to null, add it
        if (selectedUpgrade != null) {
            temp.add(selectedUpgrade)
        }
        if (prevUpgrade != null) {
            temp.add(prevUpgrade)
        }

        //        Creates a new upgrade array with the right size
        upgrades = listOf()

        //        Adds all selected upgrades to the upgrade array
        for (i in temp.indices) {
            upgrades += temp[i]
        }

        //        Resets all the toggles
        resetUpgradeToggles()

        //        Enables the selected toggles
        for (up in upgrades) {
            upgradeToggleMap[up]!!.setState(true)
        }

        //        Refreshes minion data
        minionTexts = updateMinionData()
    }

    fun resetFuelToggles() {
        for (toggle in fuelToggles.values) {
            toggle.setState(false)
        }
    }

    fun resetUpgradeToggles() {
        for (toggle in upgradeToggleMap.values) {
            toggle.setState(false)
        }
    }

    fun resetFuels() {
        selectedFuel = null
        resetFuelToggles()
    }

    fun resetUpgrades() {
        upgrades = listOf()
        resetUpgradeToggles()
    }

    fun changeFuel(fuelId: String?, state: Boolean) {
        mainTextScrollComponent.scrollToTop(false)
        resetFuelToggles()
        val toggle = fuelToggles[fuelId]
        selectedFuel = null
        toggle!!.setState(state)
        if (state) {
            selectedFuel = MinionData.fuelMap[fuelId]
        }

        minionTexts = updateMinionData()
    }

    fun updateMinionData(): MutableList<UIComponent> {
        for (minionText in minionTexts) {
            minionText.clearChildren()
            minionText.parent.removeChild(minionText)
        }
        minionTexts.clear()

        return addMinionBreakdownText(selectedCategory)
    }

    private fun fromWidthScaleFactor(pos: Float): PixelConstraint {
        return PixelConstraint((pos * (window.getWidth() / 1000.0)).toFloat())
    }

    fun getBestMinionSettings() {
        val bestProfit = Int.MIN_VALUE.toDouble()

        var availableSlots = 2 - upgradeSlotsUnavailable
        if (availableSlots < 0) {
            availableSlots = 0
        }


        var bestUpgrades = listOf<MinionData.Minion.Upgrade>()
        var bestMinionFuel: MinionData.MinionFuel? = null

        var possibleCombos: Long = 0
        for (fuel in MinionData.fuelMap.values) { // Best fuel
            for (upgrade1 in MinionData.Minion.Upgrade.entries) { // Best upgrade 1
                for (upgrade2 in MinionData.Minion.Upgrade.entries) { // Best Upgrade 2;
                    possibleCombos++
                    if (upgrade1 == upgrade2) {
                        continue
                    }
                    if (upgrade1 == MinionData.Minion.Upgrade.LESSER_SOULFLOW_ENGINE && upgrade2 == MinionData.Minion.Upgrade.SOULFLOW_ENGINE) {
                        continue
                    }
                    if (upgrade2 == MinionData.Minion.Upgrade.LESSER_SOULFLOW_ENGINE && upgrade1 == MinionData.Minion.Upgrade.SOULFLOW_ENGINE) {
                        continue
                    }

                    var testUpgrades: List<MinionData.Minion.Upgrade> = listOf()
                    when (availableSlots) {
                        0 -> testUpgrades = listOf()
                        1 -> testUpgrades = listOf(upgrade1)
                        2 -> testUpgrades = listOf(upgrade1, upgrade2)
                    }
                    val bestMinionsMap = MinionData.getMostProfitMinion(testUpgrades, fuel)

                    //                        Gets the price of the current test
                    var testMinionPrice = Int.MIN_VALUE.toDouble()
                    for (`val` in bestMinionsMap.values) {
                        if (`val` > testMinionPrice) {
                            testMinionPrice = `val`
                        }
                    }

                    if (testMinionPrice > bestProfit) {
                        bestUpgrades = testUpgrades
                        bestMinionFuel = fuel
                    }

                    println(testUpgrades)
                }
            }
        }

        resetFuels()
        if (bestMinionFuel != null) {
            changeFuel(bestMinionFuel.id, true)
        }

        resetUpgrades()
        for (upgrade in bestUpgrades) {
            changeUpgrade(upgrade)
        }
    }
}
