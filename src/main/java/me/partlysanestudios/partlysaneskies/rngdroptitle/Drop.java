package me.partlysanestudios.partlysaneskies.rngdroptitle;

import net.minecraft.client.Minecraft;

public class Drop {
    public String name;
    public String dropCategory;
    public int amount;
    public int magicFind;
    public long timeDropped;
    public int displayTime;
    public int color;

    public Drop(String name, String dropCategory, int amount, int magicFind, long timeDropped, int hex) {
        this.name = name;
        this.dropCategory = dropCategory;
        this.amount = amount;
        this.magicFind = magicFind;
        this.timeDropped = timeDropped;
        this.color = hex;  
    }

    public boolean isStillDisplay(long currentTime) {

        if(timeDropped + displayTime < Minecraft.getSystemTime()) return true;
        return false;
    }
}
