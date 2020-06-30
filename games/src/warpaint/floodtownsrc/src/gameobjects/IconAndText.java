package gameobjects;

import interactions.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class IconAndText extends GameObject {

    String text;
    BufferedImage sprite;
    double scaling = 0.25;
    boolean visible = true;
    Color color = new Color(230, 230, 230);

    public IconAndText(String text, BufferedImage sprite, int xPos, int yPos) {
        this.text = text;
        this.sprite = sprite;
        this.setXPos(xPos);
        this.setYPos(yPos);
        if (sprite == null) {
            setWidth(0);
            setHeight(0);
        } else {
            setWidth(sprite.getWidth());
            setHeight(sprite.getHeight());
        }

    }

    @Override
    public void onHover() {

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
                affineTransform.translate(getXPos(), getYPos());
                affineTransform.scale(scaling, scaling);
                graphics2D.drawImage(sprite, affineTransform, null);
            }
            graphics2D.setFont(new Font("Arial", Font.BOLD, 20));
            graphics2D.setColor(color);
            graphics2D.drawString(text, (int) (getXPos() + getWidth() * scaling + 5), (int) (getYPos() + scaling* 0.5 * getHeight() + 10));
        }
    }

    public void setGreenColor() {
        this.color = new Color(130, 220, 130);
    }

    public void setRedColor() {
        this.color = new Color(220, 130, 130);
    }

    public void setLogicalColor(boolean log) {
        if (log) {
            setGreenColor();
        } else {
            setRedColor();
        }
    }
}
