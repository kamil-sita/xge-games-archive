package gameobjects;

import game.Sprites;
import system.Settings;
import graphics.SpriteLoader;
import graphics.transforms.CacheableTransforms;
import interactions.GameObject;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WaterFallEffect extends GameObject {

    final int NUMBER_OF_WAVES = 26;

    private ArrayList<WaterFall> waterFalls = null;

    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        if (waterFalls == null) {
            waterFalls = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_WAVES; i++) {
                waterFalls.add(new WaterFall(1 + Math.random(), 325 + 25.0 * i));
            }
        }
        for (WaterFall waterFall : waterFalls) {
            waterFall.render(graphics2D);
        }
    }

    private class WaterFall extends GameObject {

        private double speed;
        private ArrayList<Water> waters = null;
        private double xPos;

        private double i = 1;
        private double iIncrement = 0.01 + Math.random() * 0.02;

        public WaterFall(double speed, double xPos) {
            this.speed = speed;
            this.xPos = xPos;
        }

        @Override
        public void render(@NotNull Graphics2D graphics2D) {
            //speed += (Math.random() - 0.5)/10.0;
            speed += Math.sin(i) * 0.002;
            i += iIncrement;
            if (speed < 1.0) {
                speed = 1.0;
            } else  if (speed > 4.0) {
                speed = 4.0;
            }
            if (waters == null) {
                waters = new ArrayList<>();
                for (int i = 0; i < 40; i++) {
                    waters.add(new Water(i * 25.0, xPos));
                }
            }
            for (Water water : waters) {
                water.move(speed);
                if (water.yPos >= 720) {
                    water.yPos -= 1000;
                }
                water.render(graphics2D);
            }
        }

        private class Water extends GameObject {

            double yPos;
            double xPos;

            public Water(double yPos, double xPos) {
                this.yPos = yPos;
                this.xPos = xPos;
            }

            public void move(double speed) {
                yPos += speed;
            }

            @Override
            public void render(@NotNull Graphics2D graphics2D) {
                BufferedImage sprite = CacheableTransforms.INSTANCE.scale(SpriteLoader.load(Sprites.underWater), 0.1, CacheableTransforms.Quality.Best);
                AffineTransform affineTransform = new AffineTransform();
                affineTransform.translate(xPos, yPos);
                graphics2D.drawImage(sprite, affineTransform, null);
            }
        }
    }
}
