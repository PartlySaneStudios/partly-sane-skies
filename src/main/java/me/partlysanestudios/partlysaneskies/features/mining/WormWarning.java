//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.gui.hud.PSSBanner;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WormWarning {

    String wormWarningString = "";
    long wormWarningBannerTime;

    @SubscribeEvent
    public void wormWarningChatEvent(ClientChatReceivedEvent event) {

        if (event.message.getUnformattedText().startsWith("You hear the sound of something approaching...")) {
            if (PartlySaneSkies.Companion.getConfig().wormWarningBanner) {
                wormWarningBannerTime = PartlySaneSkies.Companion.getTime();
                wormWarningString = "A Worm Has Spawned!";

                BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner(wormWarningString, (long) (PartlySaneSkies.Companion.getConfig().wormWarningBannerTime * 1000), 3, PartlySaneSkies.Companion.getConfig().wormWarningBannerColor.toJavaColor()));
            }
            if (PartlySaneSkies.Companion.getConfig().wormWarningBannerSound) {
                PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            }
        }
    }
}
