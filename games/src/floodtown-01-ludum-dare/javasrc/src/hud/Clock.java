package hud;

import graphics.SpriteLoader;
import graphics.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Clock extends GameObject {

    int graphic = 0;
    BufferedImage sprite;
    private final int CLOCK_SIZE = 512;
    private final int SPACE_LEFT_BY_BOARD = 290;

    public Clock () {
        sprite = SpriteLoader.load(SpriteLoader.clock0);
        SpriteLoader.load(SpriteLoader.clock3);
        SpriteLoader.load(SpriteLoader.clock6);
        SpriteLoader.load(SpriteLoader.clock9);
        xPos = (SPACE_LEFT_BY_BOARD - CLOCK_SIZE)/2;
        yPos = -150;
        width = CLOCK_SIZE;
        height = CLOCK_SIZE;
    }

    public void nextGraphic() {
        graphic += 3;
        graphic %= 12;
        if (graphic == 0) {
            sprite = SpriteLoader.load(SpriteLoader.clock0);
        }
        if (graphic == 3) {
            sprite = SpriteLoader.load(SpriteLoader.clock3);
        }
        if (graphic == 6) {
            sprite = SpriteLoader.load(SpriteLoader.clock6);
        }
        if (graphic == 9) {
            sprite = SpriteLoader.load(SpriteLoader.clock9);
        }
    }

    @Override
    public void onHover(Graphics2D graphics2D) {

    }


    @Override
    public void render(Graphics2D graphics2D) {
        graphics2D.drawImage(sprite, xPos, yPos, null);
    }

}
