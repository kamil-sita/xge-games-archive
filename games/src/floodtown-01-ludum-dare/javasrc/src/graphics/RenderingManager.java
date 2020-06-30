package graphics;

import game.StageManager;
import gameobjects.NonInteractiveImage;
import hud.DebugHud;
import main.MouseListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class RenderingManager {

    //list of elements that should be rendered
    private ArrayList<GameObject> gameObjects = null;

    private DebugHud hud;

    public RenderingManager(DebugHud hud) {
        this.hud = hud;
    }

    public void renderScreen(Graphics2D g) {
        MouseListener mouseListener = StageManager.getMouseListener();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 3840, 2160);

        if (gameObjects == null) return;
        new NonInteractiveImage(SpriteLoader.load(SpriteLoader.kilroy)).setPosition(1281, 0).render(g); //hmmm
        for (GameObject gameObject : gameObjects) {
            if (gameObject == null) continue;
            if (gameObject.isMouseOver(mouseListener)) {
                gameObject.onHover(g);
            }
            gameObject.render(g);
        }

        hud.renderDebugInfo(g);
    }

    public void setEntitesToRender(List<GameObject> entitesToRender) {
        this.gameObjects = (ArrayList<GameObject>) entitesToRender;
    }

    public boolean hasThingsToRender() {
        return gameObjects != null;
    }
}
