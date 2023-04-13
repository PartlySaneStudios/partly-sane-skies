//
// Written by Su386.
// See LICENSE for copright and license notices.
//

package me.partlysanestudios.partlysaneskies.rngdropbanner;

import net.minecraft.client.Minecraft;
import java.awt.Color;

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

    public boolean isStillDisplay(long currentTime) {

        if (timeDropped + displayTime < Minecraft.getSystemTime())
            return true;
        return false;
    }
}
