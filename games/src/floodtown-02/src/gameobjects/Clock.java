package gameobjects;

import game.Sprites;
import graphics.SpriteLoader;
import interactions.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Clock extends GameObject {

    int graphic = 0;
    BufferedImage sprite;
    private final int CLOCK_SIZE = 512;
    private final int SPACE_LEFT_BY_BOARD = 290;

    public Clock () {
        sprite = SpriteLoader.load(Sprites.clock0);
        SpriteLoader.load(Sprites.clock3);
        SpriteLoader.load(Sprites.clock6);
        SpriteLoader.load(Sprites.clock9);
        setXPos((SPACE_LEFT_BY_BOARD - CLOCK_SIZE) / 2);
        setYPos(-150);
        setWidth(CLOCK_SIZE);
        setHeight(CLOCK_SIZE);
    }

    public void nextGraphic() {
        graphic += 3;
        graphic %= 12;
        if (graphic == 0) {
            sprite = SpriteLoader.load(Sprites.clock0);
        }
        if (graphic == 3) {
            sprite = SpriteLoader.load(Sprites.clock3);
        }
        if (graphic == 6) {
            sprite = SpriteLoader.load(Sprites.clock6);
        }
        if (graphic == 9) {
            sprite = SpriteLoader.load(Sprites.clock9);
        }
    }



    @Override
    public void render(Graphics2D graphics2D) {
        graphics2D.drawImage(sprite, getXPos(), getYPos(), null);
    }

}
