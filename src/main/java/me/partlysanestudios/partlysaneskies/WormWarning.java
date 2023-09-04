//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.system.BannerRenderer;
import me.partlysanestudios.partlysaneskies.system.PSSBanner;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WormWarning {

    public WormWarning() {

    }

    String wormWarningString = "";
    long wormWarningBannerTime;

    @SubscribeEvent
    public void wormWarningChatEvent(ClientChatReceivedEvent event) {

        if (event.message.getUnformattedText().startsWith("You hear the sound of something approaching...")) {
            if (PartlySaneSkies.config.wormWarningBanner) {
                wormWarningBannerTime = PartlySaneSkies.getTime();
                wormWarningString = "A Worm Has Spawned!";

                BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner(wormWarningString, (long) (PartlySaneSkies.config.wormWarningBannerTime * 1000), 3, PartlySaneSkies.config.wormWarningBannerColor.toJavaColor()));
            }
            if (PartlySaneSkies.config.wormWarningBannerSound) {
                PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            }
        }
    }
}
