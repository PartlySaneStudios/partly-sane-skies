//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.system.BannerRenderer;
import me.partlysanestudios.partlysaneskies.system.PSSBanner;
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils;
import me.partlysanestudios.partlysaneskies.utils.ImageUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class LocationBannerDisplay extends Gui {
    float TEXT_SCALE = 5f;
    String lastLocation = "";
    public long lastLocationTime = PartlySaneSkies.getTime();
    String displayString = "empty";

    Color color = Color.white;
    

    public LocationBannerDisplay() {
    }

    public void checkLocation() {
        if (!PartlySaneSkies.config.locationBannerDisplay)
            return;

        String regionName = HypixelUtils.INSTANCE.getRegionName();
        String noColorCodeRegionName = StringUtils.INSTANCE.removeColorCodes(regionName);
        if (checkExpire()) {
            displayString = "";
        }

        if (noColorCodeRegionName.isEmpty()) {
            return;
        }
        
        noColorCodeRegionName = StringUtils.INSTANCE.stripLeading(noColorCodeRegionName);
        noColorCodeRegionName = StringUtils.INSTANCE.stripTrailing(noColorCodeRegionName);
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

        if (!regionName.isEmpty()) {
            color = StringUtils.INSTANCE.colorCodeToColor(regionName.substring(3, 5));
        }
        if (color == null) {
            color = new Color(170, 170, 170);
        }

        displayString = noColorCodeRegionName;
        lastLocation = noColorCodeRegionName;
        lastLocationTime = PartlySaneSkies.getTime();

        BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner(displayString, (long) (PartlySaneSkies.config.locationBannerTime * 1000), TEXT_SCALE, color));
    }

    private boolean checkExpire() {
        return getTimeSinceLastChange() > PartlySaneSkies.config.locationBannerTime * 1000;
    }

    private long getTimeSinceLastChange() {
        return PartlySaneSkies.getTime() - lastLocationTime;
    }
}
