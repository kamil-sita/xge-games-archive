package gameobjects;

import graphics.SpriteLoader;
import graphics.GameObject;
import sound.MusicPlayer;
import sound.SoundEffect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Field extends GameObject {

    boolean underWater;
    boolean isRock;
    BasicFieldType basicFieldType;
    BuildingType buildingType = BuildingType.empty;
    BufferedImage sprite = SpriteLoader.load(SpriteLoader.grassLand);
    int offsetDrawingYBecauseHover = 0;
    int animationIteration = 0;

    private Field(boolean isUnderwater, BasicFieldType cityFieldType) {
        this.underWater = isUnderwater;
        this.basicFieldType = cityFieldType;
        width = GameBoard.CELL_SIZE_IN_PIXELS;
        height = GameBoard.CELL_SIZE_IN_PIXELS;
    }

    public static Field newLandField(int x, int y) {
        double r = Math.random();
        Field field = new Field(false, BasicFieldType.plain);
        field.isRock = r < 0.075;
        if (r < 0.9) {
            field.basicFieldType = BasicFieldType.plain;
        } else if (r < 0.95) {
            field.basicFieldType = BasicFieldType.bonusFarm;
        } else {
            field.basicFieldType = BasicFieldType.bonusPower;
        }
        field.setPosition(x, y);
        return field;
    }


    public boolean trySetBuildingType(BuildingType buildingType, GameResources gameResources) {
        if (underWater || gameResources.money < 10 || this.buildingType == buildingType || isRock) {
            return false;
        }
        this.buildingType = buildingType;
        gameResources.money -= 10;
        MusicPlayer.play(SoundEffect.placing);
        return true;
    }

    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    public void setUnderWater() {
        updateSprite();
        buildingType = BuildingType.empty;
        underWater = true;
    }

    public void onHover(Graphics2D graphics2D) {
        offsetDrawingYBecauseHover = -3;
    }

    public void render(Graphics2D graphics2D) {
        double scale = GameBoard.CELL_SIZE_IN_PIXELS/256.0;
        updateSprite();
        AffineTransform positionAndScalingTransform = new AffineTransform();
        double wave = 0;
        if (underWater) {
            wave = 1.4 * Math.sin(animationIteration/60.0 + xPos * 3 + yPos * 3) + 1.4;
        }
        positionAndScalingTransform.translate(xPos, yPos + offsetDrawingYBecauseHover + wave);
        offsetDrawingYBecauseHover = 0;
        positionAndScalingTransform.scale(scale, scale);
        graphics2D.drawImage(sprite, positionAndScalingTransform, null);
    }

    //must be called for iteration 0, 1, 2, 3, on every main loop update
    //it is because if on previous fields there was no power generator, but there will be on next field,
    //game won't know that
    public void updateGameResources(GameResources gameResources, boolean addMoney, int iteration, BonusType currentBonus, int connectionBonus) {
        animationIteration++;
        double modifier = 1;
        switch (buildingType) {
            case powerGenerator:
                if (iteration == 0) {
                    if (currentBonus == BonusType.superCoal) modifier = 2;
                    gameResources.producedPower += 5 * modifier + connectionBonus;
                    if (basicFieldType == BasicFieldType.bonusPower) {
                        gameResources.producedPower += 5 * modifier;
                    }
                }
                break;
            case farm:
                if (gameResources.getAvailablePower() > 0 && iteration == 1) {
                    if (currentBonus == BonusType.superCows) modifier = 2;
                    gameResources.usedPower += 2;
                    gameResources.producedFood += 2 * modifier + connectionBonus;
                    if (basicFieldType == BasicFieldType.bonusFarm) {
                        gameResources.producedFood += 2 * modifier;
                    }
                }
                break;
            case housing:
                if (gameResources.getAvailablePower() > 0 && iteration == 2) {
                    gameResources.usedPower += 2;
                    gameResources.housing += 2 + connectionBonus;
                }
                break;
            case office:
                if (currentBonus == BonusType.goodEconomy) modifier = 1.4;
                if (gameResources.getAvailablePower() > 0 && gameResources.getUnemployed() > 0 && iteration == 3) {
                    gameResources.usedPower +=2;
                    gameResources.workingInhabitants += 2;
                    if (addMoney) gameResources.money += (int) (3 * modifier) + connectionBonus;
                } else if (gameResources.getUnemployed() > 0 && iteration == 3) {
                    gameResources.usedPower +=2;
                    gameResources.workingInhabitants += 2;
                    if (addMoney) gameResources.money += 1;
                }
                break;
        }
    }

    private void updateSprite() {
        sprite = SpriteLoader.load(SpriteLoader.noSprite);
        if (underWater) {
            sprite = SpriteLoader.load(SpriteLoader.underWater);
            return;
        }

        if (isRock) {
            sprite = SpriteLoader.load(SpriteLoader.rock);
            return;
        }
        if (buildingType == BuildingType.empty) {
            switch (basicFieldType) {
                case plain:
                    sprite = SpriteLoader.load(SpriteLoader.grassLand);
                    break;
                case bonusFarm:
                    sprite = SpriteLoader.load(SpriteLoader.extraFarm);
                    break;
                case bonusPower:
                    sprite = SpriteLoader.load(SpriteLoader.extraPower);
                    break;
            }
            return;
        }
        switch (buildingType) {
            case powerGenerator:
                sprite = SpriteLoader.load(SpriteLoader.coalPlant);
                if (basicFieldType == BasicFieldType.bonusPower) {
                    sprite = SpriteLoader.load(SpriteLoader.coalPlantPlus);
                }
                break;
            case farm:
                sprite = SpriteLoader.load(SpriteLoader.farm);
                if (basicFieldType == BasicFieldType.bonusFarm) {
                    sprite = SpriteLoader.load(SpriteLoader.farmPlus);
                }
                break;
            case housing:
                sprite = SpriteLoader.load(SpriteLoader.house);
                break;
            case office:
                sprite = SpriteLoader.load(SpriteLoader.office);
                break;
        }
    }

    public String toString() {
        return "Field: under water?:" + underWater;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
