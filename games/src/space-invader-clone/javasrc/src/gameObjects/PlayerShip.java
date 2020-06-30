package gameObjects;

import game.GameplayStage;
import game.Position;
import main.KeyListener;
import main.KeyListenerBuilder;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public final class PlayerShip extends PhysicalEntity {

    protected double maxHealth;
    private KeyListener keyListener;
    private boolean playerHasControl = false;

    private double xSpeed = 0;
    private double ySpeed = -5;

    private ArrayList<Weapon> weapons = new ArrayList<>();

    private ArrayList<Entity> removablePhysicalEntities;

    public PlayerShip(GameplayStage gameplayStage, Position position, double maxHealthPoints, BufferedImage entityImage, ArrayList<Entity> enginesEntities, KeyListener keyListener) {
        super(gameplayStage, position, maxHealthPoints, entityImage);
        this.maxHealth = maxHealthPoints;
        this.removablePhysicalEntities = enginesEntities;
        this.keyListener = keyListener;
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public void update() {

        processUserInput();
        position.y += ySpeed;
        position.x += xSpeed;
    }

    private void processUserInput() {
        //todo changeable
        final int FIRING_BUTTON = KeyEvent.VK_SPACE;
        final int MOVING_LEFT_BUTTON = KeyEvent.VK_A;
        final int MOVING_RIGHT_BUTTON = KeyEvent.VK_D;


        boolean moving = false;
        if (!playerHasControl) return;
        if (KeyListenerBuilder.get(keyListener).isKeyPressed(MOVING_LEFT_BUTTON).andNot(MOVING_RIGHT_BUTTON).evaluate()) {
            xSpeed = -10;
            moving = true;
        }
        if (KeyListenerBuilder.get(keyListener).isKeyPressed(MOVING_RIGHT_BUTTON).andNot(MOVING_LEFT_BUTTON).evaluate()) {
            xSpeed = 10;
            moving = true;
        }
        if (!moving) {
            final double slowdownCoefficient = 1.075;
            xSpeed /= slowdownCoefficient;
        }
        if (KeyListenerBuilder.get(keyListener).isKeyPressed(FIRING_BUTTON).evaluate()) {
            fireWeapons();
        }
    }

    private void fireWeapons() {
        for (Weapon weapon : weapons) {
            var bullet = weapon.spawnBullet(new Position(position.x, position.y));
            if (bullet == null) continue;
            bullet.setVisible(true);
            removablePhysicalEntities.add(bullet);
        }
    }

    public void setPlayerHasControl(boolean control) {
        playerHasControl = control;
    }

    public double getMaxHealth() {
        return maxHealth;
    }
}
