package graphics;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class FlyweightGraphicsLoader {

    public static final String playerShip = "/entities/shipnormal.png";
    public static final String star = "/entities/star.png";
    public static final String blast = "/fx/blast.png";
    public static final String asteroid = "/entities/asteroid.png";

    private static HashMap<String, BufferedImage> graphics = new HashMap<>();

    public static BufferedImage getFileFrom(String location) {
        if (graphics.get(location) == null) {
            graphics.put(location, ResourceIO.loadResource(location));
        }
        return graphics.get(location);
    }
}
