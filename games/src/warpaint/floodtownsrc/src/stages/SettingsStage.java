package stages;

import game.Sprites;
import gameobjects.ColorFill;
import system.Settings;
import system.Stage;
import gameobjects.NonInteractiveImage;
import gameobjects.buttons.SimpleButton;
import gameobjects.buttons.SimpleSettingsButton;
import graphics.SpriteLoader;
import interactions.GameObject;
import org.jetbrains.annotations.NotNull;
import system.StageArguments;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class SettingsStage extends Stage {

    public SettingsStage(StageArguments stageArguments) {
        super(stageArguments.getRenderingManager(), stageArguments.getInteractionManager());

        add(new ColorFill(new Color(40, 40, 40)));

        SimpleSettingsButton qualityOfRenderButton = new SimpleSettingsButton(100, 200,
                () -> Settings.highQualityRendering = !Settings.highQualityRendering);
        qualityOfRenderButton.setTextUpdateAction(() -> {
            if (Settings.highQualityRendering) {
                qualityOfRenderButton.setCurrentString("High quality rendering");
            } else {
                qualityOfRenderButton.setCurrentString("Low quality rendering");
            }
        });

        SimpleSettingsButton debugInfoButton = new SimpleSettingsButton(100, 300,
                () -> Settings.showDebug = !Settings.showDebug);
        debugInfoButton.setTextUpdateAction(() -> {
            if (Settings.showDebug ) {
                debugInfoButton.setCurrentString("Debug info visible");
            } else {
                debugInfoButton.setCurrentString("Debug info hidden");
            }
        });

        SimpleSettingsButton additionalEffectsButton = new SimpleSettingsButton(100, 400,
                () -> Settings.additionalEffects = !Settings.additionalEffects);
        additionalEffectsButton.setTextUpdateAction(() -> {
            if (Settings.additionalEffects ) {
                additionalEffectsButton.setCurrentString("Additional effects displayed");
            } else {
                additionalEffectsButton.setCurrentString("Additional effects not shown");
            }
        });


        SimpleButton backToMenuButton = new SimpleButton("Main menu" , 100, 600, 32);
        backToMenuButton.setClickAction(() -> ((WarpaintStageManager) stageArguments.getStageManager()).setMainMenuStage());

        add(qualityOfRenderButton);
        add(debugInfoButton);
        add(backToMenuButton);
        add(additionalEffectsButton);

        if (Settings.additionalEffects) {
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.vignette)));
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.noise)));
        }
    }




    @Override
    public void update() {

    }
}
