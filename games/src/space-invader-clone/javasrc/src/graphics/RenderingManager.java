package graphics;

import game.Settings;
import gameObjects.Entity;
import gameObjects.PhysicalEntity;

import java.awt.*;
import java.util.ArrayList;

public final class RenderingManager {

    //list of elements that should be rendered
    private ArrayList<Entity> entitiesToRender;

    private Hud hud;

    public RenderingManager(Hud hud) {
        this.hud = hud;
    }

    public void renderScreen(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1600, 900);

        for (int renderPriority = RenderPriority.minValue; renderPriority <= RenderPriority.maxValue; renderPriority++) {
            for (var renderable : entitiesToRender) {
                if (renderable == null) continue;
                if (renderable.getRenderPriority().intValue() == renderPriority) {
                    renderable.render(g, Settings.DEBUG_MODE);
                }
            }
        }
        hud.renderAllInQueue(g);
    }


    public void setEntitesToRender(ArrayList<Entity> entitesToRender) {
        this.entitiesToRender = entitesToRender;
    }
}
