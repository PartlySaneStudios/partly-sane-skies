//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies


import me.partlysanestudios.partlysaneskies.utils.MathUtils
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.time.LocalDate
import java.time.Month


private const val numOfSounds = 7
private var lastPrankTime = PartlySaneSkies.getTime()

/*
    Sound indexes:
    - 0: metal pipe
    - 1: loud af siren
    - 2: iphone shutter
    - 3: vine boom
    - 4: discord message sfx
    - 5: thanos snap
    - 6: knock knock
*/

class Prank {
    private fun execute() {
        val index = (Math.random() * numOfSounds).toInt().toString()

        PartlySaneSkies.minecraft.soundHandler
            .playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "prank$index")))
    }

    private fun checkDate(): Boolean{
        val today = LocalDate.now()
        val april1st = LocalDate.of(today.year, Month.APRIL, 1)
        val october31st = LocalDate.of(today.year, Month.OCTOBER, 31)

        return today == april1st || today == october31st
    }

    @SubscribeEvent
    fun run(event: TickEvent.ClientTickEvent) {
        if (!MathUtils.onCooldown(lastPrankTime, 300000)) { // 5 minutes
            return
        }

        if (!shouldPrankREPO) {
            return
        }

        if (!checkDate()) {
            return
        }

        // 20 ticks per second, 1200 ticks per minute, 54000 ticks per 3/4 hour
        val rng = (Math.random() * 54000).toInt()

        if (rng == 0) {
            execute()
            lastPrankTime = PartlySaneSkies.getTime()
        }
    }

    companion object {
        var shouldPrankREPO = false

        fun setPrankKillSwitch(value: Boolean) {
            shouldPrankREPO = value
        }
    }
}