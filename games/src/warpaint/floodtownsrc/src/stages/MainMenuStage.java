package stages;

import game.Sprites;
import gameobjects.ColorFill;
import system.Settings;
import system.Stage;
import gameobjects.NonInteractiveImage;
import gameobjects.buttons.SimpleButton;
import graphics.SpriteLoader;
import system.StageArguments;

import java.awt.*;

public class MainMenuStage extends Stage {

    public MainMenuStage(StageArguments stageArguments) {

        super(stageArguments.getRenderingManager(), stageArguments.getInteractionManager());
        SimpleButton gameplayButton = new SimpleButton("Play" , 450, 350, 32);

        WarpaintStageManager stageManager = (WarpaintStageManager) stageArguments.getStageManager();

        gameplayButton.setClickAction(stageManager::setGameplayStage);

        SimpleButton settingsButton = new SimpleButton("Settings" , 450, 550, 32);
        settingsButton.setClickAction(stageManager::setSettingsStage);

        add(new ColorFill(new Color(40, 40, 40)));

        add(settingsButton);
        add(gameplayButton);

        if (Settings.additionalEffects) {
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.vignette)));
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.noise)));
        }
    }

    @Override
    public void update() {

    }
}
