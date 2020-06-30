package system;

import graphics.RenderingManager;
import interactions.GameObject;
import interactions.InteractionManager;

import java.util.ArrayList;

public abstract class Stage {
    protected RenderingManager renderingManager;
    protected InteractionManager interactionManager;
    protected ArrayList<GameObject> gameObjects = new ArrayList<>();

    public Stage(RenderingManager renderingManager, InteractionManager interactionManager) {
        this.renderingManager = renderingManager;
        this.interactionManager = interactionManager;
        refocus();
    }

    /**
     * refocus forces rendering manager and interaction manager to react to gameObjects of this stage
     */
    public void refocus() {
        renderingManager.setEntitesToRender(gameObjects);
        interactionManager.setGameObjects(gameObjects);
    }

    protected void add(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public abstract void update();

}
