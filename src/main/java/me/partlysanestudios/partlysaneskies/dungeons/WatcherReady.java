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

package me.partlysanestudios.partlysaneskies.dungeons;

import java.awt.Color;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WatcherReady {

    public WatcherReady() {

    }

    Window window = new Window(ElementaVersion.V2);

    UIComponent watcherReadyUIText = new UIText("")
            .setTextScale(new PixelConstraint(3))
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(window.getHeight() * .333f))
            .setColor(Color.white)
            .setChildOf(window);

    String watcherReadyString = "";
    long watcherReadyBannerTime;

    @SubscribeEvent
    public void watcherReadyChatEvent(ClientChatReceivedEvent event) {

        if (event.message.getUnformattedText().startsWith("[BOSS] The Watcher: That will be enough for now.")) {
            if (PartlySaneSkies.config.watcherReadyBanner) {
                watcherReadyBannerTime = Minecraft.getSystemTime();
                watcherReadyString = "Watcher Ready!";
            }
            if (PartlySaneSkies.config.watcherReadyChatMessage) {
                PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/pc " + PartlySaneSkies.config.watcherChatMessage);
            }
            if (PartlySaneSkies.config.watcherReadySound) {
                PartlySaneSkies.minecraft.getSoundHandler()
                        .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "bell")));
            }
            if (PartlySaneSkies.config.watcherReadyAirRaidSiren) {
                // Main.minecraft.theWorld.playSoundAtEntity(Main.minecraft.thePlayer,
                // "partlysaneskies:airraidsiren", 100, 1);
                // Main.minecraft.thePlayer.playSound("partlysaneskies:airraidsiren", 100, 1);

                PartlySaneSkies.minecraft.getSoundHandler().playSound(
                        PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "airraidsiren")));
            }
        }
    }

    @SubscribeEvent
    public void renderText(RenderGameOverlayEvent.Text event) {
        ((UIText) watcherReadyUIText).setText(watcherReadyString).setColor(PartlySaneSkies.config.watcherReadyBannerColor.toJavaColor());
        window.draw(new UMatrixStack());
        if (watcherReadyBannerTime + PartlySaneSkies.config.watcherReadyBannerTime * 1000 < Minecraft.getSystemTime())
            watcherReadyString = "";
    }
}
