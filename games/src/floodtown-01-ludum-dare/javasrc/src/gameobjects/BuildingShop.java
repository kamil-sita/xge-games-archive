package gameobjects;

import gameobjects.BuildingType;
import graphics.GameObject;
import sound.MusicPlayer;
import sound.SoundEffect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BuildingShop extends GameObject {

    BufferedImage sprite;
    BuildingType buildingType;
    double scaling = 0.25;

    int bigWidth;
    int bigHeight;

    int yHoverOffset = 0;

    public BuildingShop(BufferedImage sprite, int xPos, int yPos, BuildingType buildingType) {
        this.sprite = sprite;
        this.xPos = xPos;
        this.yPos = yPos;
        this.buildingType = buildingType;
        width = (int) (sprite.getWidth() * scaling);
        height = (int) (sprite.getHeight() * scaling);
        select();
        bigWidth = (int) (sprite.getWidth() * scaling);
        bigHeight = (int) (sprite.getHeight() * scaling);
        deselect();
    }

    @Override
    public void onHover(Graphics2D graphics2D) {
        yHoverOffset = -3;
    }

    public BuildingType getBuilding() {
        select();
        return buildingType;
    }

    public void deselect() {
        scaling = 0.25;
    }

    public void select() {
        MusicPlayer.play(SoundEffect.hover);
        scaling = 0.32;
    }


    @Override
    public void render(Graphics2D graphics2D) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(xPos - 14 , yPos - 14 + yHoverOffset);
        affineTransform.scale(scaling, scaling);
        affineTransform.translate(14, 14);
        graphics2D.drawImage(sprite, affineTransform, null);
        yHoverOffset = 0;
    }
}
