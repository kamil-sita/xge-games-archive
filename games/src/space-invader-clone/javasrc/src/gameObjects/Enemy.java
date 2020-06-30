package gameObjects;

import game.BaseGameplayStage;
import game.Position;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Enemy extends PhysicalEntity {

    public Enemy(BaseGameplayStage baseGameplayStage, Position position, double healthPoints, BufferedImage entityImage) {
        super(baseGameplayStage, position, healthPoints, entityImage);
    }

    public void update(PhysicalEntity player, ArrayList<Bullet> playerBullets) {
        additionalDebug = new StringBuilder();
        additionalDebug.append("Health points: ").append(String.format("%.1f", healthPoints));
        positionUpdate();
        damageUpdate(playerBullets);
        if (isBehind(player) || isColliding(player)) {
            damagePlayerBecauseColliding(player);
            destroy();
        }
        removeIfNeeded();
    }

    protected abstract void positionUpdate();

    protected final void damageUpdate(ArrayList<Bullet> bullets) {
        if (bullets == null) return;
        for (Bullet bullet : bullets) {
            if (isColliding(bullet)) {
                dealDamage(bullet.getDamage());
                bullet.setToDelete(true);
            }
        }
        if (runOutOfHealthPoints()) {
            destroy();
        }
    }

    protected final boolean isBehind(PhysicalEntity entity) {
        final double MAXIMUM_DISTANCE_FROM_PLAYER = 150;
        return entity.getPosition().y - position.y < -MAXIMUM_DISTANCE_FROM_PLAYER;
    }

    protected void damagePlayerBecauseColliding(PhysicalEntity player) {
        player.dealDamage(healthPoints);
        destroy();
    }

    protected boolean runOutOfHealthPoints() {
        return healthPoints <= 0;
    }

    @Override
    public boolean isSetToBeRemoved() {
        return toDelete;
    }
}
