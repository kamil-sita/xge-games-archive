package gameObjects;

import game.BasicGameLogic;
import game.BaseGameplayStage;
import game.Position;
import graphics.RenderPriority;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet extends PhysicalEntity {

    private double speed;
    private long initTime;
    private double damage;

    public Bullet (BaseGameplayStage baseGameplayStage, Position position, BufferedImage entityImage, double speed, double damage) {
        super(baseGameplayStage, position, 1, entityImage);
        initTime = System.nanoTime();
        this.speed = speed;
        this.damage = damage;
    }

    public void update() {
        move();
        if (isLivingTooLong()) {
            toDelete = true;
            isVisible = false;
        }
        removeIfNeeded();
    }

    public void move() {
        position.y -= speed;
    }

    public boolean isLivingTooLong() {
        final double LIFE_TIME = 5;
        return System.nanoTime() / BasicGameLogic.NANOSECONDS_IN_SECOND > LIFE_TIME + initTime / BasicGameLogic.NANOSECONDS_IN_SECOND;
    }

    @Override
    protected void debugRender(Graphics2D graphics2D, int posX, int posY) {
        //rendering debug for bullets is almost useless
    }

    public double getDamage() {
        return damage;
    }

    public Bullet getCopy() {
        var position = new Position();
        this.position.copyPositionOnto(position);
        return new Bullet(baseGameplayStage, position, this.entityImage, this.speed, this.damage);
    }

    public RenderPriority getRenderPriority() {
        return RenderPriority.foregroundBottom;
    }

}
