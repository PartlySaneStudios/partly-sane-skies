//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

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
                watcherReadyBannerTime = PartlySaneSkies.getTime();
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
        if (watcherReadyBannerTime + PartlySaneSkies.config.watcherReadyBannerTime * 1000 < PartlySaneSkies.getTime())
            watcherReadyString = "";
    }
}
