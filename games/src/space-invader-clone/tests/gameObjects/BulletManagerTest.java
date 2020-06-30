package gameObjects;

import game.Position;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class BulletManagerTest {

    @Test
    public void bulletManagerTest() {

        //Some fields of bullets
        Camera c = new Camera();
        Position p = new Position(3, 4);

        //creating bullet, it's copy
        Bullet b1 = new Bullet(CameraAndEntitiesContainerGenerator.generate(c), p, new BufferedImage(1, 1, 1), 2, 2);
        Bullet b2 = b1.getCopy();

        //bullet manager
        BulletManager bm = new BulletManager();

        //adding prototypes of bullets
        bm.addType("type 1", b1);
        bm.addType("type 2", b2);

        //getting our new copies
        Bullet b1copy = bm.getCopyOfAndAddToList("type 1");
        Bullet b2copy = bm.getCopyOfAndAddToList("type 2");
        Bullet nullBullet = bm.getCopyOfAndAddToList("randomString");


        //copies are not the same as originals
        assertNotSame(b1, b1copy);
        assertNotSame(b2, b2copy);

        //also they are not the same (bullet copy)
        assertNotSame(b1, b2);

        //bulletmanager should not return anything in this case
        assertNull(nullBullet);

        //both bullets should be on bullet list
        assertTrue(bm.getBullets().contains(b1copy));
        assertTrue(bm.getBullets().contains(b2copy));

        //originals should not
        assertFalse(bm.getBullets().contains(b1));
        assertFalse(bm.getBullets().contains(b2));

        //their position (at this time) is same but reference is not
        assertNotSame(b1copy.getPosition(), b2copy.getPosition());

        //deleting one of the bullets
        b1copy.setToDelete(true);
        bm.update();
        assertTrue(!bm.getBullets().contains(b1copy));
        assertTrue(bm.getBullets().contains(b2copy));
    }
}