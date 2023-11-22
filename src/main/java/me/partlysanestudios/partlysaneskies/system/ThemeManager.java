//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.system;

import cc.polyfrost.oneconfig.config.core.OneColor;
import gg.essential.elementa.components.UIImage;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils;
import me.partlysanestudios.partlysaneskies.utils.ImageUtils;
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
import java.util.ArrayList;

public class ThemeManager {

    private static final Color PRIMARY_DEBUG_COLOR = new Color(255, 0, 0);
    private static final Color SECONDARY_DEBUG_COLOR = new Color(0, 0, 255);
    private static final Color ACCENT_DEBUG_COLOR = new Color(0, 255, 0);
    private static String lastThemeName = "";

    private static final ArrayList<UIImage> backgroundUIImages = new ArrayList<>();
    private static final ArrayList<ButtonData> buttonDataList = new ArrayList<>();
    private static final ArrayList<ToggleData> toggleDataList = new ArrayList<>();

    public static Theme[] defaultThemes = {
            new Theme("Classic (Dark)",  new Color(46, 47, 50), new Color(37, 38, 41), new Color(1, 255, 255)),
            new Theme("Royal Dark (Dark)", new Color(46, 47, 50), new Color(39, 31, 43), new Color(91, 192, 235)),
            new Theme("Midnight Forest (Dark)", new Color(46, 47, 50),  new Color(40, 50, 38), new Color(35, 206, 107)),

            new Theme("Moonless Night (Very Dark)", new Color(24, 24, 27), new Color(15, 17, 20), new Color(8, 124, 167)),
            new Theme("Stormy Night (Very Dark)", new Color(23, 23, 34), new Color(5, 5, 27), new Color(94, 74, 227)),
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
            PartlySaneSkies.config.accentColor = getAccentColor();
        }

