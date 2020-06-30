package stages;

import graphics.RenderingManager;
import interactions.InteractionManager;
import system.StageManager;

public class FloodtownStageManager extends StageManager {

    public FloodtownStageManager(InteractionManager interactionManager, RenderingManager renderingManager) {
        super(interactionManager, renderingManager);
    }

    private GameplayStage gameplayStage = null;

    @Override
    public void setDefaultStage() {
        setStage(new MainMenuStage(stageArguments));
    }

    public void setTutorialStage() {
        setStage(new TutorialStage(stageArguments));
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

    public void setHelpStage() {
        setStage(new HelpStage(stageArguments));
    }


    public void setSettingsStage() {
        setStage(new SettingsStage(stageArguments));
    }

    public void setMainMenuStage() {
        setStage(new MainMenuStage(stageArguments));
    }

}
