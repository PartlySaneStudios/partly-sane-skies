//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer;

import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;

import java.util.Arrays;

public class Range3d {
    private final double[] smallCoordinate;
    private final double[] largeCoordinate;
    private String rangeName;

    public Range3d (double x1, double y1, double z1, double x2, double y2, double z2) {
        this.smallCoordinate = new double[3];
        this.largeCoordinate = new double[3];

        this.smallCoordinate[0] = Math.min(x1, x2);
        this.smallCoordinate[1] = Math.min(y1, y2);
        this.smallCoordinate[2] = Math.min(z1, z2);

        this.largeCoordinate[0] = Math.max(x1, x2);
        this.largeCoordinate[1] = Math.max(y1, y2);
        this.largeCoordinate[2] = Math.max(z1, z2);
        this.rangeName = "";
    }

    public boolean isInRange(int x, int y, int z) {
        if(smallCoordinate[0] <= x && x <= largeCoordinate[0]){
            if(smallCoordinate[1] <= y  && y <= largeCoordinate[1]){
                return smallCoordinate[2] <= z && z <= largeCoordinate[2];
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

    public Point3d[] getPoints() {
        return new Point3d[] {new Point3d(smallCoordinate[0], smallCoordinate[1], smallCoordinate[2]), new Point3d(largeCoordinate[0], largeCoordinate[1], largeCoordinate[2])};
    }

    @Override
    public String toString() {
        return StringUtils.colorCodes("&7" + getRangeName() + " &b(" + smallCoordinate[0] + ", " + smallCoordinate[1] + ", " + smallCoordinate[2] + ")&7 to &b(" + largeCoordinate[0] + ", " + largeCoordinate[1] + ", " + largeCoordinate[2] + ")");
    }

    public static class Point3d extends Point2d{
        private final double z;

        public Point3d(double x, double y, double z) {
            super(x, y);
            this.z = z;
        }

        public double getZ() {
            return z;
        }
    }

    public static class Point2d {
        private final double x;
        private final double y;

        public Point2d(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getDistanceTo(Point2d point) {
            return Utils.getDistance(this, point);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range3d range3d = (Range3d) o;
        return Arrays.equals(smallCoordinate, range3d.smallCoordinate) && Arrays.equals(largeCoordinate, range3d.largeCoordinate);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(smallCoordinate);
        result = 31 * result + Arrays.hashCode(largeCoordinate);
        return result;
    }
}

