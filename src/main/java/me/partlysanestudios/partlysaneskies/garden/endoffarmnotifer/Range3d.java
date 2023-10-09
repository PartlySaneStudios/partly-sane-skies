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


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer;

public class Range3d {
    private int[] smallCoordinate;
    private int[] largeCoordinate;
    private String rangeName;

    public Range3d (int x1, int y1, int z1, int x2, int y2, int z2) {
        this.smallCoordinate = new int[3];
        this.largeCoordinate = new int[3];

        this.smallCoordinate[0] = Math.min(x1, x2);
        this.smallCoordinate[1] = Math.min(y1, y2) - 1;
        this.smallCoordinate[2] = Math.min(z1, z2);

        this.largeCoordinate[0] = Math.max(x1, x2);
        this.largeCoordinate[1] = Math.max(y1, y2) + 1;
        this.largeCoordinate[2] = Math.max(z1, z2);
        this.rangeName = "";
    }

    public boolean isInRange(int x, int y, int z) {
        if(smallCoordinate[0] <= x && x <= largeCoordinate[0]){
            if(smallCoordinate[1] <= y  && y <= largeCoordinate[1]){
                if(smallCoordinate[2] <= z  && z <= largeCoordinate[2]){
                    return true;
                }
            }
        }
        return false; 
    }

    public String getRangeName() {
        return rangeName;
    }

    public Range3d setRangeName(String name) {
        this.rangeName = name;

        return this;
    }

    @Override
    public String toString() {
        return  getRangeName() + " (" + smallCoordinate[0] + ", " + smallCoordinate[1] + ", " + smallCoordinate[2] + ") to (" + largeCoordinate[0] + ", " + largeCoordinate[1] + ", " + largeCoordinate[2] + ")";
    }
}

