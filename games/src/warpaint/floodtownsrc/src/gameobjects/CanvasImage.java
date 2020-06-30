package gameobjects;

import graphics.SpriteLoader;
import interactions.GameObject;
import system.Settings;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class CanvasImage extends NonInteractiveImage {


    public CanvasImage(String location) {
        super(SpriteLoader.loadCopy(location));
    }

    public void paintCanvas(BufferedImage bufferedImage, double x, double y) {
        Graphics2D graphics2D = (Graphics2D) sprite.getGraphics();
        Settings.applySettings(graphics2D);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(x, y);
        graphics2D.drawImage(bufferedImage, affineTransform, null);
        graphics2D.dispose();
    }


}
