package me.partlysanestudios.partlysaneskies.renderers.waypoint

import net.minecraft.util.BlockPos

class Waypoint(
    val name: String,
    val position: BlockPos,
    val color: Int = 0xFF0000,
    val showBeam: Boolean = true,
    val showBlockHighlight: Boolean = true,
    val showLabel: Boolean = true,
    val showDistance: Boolean = true
) {
    // Additional properties and methods can be added based on your customization needs
}