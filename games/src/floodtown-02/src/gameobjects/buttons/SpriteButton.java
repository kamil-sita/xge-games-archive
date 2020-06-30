package gameobjects.buttons;

import interactions.GameObject;
import org.jetbrains.annotations.NotNull;
import sound.FloodtownMusicPlayer;
import sound.SoundEffect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class SpriteButton extends GameObject {

    BufferedImage sprite;
    double scaling;
    int yHoverOffset = 0;

    public SpriteButton(BufferedImage sprite, int xPos, int yPos, double scaling) {
        setInterestedInMouse(true);
        this.sprite = sprite;
        this.setXPos(xPos);
        this.setYPos(yPos);
        this.setWidth((int) (sprite.getWidth() * scaling));
        this.setHeight((int) (sprite.getHeight() * scaling));
        this.scaling = scaling;
    }

    @Override
    public void onHover() {
        FloodtownMusicPlayer.INSTANCE.play(SoundEffect.hover);
        yHoverOffset = -5;
    }

    @Override
    public void onDeHover() {
        yHoverOffset = 0;
    }

    public SpriteButton setClickAction(Runnable runnable) {
        setOnClickRun(runnable);
        return this;
    }

    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(getXPos(), getYPos() + yHoverOffset);
        affineTransform.scale(scaling, scaling);
        graphics2D.drawImage(sprite, affineTransform, null);
    }
}
