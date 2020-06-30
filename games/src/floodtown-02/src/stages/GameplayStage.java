package stages;

import game.BuildingType;
import game.GameResources;
import game.Sprites;
import system.Settings;
import system.Stage;
import system.StageArguments;
import system.StageManager;
import gameobjects.GameBoard;
import gameobjects.IconAndText;
import gameobjects.NonInteractiveImage;
import gameobjects.buttons.BuildingShop;
import gameobjects.buttons.SimpleButton;
import gameobjects.buttons.SpriteButton;
import gameobjects.containers.GlobalBonusContainer;
import gameobjects.containers.StatisticsContainer;
import graphics.RenderingManager;
import graphics.SpriteLoader;
import gameobjects.Clock;

import java.util.Arrays;

public class GameplayStage extends Stage {

    StageArguments stageArguments;

    RenderingManager renderingManager;
    StageManager stageManager;

    GameBoard gameBoard;
    Clock clock;
    GameResources gameResources;

    IconAndText currentClockspeedInfo;

    NonInteractiveImage gameOverBackdrop;
    IconAndText gameOverScreen;

    StatisticsContainer statisticsContainer = new StatisticsContainer();

    SpriteButton overclockButton;

    SimpleButton helpButton;
    SimpleButton restartButton;

    BuildingShop[] buildingShops = new BuildingShop[4];
    GlobalBonusContainer bonusContainer = new GlobalBonusContainer(3);


    public GameplayStage (StageArguments stageArguments) {
        super(stageArguments.getRenderingManager(), stageArguments.getInteractionManager());
        this.renderingManager = stageArguments.getRenderingManager();
        this.stageManager = stageArguments.getStageManager();
        this.stageArguments = stageArguments;

        add(new NonInteractiveImage(SpriteLoader.load(Sprites.backgroundWithShadow)));

        gameBoard = new GameBoard(gameResources, selectedType);
        clock = new Clock();
        gameResources = new GameResources();
        add(clock);
        add(gameBoard);
        add(statisticsContainer);

        gameResources.money = 100;
        statisticsContainer.updateInformation(gameResources);

        gameOverScreen = new IconAndText("Victory! Money earned: xxxx", SpriteLoader.load(Sprites.trophy), 400, 350).setVisible(false).setScaling(0.6);

        FloodtownStageManager stageManager = (FloodtownStageManager) this.stageManager;

        helpButton = new SimpleButton("Help", 20, 525, 25, () -> stageManager.setHelpStage());
        restartButton = new SimpleButton("Restart", 20, 580, 25, () -> stageManager.setNewGameplayStage());


        add(bonusContainer);
        add(restartButton);
        add(helpButton);


        add(new IconAndText("", SpriteLoader.load(Sprites.paperWithTips), 1060, 375).setScaling(1));

        int spacing = 80;
        int xPos = 1013;
        int yPos = 400;

        buildingShops[0] = new BuildingShop(xPos, yPos, BuildingType.housing).setClickAction(() -> {
            for (BuildingShop buildingShop1: buildingShops) {
                buildingShop1.deselect();
            }
            selectedType = BuildingType.housing;
        });
        buildingShops[1] = new BuildingShop(xPos, yPos + spacing, BuildingType.farm).setClickAction(() -> {
            for (BuildingShop buildingShop1: buildingShops) {
                buildingShop1.deselect();
            }
            selectedType = BuildingType.farm;
        });
        buildingShops[2] = new BuildingShop(xPos, yPos + spacing * 2, BuildingType.factory).setClickAction(() -> {
            for (BuildingShop buildingShop1: buildingShops) {
                buildingShop1.deselect();
            }
            selectedType = BuildingType.factory;
        });
        buildingShops[3] = new BuildingShop(xPos, yPos + spacing * 3, BuildingType.powerGenerator).setClickAction(() -> {
            for (BuildingShop buildingShop1: buildingShops) {
                buildingShop1.deselect();
            }
            selectedType = BuildingType.powerGenerator;
        });

        gameObjects.addAll(Arrays.asList(buildingShops));

        overclockButton = new SpriteButton(SpriteLoader.load(Sprites.overclock), 10, 220, 0.25).setClickAction(this::overclockIfPossible);
        add(overclockButton);
        currentClockspeedInfo = new IconAndText("Overclock: 1/3",  null, 75, 250);
        add(currentClockspeedInfo);
        add(new IconAndText("Global bonus:", null, 55, 345));

        SimpleButton backToMenuButton = new SimpleButton("Main menu" , 20, 635, 25);
        backToMenuButton.setClickAction(() -> stageManager.setMainMenuStage());
        add(backToMenuButton);


        gameOverBackdrop = new NonInteractiveImage(SpriteLoader.load(Sprites.gameOverBack)).setVisible(false);

        add(gameOverBackdrop);
        add(gameOverScreen);
        //effects, should be added last
        if (Settings.additionalEffects) {
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.vignette)));
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.noise)));
        }

        renderingManager.setEntitesToRender(gameObjects);

        refocus();
    }


    BuildingType selectedType = null;
    int iteration = 0;
    double clockSpeed = 1;

    @Override
    public void update() {
        iteration++;

        gameBoard.setGameResources(gameResources);
        gameBoard.setSelectedType(selectedType);

        gameBoard.updateGameResources(gameResources, shouldChangeClock(iteration), bonusContainer.getBonus());
        statisticsContainer.updateInformation(gameResources);

        if (!shouldChangeClock(iteration)) return;
        if (!gameBoard.isEverythingWater()) {
            gameResources.money += 1; //a little bonus if player gets stuck
        }

        clock.nextGraphic();
        floodSomething(iteration);

        statisticsContainer.updateInformation(gameResources);
    }


    int clockspeedboosts = 1;

    private boolean overclockIfPossible() {
        if (clockSpeed < 3) {
            clockspeedboosts++;
            clockSpeed += 1.00;
            currentClockspeedInfo.setText("Overclock: "+ (clockspeedboosts) + "/3");
            bonusContainer.nextBonus();
            gameBoard.clearWater();
            return true;
        }
        return false;
    }

    private void gameOver() {
        gameOverBackdrop.setVisible(true);
        gameOverScreen.setVisible(true);
        gameOverScreen.setText("Victory! Money earned: " + gameResources.money);
    }

    private boolean shouldChangeClock(int iteration) {
        return iteration % (secondsToIterations(1.5)) == 0;
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
