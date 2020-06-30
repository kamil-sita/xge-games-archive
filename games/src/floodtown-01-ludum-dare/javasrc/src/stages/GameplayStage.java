package stages;

import game.Stage;
import game.StageManager;
import gameobjects.*;
import graphics.SpriteLoader;
import gameobjects.BuildingShop;
import hud.Clock;
import graphics.GameObject;
import hud.DebugHud;
import graphics.RenderingManager;
import gameobjects.IconAndText;
import main.KeyListener;
import main.MouseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameplayStage implements Stage {

    KeyListener keyListener;
    MouseListener mouseListener;
    RenderingManager renderingManager;
    DebugHud hud;
    List<GameObject> thingsToRender = new ArrayList<>();
    StageManager stageManager;

    GameBoard gameBoard;
    Clock clock;
    GameResources gameResources;

    IconAndText powerBar;
    IconAndText humanBar;
    IconAndText workerBar;
    IconAndText coinBar;
    IconAndText foodBar;

    IconAndText currentBonusBar;
    IconAndText currentClockspeedInfo;

    NonInteractiveImage gameOverBackdrop;
    IconAndText gameOverScreen;

    BonusType currentBonus;

    Button overclockButton;

    Button tutorialButton;
    Button restartButton;

    BuildingShop[] buildingShops = new BuildingShop[4];


    public GameplayStage (KeyListener keyListener, MouseListener mouseListener, RenderingManager renderingManager, DebugHud hud, StageManager stageManager) {
        this.keyListener = keyListener;
        this.mouseListener = mouseListener;
        this.renderingManager = renderingManager;
        this.hud = hud;
        this.stageManager = stageManager;

        thingsToRender.add(new NonInteractiveImage(SpriteLoader.load(SpriteLoader.background)));
        thingsToRender.add(new NonInteractiveImage(SpriteLoader.load(SpriteLoader.backShadow)));

        gameBoard = new GameBoard();
        clock = new Clock();
        gameResources = new GameResources();
        thingsToRender.add(clock);
        thingsToRender.add(gameBoard);

        powerBar = new IconAndText("Power: ?", SpriteLoader.load(SpriteLoader.power), 1000, 205);
        humanBar = new IconAndText("Inhabitants: ?", SpriteLoader.load(SpriteLoader.human), 1000, 75);
        coinBar  = new IconAndText("Money: ?", SpriteLoader.load(SpriteLoader.money), 1000, 10);
        foodBar  = new IconAndText("Food: ?", SpriteLoader.load(SpriteLoader.farm), 1000, 270);
        workerBar  = new IconAndText("Unemployed: ?", SpriteLoader.load(SpriteLoader.unemployed), 1000, 140);
        currentBonusBar = new IconAndText("No current bonus.", SpriteLoader.load(SpriteLoader.nothing), 10, 420);
        gameResources.money = 100;
        updateInformation();

        gameOverScreen = new IconAndText("Victory! Money earned: xxxx", SpriteLoader.load(SpriteLoader.trophy), 400, 350).setVisible(false).setScaling(0.6);

        tutorialButton = new Button(SpriteLoader.load(SpriteLoader.tutorialIcon), 40, 600, 0.3);
        restartButton = new Button(SpriteLoader.load(SpriteLoader.restartIcon), 120, 600, 0.3);
        thingsToRender.add(restartButton);

        thingsToRender.add(powerBar);
        thingsToRender.add(humanBar);
        thingsToRender.add(coinBar);
        thingsToRender.add(foodBar);
        thingsToRender.add(workerBar);
        thingsToRender.add(currentBonusBar);
        thingsToRender.add(tutorialButton);

        thingsToRender.add(new IconAndText("", SpriteLoader.load(SpriteLoader.paperWithTips), 1060, 375).setScaling(1));

        int spacing = 80;
        int xPos = 1000;
        int yPos = 400;

        buildingShops[0] = new BuildingShop(SpriteLoader.load(SpriteLoader.house), xPos, yPos, BuildingType.housing);
        buildingShops[1] = new BuildingShop(SpriteLoader.load(SpriteLoader.farm), xPos, yPos + spacing, BuildingType.farm);
        buildingShops[2] = new BuildingShop(SpriteLoader.load(SpriteLoader.office), xPos, yPos + spacing * 2, BuildingType.office);
        buildingShops[3] = new BuildingShop(SpriteLoader.load(SpriteLoader.coalPlant), xPos, yPos + spacing * 3, BuildingType.powerGenerator);

        thingsToRender.addAll(Arrays.asList(buildingShops));

        overclockButton = new Button(SpriteLoader.load(SpriteLoader.overclock), 10, 220, 0.25);
        thingsToRender.add(overclockButton);
        currentClockspeedInfo = new IconAndText("Clockspeed: 1.0 (1/3)",  null, 75, 250);
        thingsToRender.add(currentClockspeedInfo);
        thingsToRender.add(new IconAndText("Current bonus:", null, 55, 385));

        gameOverBackdrop = new NonInteractiveImage(SpriteLoader.load(SpriteLoader.gameOverBack)).setVisible(false);

        thingsToRender.add(gameOverBackdrop);
        thingsToRender.add(gameOverScreen);
        //effects, should be added last
        thingsToRender.add(new NonInteractiveImage(SpriteLoader.load(SpriteLoader.vignette)));
        thingsToRender.add(new NonInteractiveImage(SpriteLoader.load(SpriteLoader.noise)));
        renderingManager.setEntitesToRender(thingsToRender);
        getRandomBonusAndUpdateGui();
    }


    BuildingType selectedType = null;
    boolean lastIterationMouseWasClicked = false;
    int iteration = 0;
    double clockSpeed = 1;

    @Override
    public void update() {
        renderingManager.setEntitesToRender(thingsToRender); //this needs to be refreshed in case of change of focus (for example, opening tutorialIcon again)
        iteration++;

        if (gameBoard.isMouseOver(mouseListener) && mouseClick() && selectedType != null) {
            gameBoard.placeBuilding(gameResources, selectedType);
            updateInformation();
        }

        if (overclockButton.isMouseOver(mouseListener) && mouseClick()) {
            overclockIfPossible();
        }

        if (tutorialButton.isMouseOver(mouseListener) && mouseClick()) {
            stageManager.setTutorialStage();
        }

        if (restartButton.isMouseOver(mouseListener) && mouseClick()) {
            stageManager.setNewGameplayStage();
            renderingManager.setEntitesToRender(null);
            return;
        }


        for (BuildingShop buildingShop : buildingShops) {
            if (buildingShop.isMouseOver(mouseListener) && mouseClick()) {
                for (BuildingShop buildingShop1: buildingShops) {
                    buildingShop1.deselect();
                }
                selectedType = buildingShop.getBuilding();
            }
        }

        gameBoard.updateGameResources(gameResources, shouldChangeClock(iteration), currentBonus);
        updateInformation();


        lastIterationMouseWasClicked = mouseListener.isClicking;
        if (!shouldChangeClock(iteration)) return;
        if (!gameBoard.isEverythingWater()) {
            gameResources.money += 1; //a little bonus if player gets stuck
        }

        clock.nextGraphic();
        floodSomething(iteration);
        updateInformation();


    }

    int clockspeedboosts = 1;

    private boolean overclockIfPossible() {
        if (clockSpeed < 3) {
            clockspeedboosts++;
            clockSpeed += 1.00;
            currentClockspeedInfo.setText("Clockspeed: " + clockSpeed + " (" + (clockspeedboosts) + "/3)");
            getRandomBonusAndUpdateGui();
            gameBoard.clearWater();
            return true;
        }
        return false;
    }

    private void getRandomBonusAndUpdateGui() {
        currentBonus = BonusType.getRandomBonus();
        switch (currentBonus) {
            case none:
                currentBonusBar.setText("No current bonus.");
                currentBonusBar.setSprite(SpriteLoader.load(SpriteLoader.nothing));
                break;
            case superCows:
                currentBonusBar.setText("More food.");
                currentBonusBar.setSprite(SpriteLoader.load(SpriteLoader.farmPlus));
                break;
            case superCoal:
                currentBonusBar.setText("More power.");
                currentBonusBar.setSprite(SpriteLoader.load(SpriteLoader.coalPlantPlus));
                break;
            case goodEconomy:
                currentBonusBar.setText("More money.");
                currentBonusBar.setSprite(SpriteLoader.load(SpriteLoader.money));
                break;
        }
    }

    private void gameOver() {
        gameOverBackdrop.setVisible(true);
        gameOverScreen.setVisible(true);
        gameOverScreen.setText("Victory! Money earned: " + gameResources.money);
    }


    private boolean mouseClick() {
        return !mouseListener.isClicking && lastIterationMouseWasClicked;
    }

    public void updateInformation() {
        humanBar.setText("Inhabitants: " + gameResources.getInhabitants());
        powerBar.setText("Power: " + gameResources.getAvailablePower());
        foodBar.setText("Food: " + gameResources.getAvailableFood());
        coinBar.setText("Money: " + gameResources.money);
        workerBar.setText("Unemployed: " + gameResources.getUnemployed());
    }

    private boolean shouldChangeClock(int iteration) {
        return iteration % (secondsToIterations(1.5)) == 0;
    }

    private boolean shouldFloodSomething(int iteration) {
        return (iteration % secondsToIterations(1.5 * 4) == 0) || iteration > secondsToIterations(1.5 * 4 * 10);
    }

    private boolean playWaveSound(int iteration) {
        return (iteration % secondsToIterations(1.5 * 4) == 0);
    }

    boolean gameOverCalled = false;
    private void floodSomething(int iteration) {
        boolean tileChanged = false;
        while (!tileChanged) {
            //if this if wouldn't exist there would be an infinite loop trying to find random field that is not water
            if (gameBoard.isEverythingWater()) {
                if (overclockIfPossible()) {
                    tileChanged = true;
                } else {
                    if (!gameOverCalled) {
                        gameOver();
                        gameOverCalled = true;
                    }
                    tileChanged = true;
                }
            }
            int x = (int) (Math.random() * GameBoard.BOARD_SIZE_IN_CELLS);
            int y = (int) (Math.random() * GameBoard.BOARD_SIZE_IN_CELLS);
            if (gameBoard.canBeFlooded(x, y)) {
                gameBoard.flood(x, y, playWaveSound(iteration));
                tileChanged = true;
            }
        }
    }

    private int secondsToIterations(double seconds) {
        return (int) (60.0 / clockSpeed * seconds);
    }
}
