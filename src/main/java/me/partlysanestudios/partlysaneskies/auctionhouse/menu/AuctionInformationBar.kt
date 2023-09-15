package me.partlysanestudios.partlysaneskies.auctionhouse.menu

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.Utils
import java.awt.Color

class AuctionInformationBar(xConstraint: XConstraint, yConstraint: YConstraint, heightConstraint: HeightConstraint, widthConstraint: WidthConstraint, textScaleFactor: Float) {
    private val baseBlock: UIBlock = UIBlock().constrain {
        color = Color(0, 0, 0, 0).constraint
        x = xConstraint
        y = yConstraint
        height = heightConstraint
        width = widthConstraint
    }

    private val backgroundImage: UIImage = ThemeManager.getCurrentBackgroundUIImage().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = heightConstraint
        width = widthConstraint
    } childOf baseBlock

    private var headerString = ""

    private var descriptionString = ""

    private val header: UIWrappedText = UIWrappedText(centered = true) constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = heightConstraint
        width = widthConstraint
        textScale = (1.25f * textScaleFactor).pixels
    } childOf backgroundImage

    private val description: UIWrappedText = UIWrappedText(centered = true) constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = heightConstraint
        width = widthConstraint
    } childOf backgroundImage

    init {
        description.setText(descriptionString)
    }

    fun setChildOf(component: UIComponent) {
        baseBlock childOf component
    }

    fun loadAuction(auction: Auction) {
        headerString = if (auction.isBin()) {
            "Buy It Now Details:"
        } else {
            "Auction Details:"
        }

        var info = ""
        info += "&6Offer Information:\n\n\n"
        info += "&eSelling Price: &6${StringUtils.formatNumber(auction.getPrice().toDouble())}"
        info += "&eEnding In: &6${auction.getFormattedEndingTime()}"

        if (auction.getAmount() != 1) {
            info += "\n\n\n"
            info += "&eQuantity: &6${StringUtils.formatNumber(auction.getAmount().toDouble())}"
            info += "&eCost Per Item: &6${StringUtils.formatNumber(Utils.round(auction.getCostPerAmount(),2))} coins"
        }
        info += "\n\n\n\n\n\n"

        info += "&eMarket Stats:\n\n\n"
        info += if (auction.hasLowestBin()) {
            "&bCurrent Lowest Bin: &e${StringUtils.formatNumber(Utils.round(auction.getLowestBin(), 2))}"
        } else {
            "&bCurrent Lowest Bin: \n&8&o(Unknown)\n\n"
        }

        info += if (auction.hasAverageLowestBin()) {
            "&bAverage Lowest Bin (Last Day): &e${StringUtils.formatNumber(Utils.round(auction.getAverageLowestBin(), 2))}"
        } else {
            "&bAverage Lowest Bin (Last Day): \n&8&o(Unknown)\n\n"
        }

        info += if (auction.hasLowestBin() && auction.hasAverageLowestBin()) {
            "&bItem Inflation: \n&e${StringUtils.formatNumber(Utils.round(auction.getLowestBin() / auction.getAverageLowestBin() * 100.0, 2) - 100)}%\n\n"
        } else {
            "&bInflation: \n&8&o(Unknown)\n\n"
        }

        info += if (auction.hasLowestBin()) {
            "&bItem Mark up: \n&e${StringUtils.formatNumber(Utils.round(auction.getPrice() / auction.getLowestBin() / auction.getAmount() * 100 - 100, 2))}%\n"
        } else {
            "&bItem Mark up: \n&8&o(Unknown)\n"
        }

        descriptionString = info
    }

    fun clearInfo() {
        headerString = ""
        descriptionString = ""
    }

    fun update() {
        if (description.getText() != descriptionString) {
            description.setText(descriptionString)
        }

        if (header.getText() != headerString) {
            header.setText(headerString)
        }
    }

}