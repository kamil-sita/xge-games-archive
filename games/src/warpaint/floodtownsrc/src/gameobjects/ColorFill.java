package gameobjects;

import interactions.GameObject;
import system.Settings;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ColorFill extends GameObject {


    Color color;
    boolean visible = true;
    double rotation = 0;

    public ColorFill(Color color) {
        this(color, 0, 0, Settings.windowWidth, Settings.windowHeight);
    }

    public ColorFill(Color color, int xPos, int yPos, int width, int height) {
        this.color = color;
        setXPos(xPos);
        setYPos(yPos);
        setWidth(width);
        setHeight(height);
        setInterestedInMouse(false);
    }

    @Override
    public void onHover() {

    }


    @Override
    public void render(Graphics2D graphics2D) {
        if (visible) {
            graphics2D.setColor(color);
            graphics2D.fillRect(getXPos(), getYPos(), getWidth(), getHeight());
        }
    }

}
