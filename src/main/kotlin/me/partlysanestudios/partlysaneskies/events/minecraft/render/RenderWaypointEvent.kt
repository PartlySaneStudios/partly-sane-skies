//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.events.minecraft.render

import me.partlysanestudios.partlysaneskies.api.WaypointRenderPipeline
import me.partlysanestudios.partlysaneskies.api.events.PSSEvent

class RenderWaypointEvent(val pipeline: WaypointRenderPipeline, private val partialTicks: Float) : PSSEvent() {
    override fun post(): Boolean {
        super.post()
        pipeline.renderAll(partialTicks)
        return isCancelled
    }
}

