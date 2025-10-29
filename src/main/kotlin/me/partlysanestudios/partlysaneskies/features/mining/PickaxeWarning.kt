//
// Written by J10a1n15 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.api.events.PSSEvent
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType.Companion.onIslands
import me.partlysanestudios.partlysaneskies.events.minecraft.PSSChatEvent
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getCurrentlyHoldingItem
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getLore
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.isListOfStringsInLore
import me.partlysanestudios.partlysaneskies.utils.StringUtils.getMatcher
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object PickaxeWarning {

    private val pattern = "(?<ability>.*) is now available!".toPattern()
    private val pickaxeAbilities = setOf(
        "Mining Speed Boost",
        "Pickobulus",
        "Maniac Miner",
        "Tunnel Vision",
        "Sheer Force",
        "Gemstone Infusion",
    )

    @PSSEvent.Subscribe
    fun onChat(event: PSSChatEvent) {
        if (config.onlyGiveWarningOnMiningIsland && !onIslands(IslandType.DWARVEN_MINES, IslandType.CRYSTAL_HOLLOWS)) return
        val message = event.component.unformattedText

        pattern.getMatcher(message) {
            if (group("ability") !in pickaxeAbilities) return@getMatcher

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
                minecraft.soundHandler.playSound(
                    PositionedSoundRecord.create(
                        ResourceLocation(
                            "partlysaneskies",
                            if (config.pickaxeAbilityReadySiren) {
                                "airraidsiren"
                            } else {
                                "bell"
                            },
                        ),
                    ),
                )
            }
            if (config.hideReadyMessageFromChat) event.cancel()
        }
    }

    @SubscribeEvent
    fun onClick(event: PlayerInteractEvent) {
        if (!config.blockAbilityOnPrivateIsland || !onIslands(IslandType.PRIVATE_ISLAND, IslandType.GARDEN)) return
        if (event.action !in listOf(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, PlayerInteractEvent.Action.RIGHT_CLICK_AIR)) return
        if (getCurrentlyHoldingItem() == null) return

        val loreOfItemInHand = getCurrentlyHoldingItem()!!.getLore().toTypedArray<String>()
        if (isListOfStringsInLore(pickaxeAbilities.toList(), loreOfItemInHand)) {
            event.setCanceled(true)
        }
    }
}
