package gameObjects;

import game.Position;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {

    @Test
    public void enemyTesting() {
        Camera camera = new Camera();
        Position position = new Position(1, 1000);

        CameraAndEntitiesContainer cameraAndEntitiesContainer = CameraAndEntitiesContainerGenerator.generate(camera);

        Enemy e = new Enemy(cameraAndEntitiesContainer, position, 30, new BufferedImage(50, 50, 1)) {
            @Override
            protected void positionUpdate() {
                super.positionUpdate();
            }
        };

        //TESTING OF ISBEHINDPLAYER
        //before check enemy is not behind player
        assertFalse(e.isBehindPlayer());

        PhysicalEntity player = new PhysicalEntity(cameraAndEntitiesContainer, new Position(0, 0), 30, new BufferedImage(50, 50, 1));

        //after update enemy is behind player
        e.update(player, null);

        assertTrue(e.isBehindPlayer());
        assertTrue(e.isSetToBeRemoved());

        //TESTING OF DEALING DAMAGE TO ENEMY
        Bullet bulletNearEdge = new Bullet(cameraAndEntitiesContainer, new Position(position.x - 45, position.y + 45), new BufferedImage(10, 10, 1), 1, 12);
        Bullet bulletNearCenter = new Bullet(cameraAndEntitiesContainer, new Position(position.x, position.y), new BufferedImage(10, 10, 1), 1, 12);
        Bullet bulletFarFromEnemy = new Bullet(cameraAndEntitiesContainer, new Position(position.x - 1000, position.y + 1000), new BufferedImage(10, 10, 1), 1, 12); //this bullet won't hit enemy

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(bulletFarFromEnemy);
        bullets.add(bulletNearCenter);
        bullets.add(bulletNearEdge);

        //we need new enemy and player - one that will not make enemy fall behind
        e = new Enemy(cameraAndEntitiesContainer, position, 30, new BufferedImage(50, 50, 1)) {
            @Override
            protected void positionUpdate() {
                super.positionUpdate();
            }
        };
        player = new PhysicalEntity(cameraAndEntitiesContainer, position, 30, new BufferedImage(50, 50, 1));

        e.update(player, bullets);
        assertFalse(e.isBehindPlayer());
        assertFalse(e.isSetToBeRemoved()); //those bullets should not be enough to damage this enemy, but one more would do the trick

        bullets = new ArrayList<>();
        bullets.add(new Bullet(cameraAndEntitiesContainer, new Position(position.x - 2, position.y + 2), new BufferedImage(30, 30, 1), 3, 10));
        e.update(player, bullets);
        assertTrue(e.isSetToBeRemoved());
    }

}