package stages;

import game.Sprites;
import system.Settings;
import system.Stage;
import system.StageArguments;
import system.StageManager;
import gameobjects.NonInteractiveImage;
import gameobjects.buttons.SpriteButton;
import graphics.RenderingManager;
import graphics.SpriteLoader;

public class HelpStage extends Stage {

    StageManager stageManager;
    RenderingManager renderingManager;
    NonInteractiveImage tutorialBg;
    SpriteButton okayButton;

    public HelpStage(StageArguments stageArguments) {
        super(stageArguments.getRenderingManager(), stageArguments.getInteractionManager());
        this.renderingManager = stageArguments.getRenderingManager();
        this.stageManager = stageArguments.getStageManager();

        tutorialBg = new NonInteractiveImage(SpriteLoader.load(Sprites.tutorial));
        okayButton = new SpriteButton(SpriteLoader.load(Sprites.gotIt), 500, 620, 1).setClickAction(() -> {
            ((FloodtownStageManager) this.stageManager).setGameplayStage();
        });

        add(tutorialBg);
        add(okayButton);

        if (Settings.additionalEffects) {
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.noise)));
        }
    }

    @Override
    public void update() {

    }
}
