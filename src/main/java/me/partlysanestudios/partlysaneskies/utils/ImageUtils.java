package me.partlysanestudios.partlysaneskies.utils;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ImageUtils {

    private static final Color MAIN_BACKGROUND_COLOR = new Color(255, 0, 0);
    private static final Color SECONDARY_BACKGROUND_COLOR = new Color(0, 0, 255);
    private static final Color ACCENT_BACKGROUND_COLOR = new Color(0, 255, 0);

//    Save all images in ./config/partly-sane-skies/image_variants/{name of base image}/{name of coloured image}
    private static final String IMAGE_SAVE_PATH = "./config/partly-sane-skies/image_variants/";
    public static File getBackgroundWithColor(Color mainBackgroundColor, Color secondaryBackgroundColor, Color accentBackgroundColor) throws IOException {


        String mainBackgroundColorHex = Integer.toHexString(mainBackgroundColor.getRGB() & 0xffffff);
        String secondaryBackgroundColorHex = Integer.toHexString(secondaryBackgroundColor.getRGB() & 0xffffff);
        String accentBackgroundColorHex = Integer.toHexString(accentBackgroundColor.getRGB() & 0xffffff);

        Path folderPath = Paths.get("./config/partly-sane-skies/image_variants/background");
        String fileName = "background-" + mainBackgroundColorHex + "-" + secondaryBackgroundColorHex + "-" + accentBackgroundColorHex + ".png";
        Path filePath = Paths.get(folderPath.toString() + "/" + fileName);

        if (Files.exists(filePath)){
            return filePath.toFile();
        }


        ResourceLocation backgroundResourceLocation = new ResourceLocation("partlysaneskies", "textures/gui/debug_gui_textures/background.png");

        IResource debugTexture = PartlySaneSkies.minecraft.getResourceManager().getResource(backgroundResourceLocation);
        BufferedImage debugImage = TextureUtil.readBufferedImage(debugTexture.getInputStream());

        folderPath.toFile().mkdirs();
        filePath.toFile().createNewFile();


        replaceColor(debugImage, ImageUtils.MAIN_BACKGROUND_COLOR, mainBackgroundColor);
        replaceColor(debugImage, ImageUtils.SECONDARY_BACKGROUND_COLOR, secondaryBackgroundColor);
        replaceColor(debugImage, ImageUtils.ACCENT_BACKGROUND_COLOR, accentBackgroundColor);

        saveImage(debugImage, filePath);



        return filePath.toFile();
    }



    public static void saveImage(BufferedImage image, Path path) throws IOException {
        File output = path.toFile();
        ImageIO.write(image , "PNG", output);
    }

    public static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    public static BufferedImage replaceColor(BufferedImage image, Color oldColor, Color newColor) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int intColor = image.getRGB(x, y);
                Color pixelColor = new Color(intColor);

                if (pixelColor.equals(oldColor)) {
                    image.setRGB(x, y, newColor.getRGB());
                }
            }
        }

        return image;
    }
}
