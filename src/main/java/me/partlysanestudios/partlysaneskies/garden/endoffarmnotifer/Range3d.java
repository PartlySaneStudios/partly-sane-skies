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

