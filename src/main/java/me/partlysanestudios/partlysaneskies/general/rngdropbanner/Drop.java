package me.partlysanestudios.partlysaneskies.general.rngdropbanner;

import net.minecraft.client.Minecraft;
import java.awt.Color;

public class Drop {
    public String name;
    public String dropCategory;
    public int amount;
    public int magicFind;
    public long timeDropped;
    public int displayTime;
    public Color dropCategoryColor;
    public Color dropNameColor;

    public Drop(String name, String dropCategory, int amount, int magicFind, long timeDropped, Color dropCategoryColor, Color dropNameColor) {
        this.name = name;
        this.dropCategory = dropCategory;
        this.amount = amount;
        this.magicFind = magicFind;
        this.timeDropped = timeDropped;
        this.dropCategoryColor = dropCategoryColor;  
        this.dropNameColor = dropNameColor;
    }

    public boolean isStillDisplay(long currentTime) {

        if(timeDropped + displayTime < Minecraft.getSystemTime()) return true;
        return false;
    }
}
