package game;

import graphics.Hud;
import graphics.RenderingManager;
import main.KeyListener;
import static game.StageState.*;

public final class StageManager {
    private KeyListener keyListener;

    private RenderingManager renderingManager;
    private Hud hud;

    public StageManager(KeyListener keyListener, RenderingManager renderingManager, Hud hud) {
        this.keyListener = keyListener;
        this.renderingManager = renderingManager;
        this.hud = hud;
    }

    private GameplayStage gameplayStage;

    private StageState stageState = working;

    public void update() {
        if (stageState == working) {
            if (gameplayStage == null) {
                gameplayStage = new GameplayStage(keyListener, renderingManager, hud);
            }
            stageState = gameplayStage.update();
        }
        if (stageState == finished) {
            gameplayStage = new GameplayStage(keyListener, renderingManager, hud);
            stageState = working;
        }

    }


}
