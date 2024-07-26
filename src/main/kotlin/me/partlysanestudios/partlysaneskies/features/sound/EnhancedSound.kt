//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.features.sound

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.client.audio.ISound.AttenuationType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EnhancedSound {
    private val instruments =
        arrayOf(
            "live_clarinet",
            "clarinet",
            "electric_piano",
            "flute",
            "organ",
            "piano",
            "string_ensemble",
            "trombone",
            "trumpet",
            "violin",
            "wind_ensemble",
            "discord_sound",
            "kazoo",
        )

    private fun buildReplacementSound(
        event: PlaySoundEvent,
        resourceLocation: ResourceLocation,
    ): ISound =
        object : ISound {
            override fun getSoundLocation(): ResourceLocation = resourceLocation

            override fun canRepeat(): Boolean = false

            override fun getRepeatDelay(): Int = 0

            override fun getVolume(): Float = event.sound.volume

            override fun getPitch(): Float = event.sound.pitch

            override fun getXPosF(): Float =
                minecraft.thePlayer.position.y
                    .toFloat()

            override fun getYPosF(): Float =
                minecraft.thePlayer.position.y
                    .toFloat()

            override fun getZPosF(): Float =
                minecraft.thePlayer.position.z
                    .toFloat()

            override fun getAttenuationType(): AttenuationType = AttenuationType.NONE
        }

    @SubscribeEvent
    fun onSoundEvent(event: PlaySoundEvent) {
        if (event.name.equals("note.pling", ignoreCase = true)) {
            if (config.customSoundOption == 0) {
                return
            }
            val sound =
                buildReplacementSound(
                    event,
                    ResourceLocation("partlysaneskies", "tenor_" + instruments[config.customSoundOption - 1]),
                )
            event.result = sound
            minecraft.soundHandler
                .playSound(sound)
        }
        if (event.name.equals("note.bassattack", ignoreCase = true)) {
            if (config.customSoundOption == 0) {
                return
            }
            val sound =
                buildReplacementSound(
                    event,
                    ResourceLocation("partlysaneskies", "bass_" + instruments[config.customSoundOption - 1]),
                )
            event.result = sound
            minecraft.soundHandler.playSound(sound)
        } else if (event.name.equals("note.harp", ignoreCase = true)) {
            if (config.customSoundOption == 0) {
                return
            }
            val sound =
                buildReplacementSound(
                    event,
                    ResourceLocation("partlysaneskies", "alto_" + instruments[config.customSoundOption - 1]),
                )

            event.result = sound
            minecraft.soundHandler.playSound(sound)
        }
        if (event.name.equals("random.explode", ignoreCase = true)) {
            if (config.customExplosion == 0) {
                return
            } else if (config.customExplosion == 1) {
                event.result = null
                return
            }
            val sound = buildReplacementSound(event, ResourceLocation("partlysaneskies", "explosion"))

            event.result = sound
            minecraft.soundHandler.playSound(sound)
        }
    }
}
