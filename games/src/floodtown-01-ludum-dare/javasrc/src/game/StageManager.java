package game;

import hud.DebugHud;
import graphics.RenderingManager;
import main.KeyListener;
import main.MouseListener;
import stages.GameplayStage;
import stages.TutorialStage;

public final class StageManager {
    private KeyListener keyListener;
    private MouseListener mouseListener;
    private static MouseListener globalMouseListener;

    private RenderingManager renderingManager;
    private DebugHud hud;

    public StageManager(KeyListener keyListener, MouseListener mouseListener, RenderingManager renderingManager, DebugHud hud) {
        this.mouseListener = mouseListener;
        globalMouseListener = mouseListener;
        this.keyListener = keyListener;
        this.renderingManager = renderingManager;
        this.hud = hud;
    }

    private Stage stage = null;
    private GameplayStage gameplayStage = null;

    public void update() {
        currentStage().update();
    }

    public static MouseListener getMouseListener() { //shitty workaround
        return globalMouseListener;
    }

    public void setGameplayStage() {
        stage = getCurrentGamemplayStage();
    }

    public void setNewGameplayStage() {
        gameplayStage = new GameplayStage(keyListener, mouseListener, renderingManager, hud, this);
        setGameplayStage();
    }

    public void setTutorialStage() {
        stage = new TutorialStage(mouseListener, this, renderingManager);
    }

    private GameplayStage getCurrentGamemplayStage() {
        if (gameplayStage == null) {
            gameplayStage = new GameplayStage(keyListener, mouseListener, renderingManager, hud, this);
        }
        return gameplayStage;
    }

    private Stage currentStage() {
        if (stage == null) {
            setTutorialStage();
        }
        return stage;
    }


}
