package me.partlysanestudios.partlysaneskies.features.farming

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.player.PlayerBreakBlockEvent
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils

object WrongToolCropWarning {
    @SubscribePSSEvent
    fun onBlockBreak(event: PlayerBreakBlockEvent) {
        sendClientMessage("Block: ${minecraft.theWorld.getBlockState(event.point.toBlockPos())?.block?.unlocalizedName}" ?: "")

    }
}