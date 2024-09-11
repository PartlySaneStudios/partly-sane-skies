package me.partlysanestudios.partlysaneskies.features.economy.minioncalculator

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.dsl.pixels
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
        val categories = mapOf(
            "ALL" to "§b",
            "FORAGING" to "§6",
            "MINING" to "§7",
            "FISHING" to "§9",
            "FARMING" to "§a",
            "COMBAT" to "§c",
        )

        private const val HOURS = 24.0

        private val uselessUpgrades = listOf("Auto-Smelter", "Compactor", "Super Compactor", "Dwarven Super Compactor", "Enchanted Shears")

        fun registerCommand() {
            PSSCommand("minioncalculator")
                .addAlias("minioncalc", "bestminion", "mc")
                .setDescription("Opens the Profit Minion Calculator")
                .setRunnable {
                    ChatUtils.sendClientMessage("§bOpening Minion Calculator...")
                    MinecraftUtils.displayGuiScreen(ProfitMinionCalculator(ElementaVersion.V5))
                }.register()
        }

        private val soulflowUpgrades = setOf(MinionUpgrade.LESSER_SOULFLOW_ENGINE, MinionUpgrade.SOULFLOW_ENGINE)
    }

    private var upgradeSlotsUnavailable = 0
    private var selectedFuel: MinionFuel? = null
    private var upgrades = listOf<MinionUpgrade>()
    private var selectedCategory = "ALL"
    private lateinit var backgroundBox: UIComponent
    private lateinit var mainTextScrollComponent: ScrollComponent
    private lateinit var backgroundImage: UIComponent
    private lateinit var leftBar: UIComponent
    private lateinit var rightBar: UIComponent
    private lateinit var categoriesBar: UIComponent
    private lateinit var bestMinionBar: UIComponent
    private var fuelToggles = mapOf<String, PSSToggle>()
    private var minionTexts = listOf<UIComponent>()
    private var upgradeToggleMap = mapOf<MinionUpgrade, PSSToggle>()

    init {
        setUpBackground()
        minionTexts = addMinionBreakdownText("ALL")
        fuelToggles = addFuelButtons()
        upgradeToggleMap = addMinionUpgradeButtons()
        addCategories()
        addBestMinionCalculator()
    }

    private fun setUpBackground() {
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
            .setHeight(backgroundBox.getHeight().pixels)
            .setWidth(backgroundBox.getWidth().pixels)
            .setChildOf(backgroundBox)

        mainTextScrollComponent = (ScrollComponent()
            .setY(CenterConstraint())
            .setX(CenterConstraint())
            .setWidth(backgroundBox.getWidth().pixels)
            .setHeight(backgroundBox.getHeight().pixels)
            .setColor(Color.red)
            .setChildOf(backgroundImage) as ScrollComponent)

        leftBar = UIBlock()
            .setX((backgroundImage.getWidth() * (1 / 5f)).pixels)
            .setY(CenterConstraint())
            .setHeight((backgroundImage.getHeight() * .85f).pixels)
            .setWidth(fromWidthScaleFactor(2f))
            .setColor(accentColor.toJavaColor())
            .setChildOf(backgroundImage)

        rightBar = UIBlock()
            .setX((backgroundImage.getWidth() * (4 / 5f)).pixels)
            .setY(CenterConstraint())
            .setHeight((backgroundImage.getHeight() * .85f).pixels)
            .setWidth(fromWidthScaleFactor(2f))
            .setColor(accentColor.toJavaColor())
            .setChildOf(backgroundImage)

        val categoriesBarHeight = fromWidthScaleFactor(75f).value
        val categoriesBarPad = fromWidthScaleFactor(5f).value
        categoriesBar = currentBackgroundUIImage
            .setX(CenterConstraint())
            .setY((backgroundBox.getTop() - (categoriesBarHeight + categoriesBarPad)).pixels)
            .setWidth((backgroundBox.getWidth()).pixels)
            .setHeight(categoriesBarHeight.pixels)
            .setChildOf(window)

        bestMinionBar = currentBackgroundUIImage
            .setX(CenterConstraint())
            .setY((backgroundBox.getBottom() + categoriesBarPad).pixels)
            .setWidth((backgroundBox.getWidth() / 3f).pixels)
            .setHeight(categoriesBarHeight.pixels)
            .setChildOf(window)
    }

    private fun addMinionBreakdownText(category: String) = buildList {
        selectedCategory = category
        val mostProfitableMinions = MinionData.getBestMinions(upgrades, selectedFuel)

        var yPos = fromWidthScaleFactor(10f).value
        val barOffset = fromWidthScaleFactor(10f).value
        val barNegation = fromWidthScaleFactor(66f).value

        for ((index, pair) in mostProfitableMinions.withIndex()) {
            val minion = pair.first
            if (category != "ALL" && minion.category != category) continue

            val string = "§7${index+1}. ${minion.costBreakdown(minion.maxTier, HOURS, upgrades, selectedFuel)}"
            val text = UIWrappedText(string)
                .setText(string)
                .setX((leftBar.getRight() + fromWidthScaleFactor(7f).value).pixels)
                .setY(yPos.pixels)
                .setWidth((rightBar.getLeft() - leftBar.getRight() - fromWidthScaleFactor(14f).value).pixels)
                .setTextScale(fromWidthScaleFactor(1f))
                .setColor(Color.WHITE)
                .setTextScale(fromWidthScaleFactor(1f))
                .setChildOf(mainTextScrollComponent)

            val border = UIBlock()
                .setX((leftBar.getRight() - text.getLeft() + barNegation).pixels)
                .setY((text.getHeight() + barOffset).pixels)
                .setWidth((rightBar.getLeft() - leftBar.getRight() - 2 * barNegation).pixels)
                .setHeight(fromWidthScaleFactor(1f))
                .setColor(accentColor.toJavaColor())
                .setChildOf(text)

            add(text)

            yPos = border.getBottom() + barOffset - mainTextScrollComponent.getTop()
        }
    }

    private fun addFuelButtons() = buildMap {
        var yPos = fromWidthScaleFactor(5f).value
        val textPad = fromWidthScaleFactor(5f).value
        val buttonPad = fromWidthScaleFactor(7f).value

        for (value in MinionData.fuelMap.values) {
            val fuelId = value.id
            val fuelItem = getItem(fuelId) ?: continue

            val fuelContainer = PSSButton()
                .setX(fromWidthScaleFactor(10f))
                .setY(yPos.pixels)
                .setWidth((leftBar.getLeft() - mainTextScrollComponent.getLeft() - fromWidthScaleFactor(20f).value).pixels)
                .setHeight(fromWidthScaleFactor(40f))
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
                .setX(textXPos.pixels)
                .setY(CenterConstraint())
                .setWidth((backgroundBox.getWidth() - textXPos).pixels)
                .setColor(Color.white)
                .setTextScale(fromWidthScaleFactor(1f))
                .setChildOf(fuelContainer.component) as UIWrappedText

            fuelContainer.onMouseClickConsumer {
                toggle.toggleState()
                changeFuel(fuelId, toggle.getState())
            }
            put(fuelId, toggle)

            yPos = fuelContainer.component.getBottom() + buttonPad - mainTextScrollComponent.getTop()
        }
    }

    private fun addMinionUpgradeButtons(): Map<MinionUpgrade, PSSToggle> {
        val components = mutableMapOf<MinionUpgrade, PSSToggle>()

        var yPos = fromWidthScaleFactor(5f).value
        val textPad = fromWidthScaleFactor(5f).value
        val buttonPad = fromWidthScaleFactor(7f).value

        for (upgrade in MinionUpgrade.entries) {
            val upgradeId = upgrade.toString()

            val upgradeItem = getItem(upgradeId) ?: continue
            val upgradeContainer = PSSButton()
                .setX((rightBar.getRight() - rightBar.getWidth() - mainTextScrollComponent.getLeft() + fromWidthScaleFactor(10f,).value).pixels)
                .setY(yPos.pixels)
                .setWidth((mainTextScrollComponent.getRight() - rightBar.getRight() - rightBar.getWidth() - fromWidthScaleFactor(20f).value).pixels)
                .setHeight(fromWidthScaleFactor(40f))
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
                .setX(textXPos.pixels)
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

    private fun addCategories() {
        val pad = fromWidthScaleFactor(10f).value
        val blockWidth = (categoriesBar.getWidth() - (categories.size + 1) * pad) / categories.size

        var xPos = pad
        for ((category, color) in categories) {
            val button = PSSButton()
                .setX(xPos.pixels)
                .setY(CenterConstraint())
                .setWidth(blockWidth.pixels)
                .setHeight((categoriesBar.getHeight() * 0.9f).pixels)
                .setChildOf(categoriesBar)

            button.setText(color + category.titleCase())

            button.onMouseClickConsumer {
                selectedCategory = category
                minionTexts = updateMinionData()
            }

            xPos += blockWidth + pad
        }
    }

    private fun addBestMinionCalculator() {
        var heightPos = 0f
        val yPad = fromWidthScaleFactor(6f).value

        for (upgrade in uselessUpgrades) {
            heightPos += yPad
            val toggle = PSSToggle()
                .setX(fromWidthScaleFactor(10f))
                .setY(heightPos.pixels)
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
            .setHeight(fromWidthScaleFactor(60f))
            .setWidth(fromWidthScaleFactor(100f))
            .setText("Calculate Best Minion")
            .setTextScale(fromWidthScaleFactor(1f))

            .setChildOf(bestMinionBar)

        button.onMouseClickConsumer {
            getBestMinionSettings()
        }
    }

    private fun changeUpgrade(selectedUpgrade: MinionUpgrade?) {
        mainTextScrollComponent.scrollToTop(false)

        val prevUpgrade = upgrades.firstOrNull()
        upgrades = listOfNotNull(
            selectedUpgrade,
            prevUpgrade
        )
        resetUpgradeToggles()

        upgrades.forEach { upgradeToggleMap[it]?.setState(true) }
    }

    private fun resetFuelToggles() = fuelToggles.values.forEach { it.setState(false) }

    private fun resetUpgradeToggles() = upgradeToggleMap.values.forEach { it.setState(false) }

    private fun resetFuels() {
        selectedFuel = null
        resetFuelToggles()
    }

    private fun resetUpgrades() {
        upgrades = listOf()
        resetUpgradeToggles()
    }

    private fun changeFuel(fuelId: String?, state: Boolean) {
        mainTextScrollComponent.scrollToTop(false)
        resetFuelToggles()
        val toggle = fuelToggles[fuelId]
        selectedFuel = null
        toggle?.setState(state)
        if (state) {
            selectedFuel = MinionData.fuelMap[fuelId]
        }

        minionTexts = updateMinionData()
    }

    private fun updateMinionData(): List<UIComponent> {
        for (minionText in minionTexts) {
            minionText.clearChildren()
            minionText.parent.removeChild(minionText)
        }
        minionTexts = listOf()

        return addMinionBreakdownText(selectedCategory)
    }

    private fun fromWidthScaleFactor(pos: Float): PixelConstraint = (pos * (window.getWidth() / 1000.0)).pixels

    private fun getBestMinionSettings() {
        val availableSlots = (2 - upgradeSlotsUnavailable).coerceAtLeast(0)

        var bestUpgrades = listOf<MinionUpgrade>()
        var bestMinionFuel: MinionFuel? = null
        var bestProfit = Double.NEGATIVE_INFINITY

        for (fuel in MinionData.fuelMap.values) {
            for (upgrades in getUpgradeCombinations(availableSlots)) {
                val profit = MinionData.getBestMinions(upgrades, fuel).maxByOrNull { it.second }?.second ?: continue
                if (profit > bestProfit) {
                    bestProfit = profit
                    bestUpgrades = upgrades
                    bestMinionFuel = fuel
                }
            }
        }

        resetFuels()
        bestMinionFuel?.let { changeFuel(it.id, true) }

        resetUpgrades()
        bestUpgrades.forEach { changeUpgrade(it) }
    }

    private fun getUpgradeCombinations(availableSlots: Int): List<List<MinionUpgrade>> {
        val upgrades = MinionUpgrade.entries
        return when (availableSlots) {
            1 -> upgrades.map { listOf(it) }
            2 -> upgrades.flatMap { first ->
                upgrades.filter { second ->
                    first != second && !(first in soulflowUpgrades && second in soulflowUpgrades)
                }.map { listOf(first, it) }
            }
            else -> listOf()
        }
    }
}
