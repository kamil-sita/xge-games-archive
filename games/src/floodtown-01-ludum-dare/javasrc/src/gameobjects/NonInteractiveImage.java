package gameobjects;

import graphics.GameObject;
import main.MouseListener;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class NonInteractiveImage extends GameObject {

    BufferedImage sprite;
    boolean visible = true;
    double rotation = 0;

    public NonInteractiveImage(BufferedImage sprite) {
        this.sprite = sprite;
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
    }

    @Override
    public void onHover(Graphics2D graphics2D) {

    }


    @Override
    public void render(Graphics2D graphics2D) {
        if (visible) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.translate(xPos, yPos);
            affineTransform.rotate(Math.toRadians(rotation), xPos + width/2.0, yPos + height/2.0);
            graphics2D.drawImage(sprite, affineTransform, null);
        }
    }

    @Override
    public boolean isMouseOver(MouseListener mouseListener) {
        return false;
    }


    public NonInteractiveImage setPosition (int x, int y) {
        xPos = x;
        yPos = y;
        return this;
    }


    public NonInteractiveImage setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}
