package graphics;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FlyweightGraphicsLoaderTest {

    @Test
    void getFileFrom() {
        //this test tests both method and whether all strings in this class are correct

        ArrayList<BufferedImage> loadedSprites = new ArrayList<>();
        ArrayList<String> spritesLocation = new ArrayList<>();

        //getting all strings in FlyweightGraphicsLoader
        Class<FlyweightGraphicsLoader> flyweightGraphicsLoaderClass = FlyweightGraphicsLoader.class;
        for (Field f : flyweightGraphicsLoaderClass.getFields()) {
            if (f.getType().equals(String.class)) {
                try {
                    spritesLocation.add((String) f.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        //checking those strings
        for (String s : spritesLocation) {
            System.out.println(s);
            BufferedImage bi = FlyweightGraphicsLoader.getFileFrom(s);
            assertNotNull(bi);
        }
    }
}