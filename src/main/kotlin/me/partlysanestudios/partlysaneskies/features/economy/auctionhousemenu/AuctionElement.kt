//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.economy.auctionhousemenu

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.constraints.XConstraint
import gg.essential.elementa.constraints.YConstraint
import gg.essential.elementa.dsl.*
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSButton
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSItemRender
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getItemId
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getLore
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.item.ItemStack
import java.awt.Color
import java.util.*

class AuctionElement(
    private val slot: Int,
    val itemstack: ItemStack?,
    private var xConstraint: XConstraint,
    private var yConstraint: YConstraint,
    private var heightConstraint: PixelConstraint,
    val textScale: Float
) {

    private val skyblockItem = SkyblockDataManager.getItem(itemstack?.getItemId() ?: "")

    private val boundingBox = UIBlock().constrain {
        x = xConstraint
        y = yConstraint
        width = (heightConstraint.value).pixels
        height = (heightConstraint.value).pixels
        color = Color(0, 0, 0, 0).constraint
    }

    private val highlightBox = UIRoundedRectangle(7.5f).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = (heightConstraint * 1.2)
        height = (heightConstraint * 1.2)
        color = Color(0, 0, 0, 0).constraint
    } childOf boundingBox

    private val box: PSSButton = PSSButton()
        .setX(CenterConstraint())
        .setY(CenterConstraint())
        .setWidth(heightConstraint)
        .setHeight(heightConstraint)
        .setColor(getRarityColor())
        .setChildOf(boundingBox)
        .onMouseClickConsumer {
            clickAuction()
        }


    private val itemHeight = (heightConstraint.value * .667f)

    private val itemRender: PSSItemRender = PSSItemRender(
        itemstack
    )
        .setItemScale(heightConstraint / 16)
        .setX(CenterConstraint())
        .setY(CenterConstraint())
        .setWidth(itemHeight.pixels)
        .setHeight(itemHeight.pixels)
        .setChildOf(box.component) as PSSItemRender


    private val nameComponent = UIWrappedText(getName(), centered = true).constrain {
        x = CenterConstraint()
        y = itemHeight.pixels
        width = (heightConstraint.value * 1.25).pixels
        height = (heightConstraint.value * .667).pixels
    }.setTextScale(textScale.pixels) childOf boundingBox

    init {

        boundingBox.onMouseClick {
            clickAuction()
        }


    }

    fun highlightIfCheapBin() {
//        ChatUtils.sendClientMessage("checking")
        if (!isCheapBin()) {
            return
        }
//        ChatUtils.sendClientMessage("checked")
        highlightBox.setColor(ThemeManager.accentColor.toJavaColor().constraint)
    }

    fun loadAuctionInformationBar(informationBar: MarketInformationBar) {
        val auction = this
        boundingBox.onMouseEnter {
            informationBar.loadAuction(auction)
        }
        boundingBox.onMouseLeave {
            informationBar.clearInfo()
        }
    }

    fun loadItemInformationBar(informationBar: ItemInformationBar) {
        val auction = this
        boundingBox.onMouseEnter {
            informationBar.loadAuction(auction)
        }
        boundingBox.onMouseLeave {
            informationBar.clearInfo()
        }

    }

    private fun clickAuction() {
        MinecraftUtils.clickOnSlot(slot)
    }

    fun isBin(): Boolean {
        if (itemstack == null) {
            return false
        }
        val loreList: List<String> = itemstack.getLore()
        for (line in loreList) {

            if (line.removeColorCodes().contains("Buy it now: ")) {
                return true
            }
        }
        return false
    }

    fun getAmount(): Int {
        if (itemstack == null) {
            return 1
        }

        return itemstack.stackSize
    }

    fun getPrice(): Long {
        if (itemstack == null) {
            return -1
        }
        val loreList: List<String> = itemstack.getLore()
        var buyItNowPrice = ""
        for (line in loreList) {
            if (line.removeColorCodes().contains("Buy it now:")
                || line.removeColorCodes().contains("Top bid:")
                || line.removeColorCodes().contains("Starting bid:")
            ) {
                buyItNowPrice = line.removeColorCodes().replace("[^0-9]".toRegex(), "")
            }
        }
        if (buyItNowPrice.isEmpty()) {
            return -1
        }
        return buyItNowPrice.toLong()
    }

    fun getCostPerAmount(): Double {
        return getPrice() / getAmount().toDouble()
    }

    private fun isCheapBin(): Boolean {
        val sellingPrice = getPrice()
        if (SkyblockDataManager.getItem(skyblockItem?.id ?: "") == null) {
            return false
        }
        if (SkyblockDataManager.getItem(skyblockItem?.id ?: "")?.hasSellPrice() != true) {
            return false
        }
        val averageAhPrice = SkyblockDataManager.getItem(skyblockItem?.id ?: "")?.getSellPrice() ?: 0.0
        return sellingPrice <= averageAhPrice * (PartlySaneSkies.config.BINSniperPercent / 100.0)
    }

    fun getName(): String {
        return itemstack?.displayName ?: ""
    }

    fun getAverageLowestBin(): Double {
        if (SkyblockDataManager.getItem(skyblockItem?.id ?: "") == null) {
            return 0.0
        }
        return if (SkyblockDataManager.getItem(skyblockItem?.id ?: "")?.hasAverageLowestBin() != true) {
            0.0
        } else SkyblockDataManager.getItem(skyblockItem?.id ?: "")?.averageLowestBin ?: 0.0
    }

    fun getLowestBin(): Double {
        try {
            if (SkyblockDataManager.getItem(skyblockItem?.id ?: "")?.hasSellPrice() != true) {
                return 0.0
            }
        } catch (exception: NullPointerException) {
            exception.printStackTrace()
            return 0.0
        }
        return SkyblockDataManager.getItem(skyblockItem?.id ?: "")?.getSellPrice() ?: 0.0
    }

    fun hasLowestBin(): Boolean {
        return if (SkyblockDataManager.getItem(skyblockItem?.id ?: "") == null) {
            false
        } else SkyblockDataManager.getItem(skyblockItem?.id ?: "")?.hasSellPrice() ?: false
    }

    fun hasAverageLowestBin(): Boolean {
        return if (SkyblockDataManager.getItem(skyblockItem?.id ?: "") == null) {
            false
        } else SkyblockDataManager.getItem(skyblockItem?.id ?: "")?.hasAverageLowestBin() ?: false
    }

    fun getFormattedEndingTime(): String {
        if (itemstack == null) {
            return ""
        }
        val loreList: List<String> = itemstack.getLore()
        for (loreLine in loreList) {
            if (loreLine.removeColorCodes().contains("Ends in:")) {
                return loreLine.removeColorCodes().replace("Ends in: ", "")
            }
            if (loreLine.removeColorCodes().contains("Ending Soon")) {
                return loreLine.removeColorCodes()
            }
        }
        return ""
    }

    fun getLore(): String {
        if (itemstack == null) {
            return ""
        }
        return MinecraftUtils.getLoreAsString(this.itemstack)
    }

    private fun getRarity(): String {
        var str = ""
        if (itemstack == null) {
            return ""
        }
        val lastLineOfLore = try {
            val loreList = itemstack.getLore()
            if (loreList.size - 7 - 1 < 0) {
                return ""
            }
            loreList[loreList.size - 7 - 1].removeColorCodes()
        } catch (exception: NullPointerException) {
            exception.printStackTrace()
            return ""
        }

        if (lastLineOfLore.uppercase(Locale.getDefault()).contains("UNCOMMON")) {
            str = "UNCOMMON"
        } else if (lastLineOfLore.uppercase(Locale.getDefault()).contains("COMMON")) {
            str = "COMMON"
        } else if (lastLineOfLore.uppercase(Locale.getDefault()).contains("RARE")) {
            str = "RARE"
        } else if (lastLineOfLore.uppercase(Locale.getDefault()).contains("EPIC")) {
            str = "EPIC"
        } else if (lastLineOfLore.uppercase(Locale.getDefault()).contains("LEGENDARY")) {
            str = "LEGENDARY"
        } else if (lastLineOfLore.uppercase(Locale.getDefault()).contains("MYTHIC")) {
            str = "MYTHIC"
        } else if (lastLineOfLore.uppercase(Locale.getDefault()).contains("DIVINE")) {
            str = "DIVINE"
        } else if (lastLineOfLore.uppercase(Locale.getDefault()).contains("SPECIAL")) {
            str = "SPECIAL"
        }
        return str
    }

    private fun getRarityColor(): Color {
        val rarity = getRarity()
        return when (rarity) {
            "COMMON" -> Color(255, 255, 255)
            "UNCOMMON" -> Color(85, 255, 85)
            "RARE" -> Color(85, 85, 255)
            "EPIC" -> Color(170, 0, 170)
            "LEGENDARY" -> Color(255, 170, 0)
            "MYTHIC" -> Color(255, 85, 255)
            "DIVINE" -> Color(85, 255, 255)
            "SPECIAL" -> Color(255, 85, 85)
            else -> ThemeManager.primaryColor.toJavaColor()
        }
    }

    fun setX(xConstraint: XConstraint): AuctionElement {
        boundingBox.setX(xConstraint)
        this.xConstraint = xConstraint
        return this
    }

    fun setY(yConstraint: YConstraint): AuctionElement {
        boundingBox.setY(yConstraint)
        box.setY(0.pixels)
        this.yConstraint = yConstraint
        return this
    }

    fun setHeight(heightConstraint: PixelConstraint): AuctionElement {
        boundingBox.constrain {
            width = (heightConstraint.value).pixels
            height = (heightConstraint.value).pixels
        }

        this.heightConstraint = heightConstraint
        val boxHeight = heightConstraint * 2 / 3.0f
        val boxY = 0
        box.setWidth(boxHeight).setHeight(boxHeight)
        box.setY(boxY.pixels)


//        val itemX = boxHeight / 2

        itemRender.setItemScale((boxHeight / 16)).setWidth(boxHeight).setHeight(boxHeight)
        nameComponent.setY((boxHeight + heightConstraint * 0.05))
            .setHeight((heightConstraint.value * .667).pixels)
            .setWidth((heightConstraint.value * 1.25).pixels)

        highlightBox.setHeight((heightConstraint.value * 1.25).pixels)
            .setWidth((heightConstraint.value * 1.25).pixels)


        this.heightConstraint = heightConstraint

        return this
    }

    fun setChildOf(parent: UIComponent): AuctionElement {
        boundingBox.setChildOf(parent)
        return this
    }

}