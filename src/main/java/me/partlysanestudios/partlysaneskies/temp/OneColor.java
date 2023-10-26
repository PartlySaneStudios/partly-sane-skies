package me.partlysanestudios.partlysaneskies.temp;

import java.awt.Color;

public class OneColor {

    private final Color color;

    public OneColor(Color color) {
        this.color = color;
    }

    public OneColor(int i, int i1, int i2, int i3) {
        // TODO this might not be correct
        this(new Color(i, i1, i2, i3));
    }

    public OneColor(int x, int y, int z) {
        this(new Color(x, y, z));
    }

    public Color toJavaColor() {
        return color;
    }

    public int getRGB() {
        return color.getRGB();
    }
}
