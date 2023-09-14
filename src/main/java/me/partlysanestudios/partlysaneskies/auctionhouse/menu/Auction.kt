package me.partlysanestudios.partlysaneskies.auctionhouse.menu

import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.utils.Utils
import net.minecraft.item.ItemStack

class Auction(slot: Int, item: ItemStack) {
    val skyblockItem = SkyblockDataManager.getItem(Utils.getItemId(item))

    fun clickAuction() {
        Utils.clic
    }
}