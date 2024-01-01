//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.gui.hud.PSSBanner;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WatcherReady {
    @SubscribeEvent
    public void watcherReadyChatEvent(ClientChatReceivedEvent event) {

        if (event.message.getUnformattedText().startsWith("[BOSS] The Watcher: That will be enough for now.")) {
            if (PartlySaneSkies.config.watcherReadyBanner) {
                BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner("Watcher Ready!", (long) (PartlySaneSkies.config.watcherReadyBannerTime * 1000), 3.0f, PartlySaneSkies.config.watcherReadyBannerColor.toJavaColor()));
            }
            if (PartlySaneSkies.config.watcherReadyChatMessage) {
                PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/pc " + PartlySaneSkies.config.watcherChatMessage);
            }
            if (PartlySaneSkies.config.watcherReadySound) {
                PartlySaneSkies.minecraft.getSoundHandler()
                        .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "bell")));
            }
            if (PartlySaneSkies.config.watcherReadyAirRaidSiren) {
                PartlySaneSkies.minecraft.getSoundHandler().playSound(
                        PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "airraidsiren")));
            }
        }
    }
}
