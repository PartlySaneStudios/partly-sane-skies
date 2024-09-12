//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.events.minecraft

import me.partlysanestudios.partlysaneskies.api.events.PSSEvent
import net.minecraft.util.IChatComponent

class PSSChatEvent(val message: String, val component: IChatComponent) : PSSEvent(), PSSEvent.Cancellable
