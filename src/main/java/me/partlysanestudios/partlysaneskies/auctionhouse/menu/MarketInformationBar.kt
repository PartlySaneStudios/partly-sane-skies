//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


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
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber

import java.awt.Color

class MarketInformationBar(xConstraint: XConstraint, yConstraint: YConstraint, heightConstraint: HeightConstraint, widthConstraint: WidthConstraint, textScaleFactor: Float) {
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
        y = 10.pixels
        height = heightConstraint
        width = widthConstraint
        textScale = (1.5f * textScaleFactor).pixels
    } childOf backgroundImage

    private val description: UIWrappedText = UIWrappedText(centered = true) constrain {
        x = CenterConstraint()
        y = 40.pixels
        height = heightConstraint
        width = widthConstraint
        textScale = (1.0f * textScaleFactor).pixels
    } childOf backgroundImage

    init {
        description.setText(descriptionString)
    }

    fun setChildOf(component: UIComponent) {
        baseBlock childOf component
    }

    fun loadAuction(auction: AuctionElement) {
        headerString = if (auction.isBin()) {
            "Buy It Now Details:"
        } else {
            "Auction Details:\n"
        }

        var info = ""
        info += "&6Offer Information:\n\n\n"
        info += if (auction.getPrice() != -1L) {
            "&eSelling Price: &6${auction.getPrice().toDouble().formatNumber()}"
        } else {
            "\n&eSelling Price: \n&8&o(Unknown)\n\n"
        }

        info += if (auction.getFormattedEndingTime().isNotEmpty()) {
            "\n&eEnding In: &6${auction.getFormattedEndingTime()}"
        } else {
            "\n&eEnding In: \n&8&o(Unknown)\n\n"
        }

        if (auction.getAmount() > 1 ) {
            info += "\n\n\n"
            info += "\n&eQuantity: &6${auction.getAmount().toDouble().formatNumber()}"
            info += "\n&eCost Per Item: &6${auction.getCostPerAmount().round(2).formatNumber()} coins"
        }
        info += "\n\n\n\n\n\n"

        info += "&eMarket Stats:\n\n\n"
        info += if (auction.hasLowestBin()) {
            "\n&bCurrent Lowest Bin: &e${auction.getLowestBin().round( 2).formatNumber()}"
        } else {
            "\n&bCurrent Lowest Bin: \n&8&o(Unknown)\n\n"
        }

        info += if (auction.hasAverageLowestBin()) {
            "\n&bAverage Lowest Bin (Last Day): &e${auction.getAverageLowestBin().round(2).formatNumber()}"
        } else {
            "\n&bAverage Lowest Bin (Last Day): \n&8&o(Unknown)\n\n"
        }

        info += if (auction.hasLowestBin() && auction.hasAverageLowestBin()) {
            "\n&bItem Inflation: \n&e${((auction.getLowestBin() / auction.getAverageLowestBin() * 100.0).round(2) - 100).formatNumber()}%\n\n"
        } else {
            "\n&bInflation: \n&8&o(Unknown)\n\n"
        }

        info += if (auction.hasLowestBin()) {
            "\n&bItem Mark up: \n&e${(auction.getPrice() / auction.getLowestBin() / auction.getAmount() * 100 - 100).round(2).formatNumber()}%\n"
        } else {
            "\n&bItem Mark up: \n&8&o(Unknown)\n"
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