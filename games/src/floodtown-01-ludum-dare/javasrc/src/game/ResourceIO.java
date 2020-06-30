package game;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class ResourceIO {
    public static BufferedImage loadImg(String location) {
        URL url = ResourceIO.class.getResource(location);
        System.out.println("Loading img at: " + location);
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    public static AudioInputStream loadSound(String location) {
        URL url = ResourceIO.class.getResource(location);
        System.out.println("Loading sound at: " + location);
        try {
            return AudioSystem.getAudioInputStream(url);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }
}
