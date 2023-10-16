package me.partlysanestudios.partlysaneskies.utils;

import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.PixelConstraint;

public class ElementaUtils {
    public static float getWidthScaleFactor(Window window) {
        return window.getWidth() / 1097f;
    }

    public static PixelConstraint widthScaledConstraint(float value, Window window) {
        return new PixelConstraint(value * ElementaUtils.getWidthScaleFactor(window));
    }
}