        if (!PartlySaneSkies.config.customTheme) {
            PartlySaneSkies.config.primaryColor = getPrimaryColor();
            PartlySaneSkies.config.secondaryColor = getSecondaryColor();
        }

//        If the theme has changed
        if (!lastThemeName.equals(defaultThemes[PartlySaneSkies.config.themeIndex].getName())) {
            for (UIImage image : backgroundUIImages) {
                try {
                    image.applyTexture(new ReleasedDynamicTexture(ImageUtils.INSTANCE.loadImage(getCurrentBackgroundFile().getPath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (ButtonData data : buttonDataList) {
                try {
                    OneColor color;
                    if (data.getColor() == null) {
                        color = getAccentColor();
                    } else {
                        color = data.getColor();
                    }

                    data.getImage().applyTexture(new ReleasedDynamicTexture(ImageUtils.INSTANCE.loadImage(getCurrentButtonFile(color).getPath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (ToggleData data : toggleDataList) {
                try {
                    OneColor color;
                    if (data.getColor() == null) {
                        color = getAccentColor();
                    } else {
                        color = data.getColor();
                    }

                    data.getImage().applyTexture(new ReleasedDynamicTexture(ImageUtils.INSTANCE.loadImage(getCurrentToggleFile(data.isSelected(), color).getPath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            lastThemeName = defaultThemes[PartlySaneSkies.config.themeIndex].getName();
        }


    }


    public static UIImage getCurrentBackgroundUIImage() {
        UIImage image;
        if (PartlySaneSkies.config.disableThemes) {
            image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_background.png"));

        }
        else {
            try {
                image = UIImage.ofFile(getCurrentBackgroundFile());
            } catch (IOException e) {
                image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_background.png"));
            }
        }

        backgroundUIImages.add(image);
        return image;
    }

    public static UIImage getCurrentButtonUIImage() {
        return getCurrentButtonUIImage(getAccentColor());
    }

    public static UIImage getCurrentButtonUIImage(OneColor accentColor) {
        UIImage image;
        if (PartlySaneSkies.config.disableThemes) {
            if (accentColor.equals(getAccentColor())) {
                image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_button.png"));
            } else {
                image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_button_transparent.png"));
            }
        } else {
            try {
                image = UIImage.ofFile(getCurrentButtonFile(accentColor));
            } catch (IOException e) {
                if (accentColor.equals(getAccentColor())) {
                    image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_button.png"));
                } else {
                    image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/base_color_button_transparent.png"));
                }
            }
        }

        if (accentColor.equals(getAccentColor())) {
            accentColor = null;
        }

        buttonDataList.add(new ButtonData(image, accentColor));
        return image;
    }


    public static UIImage getCurrentToggleUIImage(boolean selected) {
        return getCurrentToggleUIImage(selected, getAccentColor());
    }

    public static UIImage getCurrentToggleUIImage(boolean selected, OneColor accentColor) {
        UIImage image;
        if (PartlySaneSkies.config.disableThemes) {
            if (selected) {
                image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies" , "textures/gui/selected_toggle.png"));
            } else {
                image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies" , "textures/gui/unselected_toggle.png"));
            }
        }
        try {
            image = UIImage.ofFile(getCurrentToggleFile(selected, accentColor));
        } catch (IOException e) {
            if (selected) {
                image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies" , "textures/gui/selected_toggle.png"));
            } else {
                image = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies" , "textures/gui/unselected_toggle.png"));
            }
        }

        if (accentColor.equals(getAccentColor())) {
            accentColor = null;
        }

        toggleDataList.add(new ToggleData(image, selected, accentColor));
        return image;
    }

    public static File getCurrentBackgroundFile() throws IOException {
        return getBackgroundWithColor(getPrimaryColor(), getSecondaryColor(), getAccentColor());
    }

    public static File getCurrentButtonFile() throws IOException {
        return getCurrentButtonFile(getAccentColor());
    }

    public static File getCurrentButtonFile(OneColor accentColor) throws IOException {
        return getButtonWithColor(getPrimaryColor(), getSecondaryColor(), accentColor);
    }

    public static File getCurrentToggleFile(boolean selected) throws IOException {
        return getCurrentToggleFile(selected, getAccentColor());
    }

    public static File getCurrentToggleFile(boolean selected, OneColor accentColor) throws IOException {
        if (selected) {
            return getToggleWithColor(getPrimaryColor(), getSecondaryColor(), accentColor);
        } else {
            return getToggleWithColor(getPrimaryColor(), getSecondaryColor(), getSecondaryColor());
        }
    }

    public static OneColor getPrimaryColor() {
        if (!PartlySaneSkies.config.customTheme) {
            int themeIndex = PartlySaneSkies.config.themeIndex;
            return new OneColor(defaultThemes[themeIndex].getPrimaryColor());
        } else {
            return PartlySaneSkies.config.primaryColor;
        }
    }

    public static OneColor getDarkPrimaryColor() {
        return new OneColor(darkenColor(getPrimaryColor()));
    }

    public static OneColor getLightPrimaryColor() {
        return new OneColor(lightenColor(getPrimaryColor()));
    }

    public static OneColor getSecondaryColor() {
        if (!PartlySaneSkies.config.customTheme) {
            int themeIndex = PartlySaneSkies.config.themeIndex;
            return new OneColor(defaultThemes[themeIndex].getSecondaryColor());
        } else {
            return PartlySaneSkies.config.secondaryColor;
        }
    }

    public static OneColor getDarkSecondaryColor() {
        return new OneColor(darkenColor(getSecondaryColor()));
    }

    public static OneColor getLightSecondaryColor() {
        return new OneColor(lightenColor(getSecondaryColor()));
    }

    public static OneColor getAccentColor() {
        if (PartlySaneSkies.config.useDefaultAccentColor) {
            int themeIndex = PartlySaneSkies.config.themeIndex;
            return new OneColor(defaultThemes[themeIndex].getDefaultAccentColor());
        } else {
            return PartlySaneSkies.config.accentColor;
        }
    }

    public static Color getDarkAccentColor() {
        return darkenColor(getAccentColor());
    }

    public static Color getLightAccentColor() {
        return lightenColor(getAccentColor());
    }

    private static Color darkenColor(OneColor color) {
        return darkenColor(color.toJavaColor());
    }
    private static Color darkenColor(Color color) {
        int averageR = (int) (color.getRed()* .761);
        int averageG = (int) (color.getGreen() * .761);
        int averageB = (int) (color.getBlue() * .761);

        return new Color(averageR, averageG, averageB, color.getTransparency());
    }


    private static Color lightenColor(OneColor color) {
        return lightenColor(color.toJavaColor());
    }

    private static Color lightenColor(Color color) {
        int averageR = (int) (color.getRed()* .798 + 255 * .202);
        int averageG = (int) (color.getGreen() * .798 + 255 * .202);
        int averageB = (int) (color.getBlue() * .798 + 255 * .202);

        return new Color(averageR, averageG, averageB, color.getTransparency());
    }

    public static File getBackgroundWithColor(OneColor primaryColor, OneColor secondaryColor, OneColor accentColor) throws IOException {


        String primaryColorHex = Integer.toHexString(primaryColor.getRGB() & 0xffffff);
        String secondaryColorHex = Integer.toHexString(secondaryColor.getRGB() & 0xffffff);
        String accentColorHex = Integer.toHexString(accentColor.getRGB() & 0xffffff);

        Path folderPath = Paths.get("./config/partly-sane-skies/image_variants/background");
        String fileName = "background-" + primaryColorHex + "-" + secondaryColorHex + "-" + accentColorHex + ".png";
        Path filePath = Paths.get(folderPath + "/" + fileName);

        if (Files.exists(filePath)){
            return filePath.toFile();
        }


        ResourceLocation backgroundResourceLocation = new ResourceLocation("partlysaneskies", "textures/debug_gui_textures/background.png");

        IResource debugTexture = PartlySaneSkies.minecraft.getResourceManager().getResource(backgroundResourceLocation);
        BufferedImage debugImage = TextureUtil.readBufferedImage(debugTexture.getInputStream());

        folderPath.toFile().mkdirs();
        filePath.toFile().createNewFile();


        ImageUtils.INSTANCE.replaceColor(debugImage, PRIMARY_DEBUG_COLOR, primaryColor);
        ImageUtils.INSTANCE.replaceColor(debugImage, SECONDARY_DEBUG_COLOR, secondaryColor);
        ImageUtils.INSTANCE.replaceColor(debugImage, ACCENT_DEBUG_COLOR, accentColor);

        ImageUtils.INSTANCE.saveImage(debugImage, filePath);



        return filePath.toFile();
    }

    public static File getButtonWithColor(OneColor primaryColor, OneColor secondaryColor, OneColor accentColor) throws IOException {


        String primaryHex = Integer.toHexString(primaryColor.getRGB() & 0xffffff);
        String secondaryColorHex = Integer.toHexString(secondaryColor.getRGB() & 0xffffff);
        String accentColorHex = Integer.toHexString(accentColor.getRGB() & 0xffffff);

        Path folderPath = Paths.get("./config/partly-sane-skies/image_variants/button");
        String fileName = "button-" + primaryHex + "-" + secondaryColorHex + "-" + accentColorHex + ".png";
        Path filePath = Paths.get(folderPath + "/" + fileName);

        if (Files.exists(filePath)){
            return filePath.toFile();
        }


        ResourceLocation buttonResourceLocation = new ResourceLocation("partlysaneskies", "textures/debug_gui_textures/button.png");

        IResource debugTexture = PartlySaneSkies.minecraft.getResourceManager().getResource(buttonResourceLocation);
        BufferedImage debugImage = TextureUtil.readBufferedImage(debugTexture.getInputStream());

        folderPath.toFile().mkdirs();
        filePath.toFile().createNewFile();


        ImageUtils.INSTANCE.replaceColor(debugImage, PRIMARY_DEBUG_COLOR, primaryColor);
        ImageUtils.INSTANCE.replaceColor(debugImage, SECONDARY_DEBUG_COLOR, secondaryColor);
        ImageUtils.INSTANCE.replaceColor(debugImage, ACCENT_DEBUG_COLOR, accentColor);

        ImageUtils.INSTANCE.saveImage(debugImage, filePath);



        return filePath.toFile();
    }

    public static File getToggleWithColor(OneColor primaryColor, OneColor secondaryColor, OneColor accentColor) throws IOException {


        String primaryColorHex = Integer.toHexString(primaryColor.getRGB() & 0xffffff);
        String secondaryColorHex = Integer.toHexString(secondaryColor.getRGB() & 0xffffff);
        String accentColorHex = Integer.toHexString(accentColor.getRGB() & 0xffffff);

        Path folderPath = Paths.get("./config/partly-sane-skies/image_variants/toggle");
        String fileName = "toggle-" + primaryColorHex + "-" + secondaryColorHex + "-" + accentColorHex + ".png";
        Path filePath = Paths.get(folderPath + "/" + fileName);

        if (Files.exists(filePath)){
            return filePath.toFile();
        }


        ResourceLocation toggleResourceLocation = new ResourceLocation("partlysaneskies", "textures/debug_gui_textures/toggle.png");

        IResource debugTexture = PartlySaneSkies.minecraft.getResourceManager().getResource(toggleResourceLocation);
        BufferedImage debugImage = TextureUtil.readBufferedImage(debugTexture.getInputStream());

        folderPath.toFile().mkdirs();
        filePath.toFile().createNewFile();


        ImageUtils.INSTANCE.replaceColor(debugImage, PRIMARY_DEBUG_COLOR, primaryColor);
        ImageUtils.INSTANCE.replaceColor(debugImage, SECONDARY_DEBUG_COLOR, secondaryColor);
        ImageUtils.INSTANCE.replaceColor(debugImage, ACCENT_DEBUG_COLOR, accentColor);

        ImageUtils.INSTANCE.saveImage(debugImage, filePath);


        return filePath.toFile();
    }

    public static class ButtonData {
        private final UIImage image;
        private final OneColor color;
        public ButtonData(UIImage image) {
            this.image = image;
            this.color = null;
        }
        public ButtonData(UIImage image, OneColor color) {
            this.image = image;
            this.color = color;
        }

        public UIImage getImage() {
            return image;
        }

        public OneColor getColor() {
            return color;
        }
    }

    public static class ToggleData {
        private final UIImage image;
        private final OneColor color;
        private final boolean selected;
        public ToggleData(UIImage image, boolean selected) {
            this.image = image;
            this.color = null;
            this.selected = selected;
        }

        public ToggleData(UIImage image, boolean selected, OneColor color) {
            this.image = image;
            this.color = color;
            this.selected = selected;
        }

        public UIImage getImage() {
            return image;
        }

        public OneColor getColor() {
            return color;
        }

        public boolean isSelected() {
            return selected;
        }
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
