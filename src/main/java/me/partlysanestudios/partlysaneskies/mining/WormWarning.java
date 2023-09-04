//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.mining;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
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

    Window window = new Window(ElementaVersion.V2);

    UIComponent wormWarningUIText = new UIText("")
            .setTextScale(new PixelConstraint(3))
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(window.getHeight() * .333f))
            .setColor(PartlySaneSkies.config.wormWarningBannerColor.toJavaColor())
            .setChildOf(window);

    String wormWarningString = "";
    long wormWarningBannerTime;

    @SubscribeEvent
    public void wormWarningChatEvent(ClientChatReceivedEvent event) {

        if (event.message.getUnformattedText().startsWith("You hear the sound of something approaching...")) {
            if (PartlySaneSkies.config.wormWarningBanner) {
                wormWarningBannerTime = PartlySaneSkies.getTime();
                wormWarningString = "A Worm Has Spawned!";
            }
            if (PartlySaneSkies.config.wormWarningBannerSound) {
                PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            }
        }
    }

    @SubscribeEvent
    public void renderText(RenderGameOverlayEvent.Text event) {
        ((UIText) wormWarningUIText).setText(wormWarningString).setColor(PartlySaneSkies.config.wormWarningBannerColor.toJavaColor());
        window.draw(new UMatrixStack());
        if (Utils.onCooldown(wormWarningBannerTime, (long) (PartlySaneSkies.config.wormWarningBannerTime * 1000L))) {
            wormWarningString = "";
        }
    }
}
