package gameobjects.buttons;

import game.BuildingType;
import game.Sprites;
import system.Settings;
import graphics.SimpleRenderer;
import graphics.SpriteLoader;
import graphics.transforms.CacheableTransforms;
import interactions.GameObject;
import org.jetbrains.annotations.NotNull;
import sound.FloodtownMusicPlayer;
import sound.SoundEffect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BuildingShop extends GameObject {

    BufferedImage sprite;
    BuildingType buildingType;
    double scaling = 0.25;

    int yHoverOffset = 0;

    public BuildingShop(int xPos, int yPos, BuildingType buildingType) {
        setInterestedInMouse(true);
        this.setXPos(xPos);
        this.setYPos(yPos);
        this.buildingType = buildingType;
        deselect();
        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());
    }

    @Override
    public void onHover() {
        yHoverOffset = -3;
    }

    @Override
    public void onDeHover() {
        yHoverOffset = 0;
    }


    public void deselect() {
        scaling = 0.25;
        updateSprite();
    }

    public void select() {
        FloodtownMusicPlayer.INSTANCE.play(SoundEffect.hover);
        scaling = 0.28;
        updateSprite();
    }

    @Override
    public void onClick() {
        if (getOnClickRun() != null) {
            getOnClickRun().run();
        }
        select();

    }

    private void updateSprite() {
        BufferedImage nonScaledSprite = null;
        switch (buildingType) {
            case empty:
                nonScaledSprite = SpriteLoader.load(Sprites.noSprite);
                break;
            case powerGenerator:
                nonScaledSprite = SpriteLoader.load(Sprites.coalPlant);
                break;
            case farm:
                nonScaledSprite = SpriteLoader.load(Sprites.farm);
                break;
            case housing:
                nonScaledSprite = SpriteLoader.load(Sprites.house);
                break;
            case factory:
                nonScaledSprite = SpriteLoader.load(Sprites.factory);
                break;
        }
        sprite = CacheableTransforms.INSTANCE.scale(nonScaledSprite,scaling, CacheableTransforms.Quality.Best);
    }

    public BuildingShop setClickAction(Runnable runnable) {
        setOnClickRun(runnable);
        return this;
    }

    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        updateSprite();
        SimpleRenderer.INSTANCE.renderImage(graphics2D, sprite, getXPos(), getYPos() + yHoverOffset, 1);
    }
}
