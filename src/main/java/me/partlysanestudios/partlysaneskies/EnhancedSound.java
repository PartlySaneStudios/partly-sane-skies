/*
 * Partly Sane Skies: A Hypixel Skyblock QOL and Economy mod
 * Created by Su386#9878 (Su386yt) and FlagMaster#1516 (FlagHater), the Partly Sane Studios team
 * Copyright (C) ©️ Su386 and FlagMaster 2023
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package me.partlysanestudios.partlysaneskies;

import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnhancedSound {
    private String[] instruments = {
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
        "kazoo"
    };

    @SubscribeEvent
    public void onSoundEvent(PlaySoundEvent event) {
        // Utils.visPrint(event.name);


        if (event.name.equalsIgnoreCase("note.pling")) {
            if (PartlySaneSkies.config.customSoundOption == 0) {
                return;
            }
            ISound sound = new ISound() {
                @Override
                public ResourceLocation getSoundLocation() {
                    return new ResourceLocation("partlysaneskies", "tenor_" + instruments[PartlySaneSkies.config.customSoundOption - 1]);
                }

                @Override
                public boolean canRepeat() {
                    return false;
                }

                @Override
                public int getRepeatDelay() {
                    return 0;
                }

                @Override
                public float getVolume() {
                    return event.sound.getVolume();
                }

                @Override
                public float getPitch() {
                    return event.sound.getPitch();
                }

                @Override
                public float getXPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getY();
                }

                @Override
                public float getYPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getY();
                }

                @Override
                public float getZPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getZ();
                }

                @Override
                public AttenuationType getAttenuationType() {
                    return AttenuationType.NONE;
                }

            };
            event.result = sound;
            PartlySaneSkies.minecraft.getSoundHandler()
                        .playSound(sound);
        }

        if (event.name.equalsIgnoreCase("note.bassattack")) {
            if (PartlySaneSkies.config.customSoundOption == 0) {
                return;
            }
            ISound sound = new ISound() {
                @Override
                public ResourceLocation getSoundLocation() {
                    return new ResourceLocation("partlysaneskies", "bass_" + instruments[PartlySaneSkies.config.customSoundOption - 1]);
                }

                @Override
                public boolean canRepeat() {
                    return false;
                }

                @Override
                public int getRepeatDelay() {
                    return 0;
                }

                @Override
                public float getVolume() {
                    return event.sound.getVolume();
                }

                @Override
                public float getPitch() {
                    return event.sound.getPitch();
                }

                @Override
                public float getXPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getY();
                }

                @Override
                public float getYPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getY();
                }

                @Override
                public float getZPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getZ();
                }

                @Override
                public AttenuationType getAttenuationType() {
                    return AttenuationType.NONE;
                }
            };
            event.result = sound;
            PartlySaneSkies.minecraft.getSoundHandler()
                        .playSound(sound);
        }

        else if (event.name.equalsIgnoreCase("note.harp")) {
            if (PartlySaneSkies.config.customSoundOption == 0) {
                return;
            }
            ISound sound = new ISound() {
                @Override
                public ResourceLocation getSoundLocation() {
                    return new ResourceLocation("partlysaneskies", "alto_" + instruments[PartlySaneSkies.config.customSoundOption - 1]);
                }

                @Override
                public boolean canRepeat() {
                    return false;
                }

                @Override
                public int getRepeatDelay() {
                    return 0;
                }

                @Override
                public float getVolume() {
                    return event.sound.getVolume();
                }

                @Override
                public float getPitch() {
                    return event.sound.getPitch();
                }

                @Override
                public float getXPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getY();
                }

                @Override
                public float getYPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getY();
                }

                @Override
                public float getZPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getZ();
                }

                @Override
                public AttenuationType getAttenuationType() {
                    return AttenuationType.NONE;
                }

            };
            event.result = sound;
            PartlySaneSkies.minecraft.getSoundHandler()
                        .playSound(sound);
        }
        // Utils.visPrint(event.name);
        if (event.name.equalsIgnoreCase("random.explode")) {
            if (PartlySaneSkies.config.customExplosion == 0) {
                return;
            }
            else if (PartlySaneSkies.config.customExplosion == 1) {
                event.result = null;
                return;
            }
            ISound sound = new ISound() {
                @Override
                public ResourceLocation getSoundLocation() {
                    return new ResourceLocation("partlysaneskies", "explosion");
                }

                @Override
                public boolean canRepeat() {
                    return false;
                }

                @Override
                public int getRepeatDelay() {
                    return 0;
                }

                @Override
                public float getVolume() {
                    return event.sound.getVolume() * .666667f;
                }

                @Override
                public float getPitch() {
                    return event.sound.getPitch();
                }

                @Override
                public float getXPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getY();
                }

                @Override
                public float getYPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getY();
                }

                @Override
                public float getZPosF() {
                    return PartlySaneSkies.minecraft.thePlayer.getPosition().getZ();
                }

                @Override
                public AttenuationType getAttenuationType() {
                    return AttenuationType.NONE;
                }

            };
            event.result = sound;
            PartlySaneSkies.minecraft.getSoundHandler()
                        .playSound(sound);
        }
    }
}
