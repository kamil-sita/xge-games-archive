package stages;

import game.*;
import system.Settings;
import system.Stage;
import system.StageArguments;
import system.StageManager;
import gameobjects.*;
import gameobjects.buttons.BuildingShop;
import gameobjects.buttons.SpriteButton;
import gameobjects.containers.StatisticsContainer;
import graphics.RenderingManager;
import graphics.SpriteLoader;

import java.util.Arrays;

public class TutorialStage extends Stage {

    StageArguments stageArguments;

    RenderingManager renderingManager;
    StageManager stageManager;

    GameBoard gameBoard;
    GameResources gameResources;

    StatisticsContainer statisticsContainer = new StatisticsContainer();

    IconAndText currentBonusBar;
    IconAndText currentClockspeedInfo;

    NonInteractiveImage endTutorialBackdrop;
    IconAndText endTutorialScreen;

    BonusType currentBonus;

    SpriteButton overclockButton;

    BuildingShop[] buildingShops = new BuildingShop[4];

    SimpleMessage simpleMessage;


    public TutorialStage(StageArguments stageArguments) {
        super(stageArguments.getRenderingManager(), stageArguments.getInteractionManager());
        this.renderingManager = stageArguments.getRenderingManager();
        this.stageManager = stageArguments.getStageManager();
        this.stageArguments = stageArguments;

        add(new NonInteractiveImage(SpriteLoader.load(Sprites.backgroundWithShadow)));

        gameBoard = new GameBoard(gameResources, selectedType);
        gameResources = new GameResources();
        add(gameBoard);

        currentBonusBar = new IconAndText("No current bonus.", SpriteLoader.load(Sprites.nothing), 10, 420);
        gameResources.money = 1000;
        statisticsContainer.updateInformation(gameResources);

        endTutorialScreen = new IconAndText("xxxx", SpriteLoader.load(Sprites.trophy), 400, 350).setVisible(false).setScaling(0.6);

        add(statisticsContainer);
        add(currentBonusBar);

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

        overclockButton = new SpriteButton(SpriteLoader.load(Sprites.overclock), 10, 220, 0.25).setClickAction(this::endTutorial);
        add(overclockButton);
        currentClockspeedInfo = new IconAndText("Overclock",  null, 75, 250);
        add(currentClockspeedInfo);
        add(new IconAndText("Current bonus:", null, 55, 385));

        endTutorialBackdrop = new NonInteractiveImage(SpriteLoader.load(Sprites.gameOverBack)).setVisible(false);
        simpleMessage = new SimpleMessage();
        add(simpleMessage.setString("Welcome to Floodtown tutorial! \nTo begin, select one of the buildings on the lower right corner of the screen."));

        add(endTutorialBackdrop);
        add(endTutorialScreen);
        //effects, should be added last
        if (Settings.additionalEffects) {
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.vignette)));
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.noise)));
        }

        renderingManager.setEntitesToRender(gameObjects);
        getDefaultTutorialBonus();
    }


    BuildingType selectedType = null;
    int iteration = 0;
    double clockSpeed = 1;

    private boolean isClockStarted = false;

    @Override
    public void update() {
        if (gameResources.money < 1000) {
            gameResources.money = 1000;
        }
        iteration++;
        tutorialProgression();

        gameBoard.setGameResources(gameResources);
        gameBoard.setSelectedType(selectedType);

        gameBoard.updateGameResources(gameResources, shouldChangeClock(iteration), currentBonus);
        statisticsContainer.updateInformation(gameResources);

        if (!shouldChangeClock(iteration)) return;

        if (isClockStarted) {
            floodSomething(iteration);

            statisticsContainer.updateInformation(gameResources);
        }
    }

    int progression = 0;
    private void tutorialProgression() {
        if (progression == 0) {
            simpleMessage.setString("Welcome to Floodtown tutorial! \nTo begin, select one of the buildings on the right.");
            if (selectedType != null) {
                progression = 1;
            }
        }
        if (progression == 1) {
            simpleMessage.setString("Good job! Now place this building on game board. Placing normally costs 10 gold, \nbut it's free here. " +
                    "Also note, that you can't build on mountain tiles.");
            boolean anythingSet = false;
            Field[] fields = gameBoard.getFieldsInArray();
            for (Field field : fields) {
                if (field.getBuildingType() != BuildingType.empty) {
                    anythingSet = true;
                    break;
                }
            }
            if (anythingSet) progression = 2;
        }
        if (progression == 2) {
            simpleMessage.setString("Good job! Now I will quickly explain how to make profit: \n" +
                    "- You need coal plant (red building) to power all types of buildings. \n" +
                    "- You need food (light green building) and housing (green building) if you want some inhabitants. \n" +
                    "- With all this power and inhabitants you can power factories (yellow building) which generate money. \n" +
                    "Once you earn some money, tutorial will continue. ");
            if (gameResources.money >= 1001) {
                gameBoard.clearAll();
                progression = 3;
            }
        }
        if (progression == 3) {
            simpleMessage.setString("If you were to forget about all those dependencies, there is a handy graph on the right. \n" +
                    "Now, you shall learn about adjacency bonuses. \n" +
                    "If you place 2 or more similar buildings nearby, they will generate more resources. \n" +
                    "Place 2 similar buildings nearby, to proceed.");
            if (gameBoard.isAnythingConnected()) {
                simpleDoLater(1, () -> {
                    gameBoard.clearAll();
                    progression = 4;
                });
            }
        }
        if (progression == 4) {
            simpleMessage.setString("There are other types of bonuses as well. \n" +
                    "On the left, you can see global bonus. All buildings of given type will generate more resources. \n" +
                    "There is also field bonus: placing coal plant or farm on their special field, also increases \n" +
                    "their resource output. \n" +
                    "Place building on it's special field to proceed.");
            for (Field field : gameBoard.getFieldsInArray()) {
                if ((field.getBuildingType() == BuildingType.farm && field.getBasicFieldType() == BasicFieldType.bonusFarm)
                || (field.getBuildingType() == BuildingType.powerGenerator && field.getBasicFieldType() == BasicFieldType.bonusPower)) {
                    simpleDoLater(1, () -> {
                        for (int i = 0; i < 100; i++) {
                            int x = (int) (Math.random() * GameBoard.BOARD_SIZE_IN_CELLS);
                            int y = (int) (Math.random() * GameBoard.BOARD_SIZE_IN_CELLS);
                            if (gameBoard.canBeFlooded(x, y)) {
                                gameBoard.flood(x, y, false);
                            } else {
                                i--;
                            }
                        }
                        progression = 5;
                    });
                }
            }
        }
        if (progression == 5) {
            simpleMessage.setString("WOAH! Take a look at this! Map got flooded! \n" +
                    "This will happen regularly during normal gameplay, but less suddenly - water will take map field \nby field." +
                    "You can clear all fields from water, by using OVERCLOCK button the left. \nBy doing so, you will speed the game up, " +
                    "and change current global bonus. \n" +
                    "Press OVERCLOCK button to proceed."
            );
        }
        if (progression == 6) {
            simpleMessage.setString("Congratulations! You finished the tutorial! \n" +
                    "Press OVERCLOCK button again to return to main menu. \n" +
                    "Good luck, have fun!");
        }
    }

    private void getDefaultTutorialBonus() {
        currentBonusBar.setText("More food.");
        currentBonusBar.setSprite(SpriteLoader.load(Sprites.farmPlus));
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
                if (!gameOverCalled) {
                    endTutorial();
                    gameOverCalled = true;
                }
                tileChanged = true;
            }
            int x = (int) (Math.random() * GameBoard.BOARD_SIZE_IN_CELLS);
            int y = (int) (Math.random() * GameBoard.BOARD_SIZE_IN_CELLS);
            if (gameBoard.canBeFlooded(x, y)) {
                gameBoard.flood(x, y, playWaveSound(iteration));
                tileChanged = true;
            }
        }
    }

    boolean firstOverclock = true;

    private void endTutorial() {
        if (firstOverclock) {
            if (progression == 5) {
                gameBoard.clearWater();
                progression = 6;
                firstOverclock = false;
            }

        } else {
            endTutorialBackdrop.setVisible(true);
            endTutorialScreen.setVisible(true);
            endTutorialScreen.setText("Tutorial completed!");
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((FloodtownStageManager) stageManager).setMainMenuStage();
            }).start();
        }

    }

    private boolean threadAlreadyStarted = false;
    private void simpleDoLater(double seconds, Runnable runnable) {
        if (threadAlreadyStarted) return;
        threadAlreadyStarted = true;
        new Thread(() -> {
            try {
                Thread.sleep((int) (seconds * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runnable.run();
            threadAlreadyStarted = false;
        }).start();
    }

    private int secondsToIterations(double seconds) {
        return (int) (60.0 / clockSpeed * seconds);
    }
}
