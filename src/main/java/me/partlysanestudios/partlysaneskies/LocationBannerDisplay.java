//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import java.awt.Color;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LocationBannerDisplay extends Gui {
    float TEXT_SCALE = 5f;

    String lastLocation = "";
    public long lastLocationTime = PartlySaneSkies.getTime();

    Window window = new Window(ElementaVersion.V2);
    String displayString = "empty";

    Color color = Color.white;

    UIComponent displayText = new UIText(displayString)
            .setTextScale(new PixelConstraint(TEXT_SCALE))
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(window.getHeight() * .125f))
            .setColor(Color.white)
            .setChildOf(window);

    public LocationBannerDisplay() {
    }

    public void checkLocation() {
        if (!PartlySaneSkies.config.locationBannerDisplay)
            return;

        String regionName = PartlySaneSkies.getRegionName();
        String noColorCodeRegionName = StringUtils.removeColorCodes(regionName);

        if (noColorCodeRegionName.equals("")) {
            return;
        }
        
        noColorCodeRegionName = StringUtils.stripLeading(noColorCodeRegionName);
        noColorCodeRegionName = StringUtils.stripTrailing(noColorCodeRegionName);
        noColorCodeRegionName = noColorCodeRegionName.replaceAll("\\P{Print}", ""); // Removes the RANDOM EMOJIS
        // THAT ARE PRESENT IN SKY-BLOCK LOCATIONS
        // LOOK AT THIS:
        // The Catac🔮ombs (F5)
        // The Catac👽ombs (F5)
        // The Catac🔮ombs (F5)
        // Dungeon H👾ub
        // Mountain⚽
        // Village⚽
        // Coal Mine⚽
        // THEY'RE NOT EVEN VISIBLE IN MINECRAFT - Su386
        // (ITS NOT SPELLED VISABLE - j10a)
        // (It's* - Su386)

        if (lastLocation.equals(noColorCodeRegionName)) {
            return;
        }

        if (noColorCodeRegionName.toLowerCase().contains("none")) {
            return;
        }

        if (!regionName.equals("")) {
            color = Utils.colorCodetoColor.get(regionName.substring(3, 5));
        }

        displayString = noColorCodeRegionName;
        lastLocation = noColorCodeRegionName;
        lastLocationTime = PartlySaneSkies.getTime();
    }

    private boolean checkExpire() {
        return getTimeSinceLastChange() > PartlySaneSkies.config.locationBannerTime * 1000;
    }

    @SubscribeEvent
    public void renderText(RenderGameOverlayEvent.Text event) {
        short alpha = getAlpha(getTimeSinceLastChange(), PartlySaneSkies.config.locationBannerTime);

        if (color == null)
            color = Color.gray;
        else
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);

        ((UIText) displayText)
                .setText(displayString)
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(window.getHeight() * .125f))
                .setColor(color);
        window.draw(new UMatrixStack());

        if (checkExpire())
            displayString = "";
    }

    private long getTimeSinceLastChange() {
        return PartlySaneSkies.getTime() - lastLocationTime;
    }

    public static short getAlpha(long timeSinceLastChangeMs, double displayLenghtSeconds) {
        double displayLength = displayLenghtSeconds * 1000;
        double fadeLength = displayLength * (1 / 6d);

        if (0 > timeSinceLastChangeMs) {
            return 0;
        } else if (0 < timeSinceLastChangeMs && timeSinceLastChangeMs < fadeLength) {
            return (short) Math.round(timeSinceLastChangeMs / fadeLength * 255);
        } else if (fadeLength < timeSinceLastChangeMs && timeSinceLastChangeMs <= displayLength - fadeLength) {
            return 255;
        } else if (displayLength - fadeLength < timeSinceLastChangeMs && timeSinceLastChangeMs <= displayLength) {
            return (short) Math.round((-timeSinceLastChangeMs + displayLength) / fadeLength * 255);
        } else {

            return 0;
        }

    }
}
