package graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class ResourceIO {
    public static BufferedImage loadResource(String location) {
        URL url = ResourceIO.class.getResource(location);
        System.out.println("Loading resource at: " + location);
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }
}
