package gameobjects;

import graphics.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class IconAndText extends GameObject {

    String text;
    BufferedImage sprite;
    double scaling = 0.25;
    boolean visible = true;

    public IconAndText(String text, BufferedImage sprite, int xPos, int yPos) {
        this.text = text;
        this.sprite = sprite;
        this.xPos = xPos;
        this.yPos = yPos;
        if (sprite == null) {
            width = 0;
            height = 0;
        } else {
            width = sprite.getWidth();
            height = sprite.getHeight();
        }

    }

    @Override
    public void onHover(Graphics2D graphics2D) {

    }


    public void setText(String text) {
        this.text = text;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public IconAndText setScaling(double scaling) {
        this.scaling = scaling;
        return this;
    }

    public IconAndText setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        if (visible) {
            if (sprite != null) {
                AffineTransform affineTransform = new AffineTransform();
                affineTransform.translate(xPos, yPos);
                affineTransform.scale(scaling, scaling);
                graphics2D.drawImage(sprite, affineTransform, null);
            }
            graphics2D.setFont(new Font("Arial", Font.BOLD, 20));
            graphics2D.setColor(new Color(230, 230, 230));
            graphics2D.drawString(text, (int) (xPos + width * scaling + 5), (int) (yPos + scaling* 0.5 * height + 10));
        }
    }
}
