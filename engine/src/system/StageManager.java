package system;

import graphics.RenderingManager;
import interactions.InteractionManager;

public abstract class StageManager {

    protected StageArguments stageArguments;

    public StageManager(InteractionManager interactionManager, RenderingManager renderingManager) {
        this.stageArguments = new StageArguments(this, renderingManager, interactionManager);
    }

    private Stage stage = null;


    public void tick() {
        currentStage().update();
    }

    public void setStage(Stage stage) {
        beforeStageChange();
        this.stage = stage;
        afterStageChange();
    }

    public Stage getStage() {
        return stage;
    }

    protected Stage currentStage() {
        if (stage == null) {
            setDefaultStage();
        }
        return stage;
    }

    public abstract void setDefaultStage();


    private void beforeStageChange() {
        stageArguments.getRenderingManager().setEntitesToRender(null);
        stageArguments.getInteractionManager().setGameObjects(null);
    }

    private void afterStageChange() {
       currentStage().refocus();
    }


}
