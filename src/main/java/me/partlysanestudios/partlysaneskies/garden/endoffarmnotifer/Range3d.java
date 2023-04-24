//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer;

import me.partlysanestudios.partlysaneskies.utils.StringUtils;

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
        return StringUtils.colorCodes("&7" + getRangeName() + " &b(" + smallCoordinate[0] + ", " + smallCoordinate[1] + ", " + smallCoordinate[2] + ")&7 to &b(" + largeCoordinate[0] + ", " + largeCoordinate[1] + ", " + largeCoordinate[2] + ")");
    }
}

