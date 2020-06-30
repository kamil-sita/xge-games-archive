package gameobjects;

import interactions.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class NonInteractiveImage extends GameObject {

    BufferedImage sprite;
    boolean visible = true;
    double rotation = 0;

    public NonInteractiveImage(BufferedImage sprite) {
        if (sprite == null) System.out.println("?????????");
        this.sprite = sprite;
        this.setWidth(sprite.getWidth());
        this.setHeight(sprite.getHeight());
    }

    @Override
    public void onHover() {

    }


    @Override
    public void render(Graphics2D graphics2D) {
        if (visible) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.translate(getXPos(), getYPos());
            affineTransform.rotate(Math.toRadians(rotation), getXPos() + getWidth() /2.0, getYPos() + getHeight() /2.0);
            graphics2D.drawImage(sprite, affineTransform, null);
        }
    }


    public NonInteractiveImage setPosition (int x, int y) {
        setXPos(x);
        setYPos(y);
        return this;
    }


    public NonInteractiveImage setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}
