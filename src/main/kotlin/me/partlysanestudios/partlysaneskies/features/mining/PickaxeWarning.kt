//
// Written by J10a1n15 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getCurrentlyHoldingItem
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getLore
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.isArrOfStringsInLore
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Pattern

object PickaxeWarning {
    private val pattern = Pattern.compile("(Mining Speed Boost|Pickobulus|Maniac Miner|Vein Seeker) is now available!")
    private val pickaxeAbilities =
        arrayOf<String?>(
            "Mining Speed Boost",
            "Pickobulus",
            "Maniac Miner",
            "Vein Seeker",
            "Hazardous Miner",
            "Gemstone Infusion",
        )

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onChat(event: ClientChatReceivedEvent) {
        if (config.onlyGiveWarningOnMiningIsland) {
            if (!IslandType.DWARVEN_MINES.onIsland() && !IslandType.CRYSTAL_HOLLOWS.onIsland()) {
                return
            }
        }
        val message = event.message.formattedText.removeColorCodes()
        val matcher = pattern.matcher(message)
        if (matcher.find()) {
            if (config.pickaxeAbilityReadyBanner) {
                renderNewBanner(
                    PSSBanner(
                        config.pickaxeAbilityReadyBannerText,
                        (config.pickaxeBannerTime * 1000).toLong(),
                        4.0f,
                        config.pickaxeBannerColor.toJavaColor(),
                    ),
                )
            }
            if (config.pickaxeAbilityReadySound) {
                if (config.pickaxeAbilityReadySiren) {
                    minecraft.thePlayer.playSound("partlysaneskies:airraidsiren", 100f, 1f)
                } else {
                    minecraft.thePlayer.playSound("partlysaneskies:bell", 100f, 1f)
                }
            }
            if (config.hideReadyMessageFromChat) {
                event.setCanceled(true)
            }
        }
    }

    @SubscribeEvent
    fun onClick(event: PlayerInteractEvent) {
        if (!config.blockAbilityOnPrivateIsland) {
            return
        }
        if (!IslandType.GARDEN.onIsland() && !IslandType.PRIVATE_ISLAND.onIsland()) {
            return
        }
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            if (getCurrentlyHoldingItem() == null) return
            val loreOfItemInHand = getCurrentlyHoldingItem()!!.getLore().toTypedArray<String>()
            if (isArrOfStringsInLore(pickaxeAbilities, loreOfItemInHand)) {
                event.setCanceled(true)
            }
        }
    }
}
