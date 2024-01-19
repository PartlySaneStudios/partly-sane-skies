//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WatcherReady {
    @SubscribeEvent
    public void watcherReadyChatEvent(ClientChatReceivedEvent event) {

        if (event.message.getUnformattedText().startsWith("[BOSS] The Watcher: That will be enough for now.")) {
            if (PartlySaneSkies.Companion.getConfig().dungeons.watcherReadyBanner) {
                BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner("Watcher Ready!", (long) (PartlySaneSkies.Companion.getConfig().dungeons.watcherReadyBannerTime * 1000), 3.0f, PartlySaneSkies.Companion.getConfig().dungeons.watcherReadyBannerColor.toJavaColor()));
            }
            if (PartlySaneSkies.Companion.getConfig().dungeons.watcherReadyChatMessage) {
                PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/pc " + PartlySaneSkies.Companion.getConfig().dungeons.watcherChatMessage);
            }
            if (PartlySaneSkies.Companion.getConfig().dungeons.watcherReadySound) {
                PartlySaneSkies.Companion.getMinecraft().getSoundHandler()
                        .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "bell")));
            }
            if (PartlySaneSkies.Companion.getConfig().dungeons.watcherReadyAirRaidSiren) {
                PartlySaneSkies.Companion.getMinecraft().getSoundHandler().playSound(
                        PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "airraidsiren")));
            }
        }
    }
}
