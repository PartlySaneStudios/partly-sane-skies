//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.hud.rngdropbanner;

import java.awt.Color;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;

public class Drop {
    public String name;
    public String dropCategory;
    public int amount;
    public long timeDropped;
    public int displayTime;
    public Color dropCategoryColor;

    public Drop(String name, String dropCategory, int amount, long timeDropped, Color dropCategoryColor) {
        this.name = name;
        this.dropCategory = dropCategory;
        this.amount = amount;
        this.timeDropped = timeDropped;
        this.dropCategoryColor = dropCategoryColor;
    }

    public boolean isStillDisplay() {

        return timeDropped + displayTime < PartlySaneSkies.getTime();
    }
}
