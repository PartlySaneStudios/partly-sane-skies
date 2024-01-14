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
            if (PartlySaneSkies.Companion.getConfig().watcherReadyBanner) {
                BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner("Watcher Ready!", (long) (PartlySaneSkies.Companion.getConfig().watcherReadyBannerTime * 1000), 3.0f, PartlySaneSkies.Companion.getConfig().watcherReadyBannerColor.toJavaColor()));
            }
            if (PartlySaneSkies.Companion.getConfig().watcherReadyChatMessage) {
                PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/pc " + PartlySaneSkies.Companion.getConfig().watcherChatMessage);
            }
            if (PartlySaneSkies.Companion.getConfig().watcherReadySound) {
                PartlySaneSkies.Companion.getMinecraft().getSoundHandler()
                        .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "bell")));
            }
            if (PartlySaneSkies.Companion.getConfig().watcherReadyAirRaidSiren) {
                PartlySaneSkies.Companion.getMinecraft().getSoundHandler().playSound(
                        PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "airraidsiren")));
            }
        }
    }
}
