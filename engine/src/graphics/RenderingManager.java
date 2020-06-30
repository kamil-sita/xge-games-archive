package graphics;

import interactions.GameObject;
import org.jetbrains.annotations.NotNull;
import system.Settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public final class RenderingManager {

    //list of elements that should be rendered
    private ArrayList<GameObject> gameObjects = null;

    private DebugHud hud;

    public RenderingManager() {
    }

    public void renderScreen(@NotNull Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Settings.windowWidth, Settings.windowHeight);

        if (gameObjects == null) return;

        ArrayList<GameObject> gameObjectsAtStart = gameObjects;
        Iterator<GameObject> gameObjectIterator = gameObjectsAtStart.iterator();
        while (gameObjectIterator.hasNext()) {
            GameObject gameObject = null;
            try {
                gameObject = gameObjectIterator.next();
                //can throw exception due to multithreading, but it can be most of the time safely ignored
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (gameObject == null) continue;
            gameObject.render(g);
        }

        if (hud != null) {
            hud.renderDebugInfo(g);
        }

    }


    public RenderingManager setDebugHud(DebugHud debugHud) {
        this.hud = debugHud;
        return this;
    }

    public void setEntitesToRender(ArrayList<GameObject> entitiesToRender) {
        this.gameObjects = entitiesToRender;
    }

    public boolean hasThingsToRender() {
        return gameObjects != null;
    }
}
