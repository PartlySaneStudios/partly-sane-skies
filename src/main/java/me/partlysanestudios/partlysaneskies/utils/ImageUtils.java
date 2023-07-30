//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;


public class ImageUtils {

    //    Save all images in ./config/partly-sane-skies/image_variants/{name of base image}/{name of coloured image}
    private static final String IMAGE_SAVE_PATH = "./config/partly-sane-skies/image_variants/";


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
