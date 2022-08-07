package me.partlysanestudios.partlysaneskies.rngdropbanner;

import net.minecraft.client.Minecraft;

public class Drop {
    public String name;
    public String dropCategory;
    public int amount;
    public int magicFind;
    public long timeDropped;
    public int displayTime;
    public int dropCategoryColor;
    public int dropNameHex;

    public Drop(String name, String dropCategory, int amount, int magicFind, long timeDropped, int dropCategoryHex, int dropNameHex) {
        this.name = name;
        this.dropCategory = dropCategory;
        this.amount = amount;
        this.magicFind = magicFind;
        this.timeDropped = timeDropped;
        this.dropCategoryColor = dropCategoryHex;  
        this.dropNameHex = dropNameHex;
    }

    public boolean isStillDisplay(long currentTime) {

        if(timeDropped + displayTime < Minecraft.getSystemTime()) return true;
        return false;
    }
}
