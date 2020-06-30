package gameObjects;

import game.BaseGameplayStage;
import game.Position;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PhysicalEntity extends Entity {

    protected double healthPoints;
    protected boolean toDelete = false;

    public PhysicalEntity(BaseGameplayStage baseGameplayStage, Position position, double healthPoints, BufferedImage entityImage) {
        super(baseGameplayStage, position);
        setEntityImage(entityImage);
        this.healthPoints = healthPoints;
    }

    public PhysicalEntity(BaseGameplayStage baseGameplayStage, Position position, double healthPoints) {
        super(baseGameplayStage, position);
        this.healthPoints = healthPoints;
    }

    public boolean isColliding(PhysicalEntity entity) {
        if (this.isVisible && entity.isVisible) {
            return getCollisionRectangle().intersects(entity.getCollisionRectangle());
        }
        return false;
    }

    private double collisionScalling = 1.0;

    public Rectangle getCollisionRectangle() {
        var startX = (int) (this.getPosition().x - entityImage.getHeight()/2 * collisionScalling);
        var startY = (int) (this.getPosition().y - entityImage.getHeight()/2 * collisionScalling);
        var width = (int) (this.entityImage.getWidth() * collisionScalling);
        var height = (int) (this.entityImage.getHeight() * collisionScalling);
        return new Rectangle(startX, startY, width, height);
    }

    public void setCollisionScaling(double scaling) {
        collisionScalling = scaling;
    }

    public double getHealthPoints() {
        return healthPoints;
    }

    public boolean isSetToBeRemoved() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public void dealDamage(double damage) {
        healthPoints -= damage;
    }

    protected void destroy() {
        toDelete = true;
        this.setVisible(false);
    }
}
