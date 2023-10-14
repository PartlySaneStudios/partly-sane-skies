package me.partlysanestudios.partlysaneskies

import me.partlysanestudios.partlysaneskies.utils.Utils
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.time.LocalDate
import java.time.Month


private const val numOfSounds = 7
private var lastPrankTime = PartlySaneSkies.getTime();
private var dateState = "unknown"

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

        // If the date has already been checked, return the result
        if (dateState != "unknown") {
            return dateState == "valid"
        }

        val today = LocalDate.now()
        val april1st = LocalDate.of(today.year, Month.APRIL, 1)
        val october31st = LocalDate.of(today.year, Month.OCTOBER, 31)

        dateState = if (today == april1st || today == october31st) {
            "valid"
        } else {
            "invalid"
        }
        return today == april1st || today == october31st
    }

    @SubscribeEvent
    fun run(event: TickEvent.ClientTickEvent) {
        if (!Utils.onCooldown(lastPrankTime, 300000)) { // 5 minutes
            return
        }

        if (!checkDate()) {
            return
        }

        // 20 ticks per second, 1200 ticks per minute, 36000 ticks per half hour
        val rng = (Math.random() * 36000).toInt()

        if (rng == 0) {
            execute()
            lastPrankTime = PartlySaneSkies.getTime()
        }
    }
}