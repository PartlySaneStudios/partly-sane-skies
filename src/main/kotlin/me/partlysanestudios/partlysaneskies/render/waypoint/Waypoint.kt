package me.partlysanestudios.partlysaneskies.render.waypoint

import me.partlysanestudios.partlysaneskies.render.BeamRenderer
import me.partlysanestudios.partlysaneskies.render.BlockHighlightRenderer
import me.partlysanestudios.partlysaneskies.render.TextRenderer
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
    // Additional properties and methods can be added based on your customization needs
    fun render(distance: Double) {
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
            val distanceText = "${distance.toInt()}m"
            TextRenderer.renderText3d(this.position.up(), distanceText)
        }
    }
}