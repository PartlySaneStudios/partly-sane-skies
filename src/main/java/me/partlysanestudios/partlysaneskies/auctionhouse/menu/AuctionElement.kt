//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.auctionhouse.menu

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.constraints.XConstraint
import gg.essential.elementa.constraints.YConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import me.partlysanestudios.partlysaneskies.system.guicomponents.PSSButton
import me.partlysanestudios.partlysaneskies.system.guicomponents.PSSItemRender
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.Utils
import net.minecraft.item.ItemStack
import java.awt.Color
import java.util.*

class AuctionElement(private val slot: Int, val itemstack: ItemStack?, var xConstraint: XConstraint, var yConstraint: YConstraint, var heightConstraint: PixelConstraint, val textScale: Float) {

    val skyblockItem = SkyblockDataManager.getItem(Utils.getItemId(itemstack))

    private val boundingBox = UIBlock().constrain {
        x = xConstraint
        y = yConstraint
        width = (heightConstraint.value).pixels
        height = (heightConstraint.value).pixels
        color = Color(0, 0, 0, 0).constraint
    }

    private val box: PSSButton = PSSButton()
        .setX(CenterConstraint())
        .setY(CenterConstraint())
        .setWidth(heightConstraint.value)
        .setHeight(heightConstraint.value)
        .setColor(getRarityColor())
        .setChildOf(boundingBox)
        .onMouseClickConsumer {
            clickAuction()
        }


    private val itemHeight = (heightConstraint.value * .667f)

    private val itemRender: PSSItemRender = PSSItemRender(itemstack)
        .setItemScale((heightConstraint.value / 16).pixels)
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

    fun loadAuctionInformationBar(informationBar: MarketInformationBar) {
        val auction = this
        boundingBox.onMouseEnter {
            informationBar.loadAuction(auction)
        }
        boundingBox.onMouseLeave {
            informationBar.clearInfo()
        }
    }
    fun loadItemInformationBar(informationBar: ItemInformationBar){
        val auction = this
        boundingBox.onMouseEnter {
            informationBar.loadAuction(auction)
        }
        boundingBox.onMouseLeave {
            informationBar.clearInfo()
        }

    }

    fun clickAuction() {
        Utils.clickOnSlot(slot)
    }

    fun isBin(): Boolean {
        val loreList: List<String> = Utils.getLore(itemstack)
        for (line in loreList) {
            if (StringUtils.removeColorCodes(line).contains("Buy it now: ")) {
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
        val loreList: List<String> = Utils.getLore(itemstack)
        var buyItNowPrice = ""
        for (line in loreList) {
            if (StringUtils.removeColorCodes(line).contains("Buy it now:")
                || StringUtils.removeColorCodes(line).contains("Top bid:")
                || StringUtils.removeColorCodes(line).contains("Starting bid:")
            ) {
                buyItNowPrice = StringUtils.removeColorCodes(line).replace("[^0-9]".toRegex(), "")
            }
        }
        return buyItNowPrice.toLong()
    }

    fun getCostPerAmount(): Double {
        return getPrice() / getAmount().toDouble()
    }

    private fun isCheapBin(): Boolean {
        val sellingPrice = getPrice()
        if (SkyblockDataManager.getItem(skyblockItem.id) == null) {
            return false
        }
        if (!SkyblockDataManager.getItem(skyblockItem.id).hasSellPrice()) {
            return false
        }
        val averageAhPrice = SkyblockDataManager.getItem(skyblockItem.id).getSellPrice()
        return sellingPrice <= averageAhPrice * (PartlySaneSkies.config.BINSniperPercent / 100.0)
    }

    fun getName(): String {
        return itemstack?.displayName ?: ""
    }

    fun getAverageLowestBin(): Double {
        if (SkyblockDataManager.getItem(skyblockItem.id) == null) {
            return 0.0
        }
        return if (!SkyblockDataManager.getItem(skyblockItem.id).hasAverageLowestBin()) {
            0.0
        } else SkyblockDataManager.getItem(skyblockItem.id).getAverageLowestBin()
    }

    fun getLowestBin(): Double {
        try {
            if (!SkyblockDataManager.getItem(skyblockItem.id).hasSellPrice()) {
                return 0.0
            }
        } catch (exception: NullPointerException) {
            exception.printStackTrace()
            return 0.0
        }
        return SkyblockDataManager.getItem(skyblockItem.id).getSellPrice()
    }

    fun hasLowestBin(): Boolean {
        return if (SkyblockDataManager.getItem(skyblockItem.id) == null) {
            false
        } else SkyblockDataManager.getItem(skyblockItem.id).hasSellPrice()
    }

    fun hasAverageLowestBin(): Boolean {
        return if (SkyblockDataManager.getItem(skyblockItem.id) == null) {
            false
        } else SkyblockDataManager.getItem(skyblockItem.id).hasAverageLowestBin()
    }

    fun getFormattedEndingTime(): String {
        if (itemstack == null) {
            return ""
        }
        val loreList: List<String> = Utils.getLore(itemstack)
        for (loreLine in loreList) {
            if (StringUtils.removeColorCodes(loreLine).contains("Ends in:")) {
                return StringUtils.removeColorCodes(loreLine).replace("Ends in: ", "")
            }
            if (StringUtils.removeColorCodes(loreLine).contains("Ending Soon")) {
                return StringUtils.removeColorCodes(loreLine)
            }
        }
        return ""
    }

    fun getLore(): String {
        if (itemstack == null) {
            return ""
        }
        return Utils.getLoreAsString(this.itemstack)
    }

    fun getRarity(): String {
        var str = ""
        if (itemstack == null) {
            return ""
        }
        val lastLineOfLore = try {
            val loreList = Utils.getLore(itemstack)
            if (loreList.size - 7 - 1 < 0) {
                return ""
            }
            StringUtils.removeColorCodes(loreList[loreList.size - 7 - 1])
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

    fun getRarityColor(): Color {
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
            else -> ThemeManager.getPrimaryColor().toJavaColor()
        }
    }

    fun setX(xConstraint: XConstraint): AuctionElement  {
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
        val boxHeight = heightConstraint.value * 2/3.0f
        val boxY = 0
        box.setWidth(boxHeight).setHeight(boxHeight)
        box.setY(boxY.pixels)


//        val itemX = boxHeight / 2

        itemRender.setItemScale((boxHeight / 16).pixels).setWidth(boxHeight.pixels).setHeight(boxHeight.pixels)
        nameComponent.setY((boxHeight + 0.05 * heightConstraint.value).pixels)
            .setHeight((heightConstraint.value * .667).pixels)
            .setWidth((heightConstraint.value * 1.25).pixels)


        this.heightConstraint = heightConstraint

        return this
    }

    fun setChildOf(parent: UIComponent): AuctionElement {
        boundingBox.setChildOf(parent)
        return this
    }

}