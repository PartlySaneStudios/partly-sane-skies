package me.partlysanestudios.partlysaneskies.render.waypoint

import net.minecraft.util.BlockPos
import java.awt.Color

class Waypoint(
    val name: String,
    val position: BlockPos,
    val outlineColor: Color = Color(255, 0, 0, 255),
    val fillColor: Color = Color(255, 127, 127, 127),
    val showBeam: Boolean = false,
    val showBlockHighlight: Boolean = true,
    val showLabel: Boolean = false,
    val showDistance: Boolean = false
) {
    // Additional properties and methods can be added based on your customization needs
}