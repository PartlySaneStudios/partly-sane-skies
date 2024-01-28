//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import net.minecraft.item.ItemStack

abstract class Cooldown {

    private var startingTime = -1L
    /**
     * @return Starting time of cooldown in milliseconds
     */
    fun getStartingTime(): Long {
        return startingTime
    }

    /**
     * @return Ending time of the cooldown in milliseconds
     */
    fun getEndingTime(): Long {
        return getStartingTime() + getTotalTime()
    }

    /**
     * @return Total time of the cooldown in milliseconds
     */
    abstract fun getTotalTime(): Long

    /**
     * @return Time remaining of the cooldown in milliseconds
     */
    fun getTimeRemaining(): Long {
        return getEndingTime() - PartlySaneSkies.time
    }

    /**
     * Starts the cooldown by setting the starting time to now
     */
    fun startCooldown() {
        startingTime = PartlySaneSkies.time
    }

    /*
     * Returns true if the cooldown is active, false if it is not
     */
    fun isCooldownActive(): Boolean {
        return MathUtils.onCooldown(startingTime, getTotalTime())
    }

    /*
     * Returns the name of the cooldown
     */
    abstract fun getDisplayName(): String

    abstract fun getItemToDisplay(): ItemStack
}