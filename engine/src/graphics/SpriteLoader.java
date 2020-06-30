package graphics;


import org.jetbrains.annotations.NotNull;
import resourcemanaging.ResourceIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class SpriteLoader {
    private static HashMap<String, BufferedImage> graphics = new HashMap<>();
    private static GraphicsConfiguration graphicsConfiguration = null;

    public static BufferedImage load(String location) {
        if (graphics.get(location) == null) {

            BufferedImage loadedImage = ResourceIO.loadImg(location);
            loadedImage = optimizeLoadedImage(loadedImage);
            graphics.put(location, loadedImage);

        }
        return graphics.get(location);
    }

    @NotNull
    private static BufferedImage optimizeLoadedImage(BufferedImage loadedImage) {
        if (graphicsConfiguration == null) {
            graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        }
        if (!loadedImage.getColorModel().equals(graphicsConfiguration.getColorModel())) {
            //optimalization: https://stackoverflow.com/questions/196890/java2d-performance-issues
            System.out.println("Changing color model");
            BufferedImage newImage = graphicsConfiguration.createCompatibleImage(loadedImage.getWidth(), loadedImage.getHeight(), loadedImage.getTransparency());
            Graphics2D graphics2D = newImage.createGraphics();
            graphics2D.drawImage(loadedImage, 0, 0, null);
            graphics2D.dispose();
            loadedImage = newImage;
        }
        return loadedImage;
    }


    public static BufferedImage loadCopy(String location) {
        BufferedImage original = load(location);
        BufferedImage newImage = graphicsConfiguration.createCompatibleImage(original.getWidth(), original.getHeight(), original.getTransparency());
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.drawImage(original, 0, 0, null);
        graphics2D.dispose();
        return newImage;
    }


}
