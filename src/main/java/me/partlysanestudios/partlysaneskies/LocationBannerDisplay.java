//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.system.BannerRenderer;
import me.partlysanestudios.partlysaneskies.system.PSSBanner;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.LocationUtils;
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

        String regionName = LocationUtils.getRegionName();
        String noColorCodeRegionName = LocationUtils.getLocation();
        if (checkExpire()) {
            displayString = "";
        }

        if (lastLocation.equals(noColorCodeRegionName)) {
            return;
        }

        if (noColorCodeRegionName.toLowerCase().contains("none")) {
            return;
        }

        if (!regionName.isEmpty()) {
            color = Utils.colorCodetoColor.get(regionName.substring(3, 5));
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
