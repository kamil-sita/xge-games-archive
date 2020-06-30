package stages;

import graphics.RenderingManager;
import interactions.InteractionManager;
import system.StageManager;

public class WarpaintStageManager extends StageManager {

    public WarpaintStageManager(InteractionManager interactionManager, RenderingManager renderingManager) {
        super(interactionManager, renderingManager);
    }

    private GameplayStage gameplayStage = null;

    @Override
    public void setDefaultStage() {
        setStage(new MainMenuStage(stageArguments));
    }

    public void setGameplayStage() {
        setStage(getCurrentGameplayStage());
    }

    private GameplayStage getCurrentGameplayStage() {
        if (gameplayStage == null) {
            setNewGameplayStage();
        }
        return gameplayStage;
    }


    public void setNewGameplayStage() {
        gameplayStage = new GameplayStage(stageArguments);
        setStage(new GameplayStage(stageArguments));
    }

    public void setSettingsStage() {
        setStage(new SettingsStage(stageArguments));
    }

    public void setMainMenuStage() {
        setStage(new MainMenuStage(stageArguments));
    }

}
