package gameobjects;

import game.StageManager;
import graphics.GameObject;
import main.MouseListener;
import sound.MusicPlayer;
import sound.SoundEffect;

import java.awt.*;

public class GameBoard extends GameObject {

    public final static int BOARD_SIZE_IN_CELLS = 13;
    public final static int BOARD_SIZE_IN_PIXELS = 700;
    public final static int CELL_SIZE_IN_PIXELS = BOARD_SIZE_IN_PIXELS/BOARD_SIZE_IN_CELLS;
    public final static int BOARD_Y_POSITION = 10;
    public final static int BOARD_X_POSITION = 290;

    final Field[][] gameBoardField = new Field[BOARD_SIZE_IN_CELLS][BOARD_SIZE_IN_CELLS];
    final Connector[][] connected = new Connector[BOARD_SIZE_IN_CELLS][BOARD_SIZE_IN_CELLS];

    public GameBoard() {
        for (int x = 0; x < BOARD_SIZE_IN_CELLS; x++) {
            for (int y = 0; y < BOARD_SIZE_IN_CELLS; y++) {

                int fieldXPosition = BOARD_X_POSITION + x * CELL_SIZE_IN_PIXELS;
                int fieldYPosition = BOARD_Y_POSITION + y * CELL_SIZE_IN_PIXELS;

                gameBoardField[y][x] = Field.newLandField(fieldXPosition, fieldYPosition);

            }
        }
        xPos = BOARD_X_POSITION;
        yPos = BOARD_Y_POSITION;

        width = BOARD_SIZE_IN_PIXELS;
        height = BOARD_SIZE_IN_PIXELS;
    }

    public boolean canBeFlooded(int x, int y) {
        return hasNeighbouringWater(x, y) && !isWater(x, y);
    }

    public boolean hasNeighbouringWater(int x, int y) {
        if (x == 0 || x == BOARD_SIZE_IN_CELLS -1 || y == 0 || y == BOARD_SIZE_IN_CELLS -1) { //edges always have neighbouring water, and I don't want to get outside of the array
            return true;
        }
        boolean top = isWater(x, y - 1);
        boolean bottom = isWater(x, y + 1);
        boolean left = isWater(x - 1, y);
        boolean right = isWater(x + 1, y);
        return top || bottom || left || right;
    }

    public boolean isWater(int x, int y) {
        return gameBoardField[y][x].underWater;
    }

    public void flood(int x, int y, boolean playSound) {
        if (playSound) {
            MusicPlayer.play(SoundEffect.waves);
        }
        gameBoardField[y][x].setUnderWater();
    }

    public void updateGameResources(GameResources gameResources, boolean addMoney, BonusType currentBonus) {
        gameResources.clearBeforeIteration();
        updateConnectors();
        for (int i = 0; i < 4; i++) {

            for (int y = 0; y < BOARD_SIZE_IN_CELLS; y++) {
                for (int x = 0; x < BOARD_SIZE_IN_CELLS; x++) {
                    int connections = 0;
                    if (connected[y][x].connectedRight) {
                        connections++;
                    }
                    if (connected[y][x].connectedDown) {
                        connections++;
                    }
                    gameBoardField[y][x].updateGameResources(gameResources, addMoney, i, currentBonus, connections);
                }
            }
        }


    }

    public void clearWater() {
        for (Field[] fields : gameBoardField) {
            for (Field field : fields) {
                field.underWater = false;
            }
        }
    }

    public boolean isEverythingWater() {
        for (int x = 0; x < BOARD_SIZE_IN_CELLS; x++) {
            for (int y = 0; y < BOARD_SIZE_IN_CELLS; y++) {
                if(!gameBoardField[y][x].underWater)
                    return false;
            }
        }
        return true;
    }

    public void onHover(Graphics2D graphics2D) {
        Field field = getHoveredField();
        if (field != null) {
            field.onHover(graphics2D);
        }
    }

    private Field getHoveredField() {
        MouseListener mouseListener = StageManager.getMouseListener();
        for (Field[] fields : gameBoardField) {
            for (Field field : fields) {
                if (field.isMouseOver(mouseListener)) {
                    return field;
                }
            }
        }
        return null;
    }

    public void placeBuilding(GameResources gameResources, BuildingType buildingType) {
        Field field = getHoveredField();
        if (field != null) {
            field.trySetBuildingType(buildingType, gameResources);
        }
    }

    public void render(Graphics2D graphics2D) {
        for (Field[] fields: gameBoardField) {
            for (Field field : fields) {
                field.render(graphics2D);
            }
        }
        updateConnectors();
        for (int y = 0; y < connected.length; y++) {
            for (int x = 0; x < connected[0].length; x++) {
                if (connected[y][x].connectedRight) {
                    int xPos = gameBoardField[y][x].getX();
                    int yPos = gameBoardField[y][x].getY();
                    int width = gameBoardField[y][x].getWidth();
                    int height = gameBoardField[y][x].getHeight();
                    graphics2D.setColor(Color.LIGHT_GRAY);
                    graphics2D.fillRect(xPos + width - 5, yPos + height/2, 9, 5);
                    graphics2D.setColor(Color.GRAY);
                    graphics2D.fillRect(xPos + width - 5, yPos + height/2 + 4, 9, 1);
                }
                if (connected[y][x].connectedDown) {
                    int xPos = gameBoardField[y][x].getX();
                    int yPos = gameBoardField[y][x].getY();
                    int width = gameBoardField[y][x].getWidth();
                    int height = gameBoardField[y][x].getHeight();
                    graphics2D.setColor(Color.LIGHT_GRAY);
                    graphics2D.fillRect(xPos + width/2, yPos + height - 5, 5, 9);
                    graphics2D.setColor(Color.GRAY);
                    graphics2D.fillRect(xPos + width/2 + 4, yPos + height - 5, 1, 9);
                }
            }
        }
    }

    private void updateConnectors() {
        for (int y = 0; y < BOARD_SIZE_IN_CELLS; y++) {
            for (int x = 0; x < BOARD_SIZE_IN_CELLS; x++) {
                if (connected[y][x] == null) {
                    connected[y][x] = new Connector(false, false);
                } else {
                    connected[y][x].connectedDown = false;
                    connected[y][x].connectedRight = false;
                }
                if (gameBoardField[y][x].buildingType != BuildingType.empty) {
                    if ((x + 1 != BOARD_SIZE_IN_CELLS) && (gameBoardField[y][x].buildingType == gameBoardField[y][x + 1].buildingType)) {
                        connected[y][x].connectedRight = true;
                    }
                    if ((y + 1 != BOARD_SIZE_IN_CELLS) && (gameBoardField[y][x].buildingType == gameBoardField[y + 1][x].buildingType)) {
                        connected[y][x].connectedDown = true;
                    }
                }
            }
        }
    }

    private class Connector {
        boolean connectedDown = false;
        boolean connectedRight = false;

        public Connector() {

        }

        public Connector(boolean connectDown, boolean connectRight) {
            this.connectedDown = connectDown;
            this.connectedRight = connectRight;
        }
    }

}
