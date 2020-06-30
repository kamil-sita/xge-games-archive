package stages;

import game.Sprites;
import sound.FloodtownMusicPlayer;
import system.Settings;
import system.Stage;
import gameobjects.NonInteractiveImage;
import gameobjects.WaterFallEffect;
import gameobjects.buttons.SimpleButton;
import graphics.SpriteLoader;
import system.StageArguments;

public class MainMenuStage extends Stage {

    public MainMenuStage(StageArguments stageArguments) {

        super(stageArguments.getRenderingManager(), stageArguments.getInteractionManager());
        SimpleButton gameplayButton = new SimpleButton("Play" , 450, 350, 32);

        FloodtownStageManager stageManager = (FloodtownStageManager) stageArguments.getStageManager();

        gameplayButton.setClickAction(stageManager::setGameplayStage);



        SimpleButton tutorialButton = new SimpleButton("Tutorial" , 450, 450, 32);
        tutorialButton.setClickAction(stageManager::setTutorialStage);

        SimpleButton settingsButton = new SimpleButton("Settings" , 450, 550, 32);
        settingsButton.setClickAction(stageManager::setSettingsStage);

        add(new NonInteractiveImage(SpriteLoader.load(Sprites.background)));

        add(new WaterFallEffect());
        add(new NonInteractiveImage(SpriteLoader.load(Sprites.logo)).setPosition(450, 50));

        add(settingsButton);
        add(tutorialButton);
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
