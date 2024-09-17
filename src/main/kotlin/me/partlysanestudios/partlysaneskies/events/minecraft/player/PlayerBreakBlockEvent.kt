//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.events.minecraft.player

import me.partlysanestudios.partlysaneskies.api.events.PSSEvent
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d
import net.minecraft.util.EnumFacing

class PlayerBreakBlockEvent(val point: Point3d, val side: EnumFacing) : PSSEvent()
