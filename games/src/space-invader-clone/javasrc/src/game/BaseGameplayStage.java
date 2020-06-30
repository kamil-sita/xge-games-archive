package game;


import gameObjects.Camera;
import gameObjects.Entity;

public interface BaseGameplayStage {
    Camera getCamera();
    void removeEntity(Entity entity);
}
