//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WormWarning {

    String wormWarningString = "";
    long wormWarningBannerTime;

    @SubscribeEvent
    public void wormWarningChatEvent(ClientChatReceivedEvent event) {

        if (event.message.getUnformattedText().startsWith("You hear the sound of something approaching...")) {
            if (PartlySaneSkies.Companion.getConfig().mining.wormWarningBanner) {
                wormWarningBannerTime = PartlySaneSkies.Companion.getTime();
                wormWarningString = "A Worm Has Spawned!";

                BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner(wormWarningString, (long) (PartlySaneSkies.Companion.getConfig().mining.wormWarningBannerTime * 1000), 3, PartlySaneSkies.Companion.getConfig().mining.wormWarningBannerColor.toJavaColor()));
            }
            if (PartlySaneSkies.Companion.getConfig().mining.wormWarningBannerSound) {
                PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            }
        }
    }
}
