package gameobjects;

import game.Sprites;
import game.UnitColor;
import graphics.SpriteLoader;
import interactions.GameObject;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class Bullet extends GameObject {

    Bullets bullets;

    double initialX;
    double initialY;

    double posX;
    double posY;

    double angle;
    double distanceToTravel;
    Unit targetUnit;
    UnitColor bulletColor;

    public Bullet(double posX, double posY, double angle, double distanceToTravel, Unit targetUnit, UnitColor bulletColor, Bullets bullets) {
        initialX = posX;
        initialY = posY;
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.distanceToTravel = distanceToTravel;
        this.targetUnit = targetUnit;
        this.bulletColor = bulletColor;
        this.bullets = bullets;
        bullets.add(this);
    }

    public void action() {
        double speed = 2.8;

        double dx = Math.sin(Math.toRadians(angle));
        double dy = - Math.cos(Math.toRadians(angle));
        posX += speed * dx;
        posY += speed * dy;

        if (distanceFromInitial() >= distanceToTravel) {
            targetUnit.dealDamage(2.0, bulletColor);
            BufferedImage bulletDecal = null;
            switch (bulletColor) {
                case red:
                    bulletDecal = SpriteLoader.load(Sprites.red);
                    break;
                case green:
                    bulletDecal = SpriteLoader.load(Sprites.green);
                    break;
                case blue:
                    bulletDecal = SpriteLoader.load(Sprites.blue);
                    break;
            }
            bullets.getGameplayStage().getBackground().paintCanvas(bulletDecal, posX, posY);
            bullets.remove(this);
        }
    }

    private double distanceFromInitial() {
        return sqrt(pow(posX - initialX, 2) + pow(posY - initialY, 2));
    }


    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        graphics2D.setColor(new Color(0, 0, 0, 32));
        graphics2D.drawRect((int) posX - 1, (int) posY - 1 + 7, 2, 2);
        graphics2D.setColor(bulletColor.getColor());
        graphics2D.drawRect((int) posX - 1, (int) posY - 1, 2, 2);
    }
}
