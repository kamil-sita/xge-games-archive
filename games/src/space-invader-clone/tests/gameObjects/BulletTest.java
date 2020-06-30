package gameObjects;

import game.Position;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {

    @Test
    void getcopy() {
        Camera c = new Camera();
        Bullet b = new Bullet(new CameraAndEntitiesContainer() {
            @Override
            public Camera getCamera() {
                return c;
            }

            ArrayList<Entity> entities = new ArrayList<>();

            @Override
            public ArrayList<Entity> getEntities() {
                return entities;
            }
        }, new Position(3, 3), new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), 1, 1);
        Bullet copy = b.getCopy();

        //copy should be different than original
        assertNotSame(copy, b);
        //but their positions should be the same
        assertEquals(b.position.x, copy.position.x);
        assertEquals(b.position.y, copy.position.y);
        //references should differ though
        assertNotEquals(b.getPosition(), copy.getPosition());
        //camera also should be the same
        assertEquals(b.camera, copy.camera);
        //their other fields should also be the same
        assertEquals(b.getEntityImage(), copy.getEntityImage());
        assertEquals(b.getDamage(), copy.getDamage());
        assertEquals(b.healthPoints, copy.healthPoints);
    }
}