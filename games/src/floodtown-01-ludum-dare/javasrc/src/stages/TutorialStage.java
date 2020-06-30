package stages;

import game.Stage;
import game.StageManager;
import gameobjects.NonInteractiveImage;
import gameobjects.Button;
import graphics.GameObject;
import graphics.RenderingManager;
import graphics.SpriteLoader;
import main.MouseListener;

import java.util.ArrayList;
import java.util.List;

public class TutorialStage implements Stage {

    MouseListener mouseListener;
    StageManager stageManager;
    RenderingManager renderingManager;
    NonInteractiveImage tutorialBg;
    List<GameObject> thingsToRender = new ArrayList<>();
    Button okayButton;

    public TutorialStage(MouseListener mouseListener, StageManager stageManager, RenderingManager renderingManager) {
        this.mouseListener = mouseListener;
        this.stageManager = stageManager;
        this.renderingManager = renderingManager;
        tutorialBg = new NonInteractiveImage(SpriteLoader.load(SpriteLoader.tutorial));
        okayButton = new Button(SpriteLoader.load(SpriteLoader.gotIt), 500, 620, 1);

        thingsToRender.add(tutorialBg);
        thingsToRender.add(okayButton);
        thingsToRender.add(new NonInteractiveImage(SpriteLoader.load(SpriteLoader.noise)));
        renderingManager.setEntitesToRender(thingsToRender);
    }

    boolean lastIterationMouseWasClicked = false;

    @Override
    public void update() {
        if (okayButton.isMouseOver(mouseListener) && mouseClick()) {
            stageManager.setGameplayStage();
        }

        lastIterationMouseWasClicked = mouseListener.isClicking;
    }

    private boolean mouseClick() {
        return !mouseListener.isClicking && lastIterationMouseWasClicked;
    }
}
