/*
 * Partly Sane Skies: A Hypixel Skyblock QOL and Economy mod
 * Created by Su386#9878 (Su386yt) and FlagMaster#1516 (FlagHater), the Partly Sane Studios team
 * Copyright (C) ©️ Su386 and FlagMaster 2023
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
