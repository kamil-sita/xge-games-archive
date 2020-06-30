package gameObjects;

import gameObjects.Camera;
import gameObjects.CameraAndEntitiesContainer;
import gameObjects.Entity;

import java.util.ArrayList;

public class CameraAndEntitiesContainerGenerator {
    public static CameraAndEntitiesContainer generate(Camera camera) {
        return new CameraAndEntitiesContainer() {
            ArrayList<Entity> entities = new ArrayList<>();

            @Override
            public Camera getCamera() {
                return camera;
            }

            @Override
            public ArrayList<Entity> getEntities() {
                return entities;
            }
        };
    }
}
