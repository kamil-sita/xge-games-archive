package gameobjects;

import game.StageManager;
import graphics.GameObject;
import sound.MusicPlayer;
import sound.SoundEffect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Button extends GameObject {

    BufferedImage sprite;
    double scaling;
    int yHoverOffset = 0;

    public Button(BufferedImage sprite, int xPos, int yPos, double scaling) {
        this.sprite = sprite;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = (int) (sprite.getWidth() * scaling);
        this.height = (int) (sprite.getHeight() * scaling);
        this.scaling = scaling;
    }

    boolean lastHovered = false;

    @Override
    public void onHover(Graphics2D graphics2D) {
        if (!lastHovered) {
            MusicPlayer.play(SoundEffect.hover);
            lastHovered = true;
        }
        yHoverOffset = -5;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(xPos, yPos + yHoverOffset);
        affineTransform.scale(scaling, scaling);
        graphics2D.drawImage(sprite, affineTransform, null);
        yHoverOffset = 0;
        if (!isMouseOver(StageManager.getMouseListener())) {
            lastHovered = false;
        }
    }
}
