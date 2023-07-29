package me.partlysanestudios.partlysaneskies.system;

import cc.polyfrost.oneconfig.config.core.OneColor;
import com.ibm.icu.text.MessagePattern;
import gg.essential.elementa.components.UIImage;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.ImageUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThemeManager {

    private static final Color PRIMARY_DEBUG_COLOR = new Color(255, 0, 0);
    private static final Color SECONDARY_DEBUG_COLOR = new Color(0, 0, 255);
    private static final Color ACCENT_DEBUG_COLOR = new Color(0, 255, 0);
    public static Theme[] defaultThemes = {
            new Theme("Classic (Dark)",  new Color(85, 85, 88), new Color(42, 43, 46), new Color(1, 255, 255)),
            new Theme("Royal Dark (Dark)", new Color(85, 85, 88), new Color(59, 51, 63), new Color(91, 192, 235)),
            new Theme("Midnight Forest (Dark)", new Color(85, 85, 88),  new Color(60, 70, 58), new Color(35, 206, 107)),

            new Theme("Moonless Night (Very Dark)", new Color(34, 34, 37), new Color(25, 27, 30), new Color(8, 124, 167)),
            new Theme("Stormy Night (Very Dark)", new Color(33, 33, 44), new Color(15, 15, 37), new Color(94, 74, 227)),
            new Theme("The Void (Very Dark)", new Color(14, 17, 21), new Color(5, 5, 12), new Color(113, 179, 64)),

            new Theme("Classic (Light)", new Color(245, 245, 245), new Color(213, 210, 195), new Color(42, 84, 209)),
            new Theme("Royal Light (Light)",  new Color(245, 245, 245), new Color(127, 106, 147), new Color(242, 97, 87)),
            new Theme("Partly Cloudy (Light)", new Color(245, 245, 245), new Color(84, 95, 117), new Color(217, 114, 255)),

            new Theme("Waterfall (Colorful)", new Color(214, 237, 246), new Color(172, 215, 236), new Color(108, 197, 81)),
            new Theme("Jungle (Colorful)", new Color(201, 227, 172), new Color(144, 190, 109), new Color(254, 100, 163)),
            new Theme("Dunes (Colorful)", new Color(229, 177, 129), new Color(222, 107, 72), new Color(131, 34, 50))
    };

    public static void run() {
        if (PartlySaneSkies.config.useDefaultAccentColor) {
            PartlySaneSkies.config.accentColor = new OneColor(getAccentColor());
        }

        if (!PartlySaneSkies.config.customTheme) {
            PartlySaneSkies.config.primaryColor = new OneColor(getPrimaryColor());
            PartlySaneSkies.config.secondaryColor = new OneColor(getSecondaryColor());
        }
    }


    public static UIImage getCurrentBackgroundUIImage() {
        if (PartlySaneSkies.config.disableThemes) {
            return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_background.png"));

        }

        try {
            return UIImage.ofFile(getCurrentBackgroundFile());
        } catch (IOException e) {
            return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_background.png"));
        }
    }

    public static UIImage getCurrentButtonUIImage() {
        return getCurrentButtonUIImage(getAccentColor());
    }

    public static UIImage getCurrentButtonUIImage(Color accentColor) {
        if (PartlySaneSkies.config.disableThemes) {
            if (accentColor.equals(getAccentColor())) {
                return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_button.png"));
            } else {
                return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_button_transparent.png"));
            }
        }

        try {
            return UIImage.ofFile(getCurrentButtonFile(accentColor));
        } catch (IOException e) {
            if (accentColor.equals(getAccentColor())) {
                return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_button.png"));
            } else {
                return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_button_transparent.png"));
            }
        }
    }


    public static UIImage getCurrentToggleUIImage(boolean selected) {
        return getCurrentToggleUIImage(selected, getAccentColor());
    }

    public static UIImage getCurrentToggleUIImage(boolean selected, Color accentColor) {
        if (PartlySaneSkies.config.disableThemes) {
            if (selected) {
                return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies" , "textures/gui/selected_toggle.png"));
            } else {
                return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies" , "textures/gui/unselected_toggle.png"));
            }
        }
        try {
            return UIImage.ofFile(getCurrentToggleFile(selected, accentColor));
        } catch (IOException e) {
            if (selected) {
                return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies" , "textures/gui/selected_toggle.png"));
            } else {
                return Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies" , "textures/gui/unselected_toggle.png"));
            }
        }
    }

    public static File getCurrentBackgroundFile() throws IOException {
        return getBackgroundWithColor(getPrimaryColor(), getSecondaryColor(), getAccentColor());
    }

    public static File getCurrentButtonFile() throws IOException {
        return getCurrentButtonFile(getAccentColor());
    }

    public static File getCurrentButtonFile(Color accentColor) throws IOException {
        return getButtonWithColor(getPrimaryColor(), getSecondaryColor(), accentColor);
    }

    public static File getCurrentToggleFile(boolean selected) throws IOException {
        return getCurrentToggleFile(selected, getAccentColor());
    }

    public static File getCurrentToggleFile(boolean selected, Color accentColor) throws IOException {
        if (selected) {
            return getToggleWithColor(getPrimaryColor(), getSecondaryColor(), accentColor);
        } else {
            return getToggleWithColor(getPrimaryColor(), getSecondaryColor(), getSecondaryColor());
        }
    }

    public static Color getPrimaryColor() {
        if (PartlySaneSkies.config.customTheme) {
            int themeIndex = PartlySaneSkies.config.themeIndex;
            return defaultThemes[themeIndex].getPrimaryColor();
        } else {
            return PartlySaneSkies.config.primaryColor.toJavaColor();
        }
    }

    public static Color getDarkPrimaryColor() {
        return darkenColor(getPrimaryColor());
    }

    public static Color getLightPrimaryColor() {
        return lightenColor(getPrimaryColor());
    }

    public static Color getSecondaryColor() {
        if (PartlySaneSkies.config.customTheme) {
            int themeIndex = PartlySaneSkies.config.themeIndex;
            return defaultThemes[themeIndex].getSecondaryColor();
        } else {
            return PartlySaneSkies.config.secondaryColor.toJavaColor();
        }
    }

    public static Color getDarkSecondaryColor() {
        return darkenColor(getSecondaryColor());
    }

    public static Color getLightSecondaryColor() {
        return lightenColor(getSecondaryColor());
    }

    public static Color getAccentColor() {
        if (PartlySaneSkies.config.useDefaultAccentColor) {
            int themeIndex = PartlySaneSkies.config.themeIndex;
            return defaultThemes[themeIndex].getDefaultAccentColor();
        } else {
            return PartlySaneSkies.config.accentColor.toJavaColor();
        }
    }

    public static Color getDarkAccentColor() {
        return darkenColor(getAccentColor());
    }

    public static Color getLightAccentColor() {
        return lightenColor(getAccentColor());
    }

    private static Color darkenColor(Color color) {
        int averageR = (int) (color.getRed()* .761);
        int averageG = (int) (color.getGreen() * .761);
        int averageB = (int) (color.getBlue() * .761);

        return new Color(averageR, averageG, averageB, getAccentColor().getTransparency());
    }

    private static Color lightenColor(Color color) {
        int averageR = (int) (color.getRed()* .798 + 255 * .202);
        int averageG = (int) (color.getGreen() * .798 + 255 * .202);
        int averageB = (int) (color.getBlue() * .798 + 255 * .202);

        return new Color(averageR, averageG, averageB, getAccentColor().getTransparency());
    }

    public static File getBackgroundWithColor(Color primaryColor, Color secondaryColor, Color accentColor) throws IOException {


        String primaryColorHex = Integer.toHexString(primaryColor.getRGB() & 0xffffff);
        String secondaryColorHex = Integer.toHexString(secondaryColor.getRGB() & 0xffffff);
        String accentColorHex = Integer.toHexString(accentColor.getRGB() & 0xffffff);

        Path folderPath = Paths.get("./config/partly-sane-skies/image_variants/background");
        String fileName = "background-" + primaryColorHex + "-" + secondaryColorHex + "-" + accentColorHex + ".png";
        Path filePath = Paths.get(folderPath.toString() + "/" + fileName);

        if (Files.exists(filePath)){
            return filePath.toFile();
        }


        ResourceLocation backgroundResourceLocation = new ResourceLocation("partlysaneskies", "textures/debug_gui_textures/background.png");

        IResource debugTexture = PartlySaneSkies.minecraft.getResourceManager().getResource(backgroundResourceLocation);
        BufferedImage debugImage = TextureUtil.readBufferedImage(debugTexture.getInputStream());

        folderPath.toFile().mkdirs();
        filePath.toFile().createNewFile();


        ImageUtils.replaceColor(debugImage, PRIMARY_DEBUG_COLOR, primaryColor);
        ImageUtils.replaceColor(debugImage, SECONDARY_DEBUG_COLOR, secondaryColor);
        ImageUtils.replaceColor(debugImage, ACCENT_DEBUG_COLOR, accentColor);

        ImageUtils.saveImage(debugImage, filePath);



        return filePath.toFile();
    }

    public static File getButtonWithColor(Color primaryColor, Color secondaryColor, Color accentColor) throws IOException {


        String primaryHex = Integer.toHexString(primaryColor.getRGB() & 0xffffff);
        String secondaryColorHex = Integer.toHexString(secondaryColor.getRGB() & 0xffffff);
        String accentColorHex = Integer.toHexString(accentColor.getRGB() & 0xffffff);

        Path folderPath = Paths.get("./config/partly-sane-skies/image_variants/button");
        String fileName = "button-" + primaryHex + "-" + secondaryColorHex + "-" + accentColorHex + ".png";
        Path filePath = Paths.get(folderPath.toString() + "/" + fileName);

        if (Files.exists(filePath)){
            return filePath.toFile();
        }


        ResourceLocation buttonResourceLocation = new ResourceLocation("partlysaneskies", "textures/debug_gui_textures/button.png");

        IResource debugTexture = PartlySaneSkies.minecraft.getResourceManager().getResource(buttonResourceLocation);
        BufferedImage debugImage = TextureUtil.readBufferedImage(debugTexture.getInputStream());

        folderPath.toFile().mkdirs();
        filePath.toFile().createNewFile();


        ImageUtils.replaceColor(debugImage, PRIMARY_DEBUG_COLOR, primaryColor);
        ImageUtils.replaceColor(debugImage, SECONDARY_DEBUG_COLOR, secondaryColor);
        ImageUtils.replaceColor(debugImage, ACCENT_DEBUG_COLOR, accentColor);

        ImageUtils.saveImage(debugImage, filePath);



        return filePath.toFile();
    }

    public static File getToggleWithColor(Color primaryColor, Color secondaryColor, Color accentColor) throws IOException {


        String primaryColorHex = Integer.toHexString(primaryColor.getRGB() & 0xffffff);
        String secondaryColorHex = Integer.toHexString(secondaryColor.getRGB() & 0xffffff);
        String accentColorHex = Integer.toHexString(accentColor.getRGB() & 0xffffff);

        Path folderPath = Paths.get("./config/partly-sane-skies/image_variants/toggle");
        String fileName = "toggle-" + primaryColorHex + "-" + secondaryColorHex + "-" + accentColorHex + ".png";
        Path filePath = Paths.get(folderPath.toString() + "/" + fileName);

        if (Files.exists(filePath)){
            return filePath.toFile();
        }


        ResourceLocation toggleResourceLocation = new ResourceLocation("partlysaneskies", "textures/debug_gui_textures/toggle.png");

        IResource debugTexture = PartlySaneSkies.minecraft.getResourceManager().getResource(toggleResourceLocation);
        BufferedImage debugImage = TextureUtil.readBufferedImage(debugTexture.getInputStream());

        folderPath.toFile().mkdirs();
        filePath.toFile().createNewFile();


        ImageUtils.replaceColor(debugImage, PRIMARY_DEBUG_COLOR, primaryColor);
        ImageUtils.replaceColor(debugImage, SECONDARY_DEBUG_COLOR, secondaryColor);
        ImageUtils.replaceColor(debugImage, ACCENT_DEBUG_COLOR, accentColor);

        ImageUtils.saveImage(debugImage, filePath);


        return filePath.toFile();
    }


    public static class Theme {
        private final String name;
        private final Color primaryColor;
        private final Color secondaryColor;
        private final Color defaultAccentColor;

        public Theme(String name, Color primaryColor, Color secondaryColor, Color defaultAccentColor) {
            this.name = name;
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            this.defaultAccentColor = defaultAccentColor;
        }

        public String getName() {
            return name;
        }
        public Color getPrimaryColor() {
            return primaryColor;
        }

        public Color getSecondaryColor() {
            return secondaryColor;
        }

        public Color getDefaultAccentColor() {
            return defaultAccentColor;
        }
    }
}
