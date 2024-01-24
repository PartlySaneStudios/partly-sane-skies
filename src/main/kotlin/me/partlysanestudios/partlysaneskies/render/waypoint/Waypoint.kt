//
// Written by Su386 and j10a1n15.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.render.waypoint

import me.partlysanestudios.partlysaneskies.render.TextRenderer
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import net.minecraft.util.BlockPos
import java.awt.Color

class Waypoint(
    val name: String,
    val position: BlockPos,
    val outlineColor: Color = Color(255, 0, 0, 255),
    val fillColor: Color = Color(255, 127, 127, 127),
    val showBeam: Boolean = true,
    val showBlockHighlight: Boolean = true,
    val showLabel: Boolean = true,
    val showDistance: Boolean = true
) {

    /**
     * Render the waypoint
     *
     * @param distance the distance the waypoint should display in the distance label
     */
    internal fun render(distance: Double) {
        // Render waypoint beam
        if (this.showBeam) {
            BeamRenderer.renderBeam(this.position, this.outlineColor, this.fillColor)
        }

        // Render waypoint block highlight
        if (this.showBlockHighlight) {
            BlockHighlightRenderer.renderColoredBlockHighlight(this.position, this.outlineColor, this.fillColor)
        }

        // Render waypoint label
        if (this.showLabel) {
            TextRenderer.renderText3d(this.position.up().up(), this.name)
        }

        // Render waypoint distance
        if (this.showDistance) {
            val distanceText = "${distance.round(1)}m"
            TextRenderer.renderText3d(this.position.up(), distanceText)
        }
    }
}