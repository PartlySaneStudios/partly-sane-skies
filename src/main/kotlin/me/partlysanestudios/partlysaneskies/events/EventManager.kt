//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.events

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.api.WaypointRenderPipeline
import me.partlysanestudios.partlysaneskies.api.events.PSSEvent
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.minecraft.PSSChatEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.TablistUpdateEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.render.RenderWaypointEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonEndEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EventManager {

    private var oldTablist = emptyList<String>()
    private var lastDunegonStartEventSend: Long = 0
    private var lastDungeonEndEventSend: Long = 0

    fun tick() {
        val tablist = MinecraftUtils.getTabList()

        if (tablist != oldTablist) {
            TablistUpdateEvent(tablist).post()
            oldTablist = tablist
        }
    }

    @SubscribeEvent
    fun onScreenRender(event: RenderWorldLastEvent) {
        RenderWaypointEvent(WaypointRenderPipeline(), event.partialTicks).post()
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onChatReceivedEvent(event: ClientChatReceivedEvent) {
        if (event.type.toInt() != 0) return

        if (PSSChatEvent(event.message.formattedText, event.message).post()) {
            event.isCanceled = true
        }
    }

    @PSSEvent.Subscribe
    fun onChat(event: PSSChatEvent) {
        val message = event.message
        if (message.contains("Starting in 1 second.") && IslandType.CATACOMBS.onIsland() && lastDunegonStartEventSend + 3000 < time) {
            DungeonStartEvent().post()
            lastDunegonStartEventSend = time
        }
        if (message.contains("§r§c☠ §r§eDefeated §r") && lastDungeonEndEventSend + 3000 < time) {
            DungeonEndEvent().post()
            lastDungeonEndEventSend = time
        }
    }
}
