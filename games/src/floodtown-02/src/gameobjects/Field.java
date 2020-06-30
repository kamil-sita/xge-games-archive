package gameobjects;

import game.*;
import system.Settings;
import graphics.SimpleRenderer;
import graphics.SpriteLoader;
import graphics.transforms.CacheableTransforms;
import interactions.GameObject;
import org.jetbrains.annotations.NotNull;
import sound.FloodtownMusicPlayer;
import sound.SoundEffect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Field extends GameObject {

    boolean underWater;
    boolean isRock;
    BasicFieldType basicFieldType;
    BuildingType buildingType = BuildingType.empty;
    BufferedImage sprite = SpriteLoader.load(Sprites.grassLand);
    int offsetDrawingYBecauseHover = 0;
    int animationIteration = 0;
    double scale = GameBoard.CELL_SIZE_IN_PIXELS/256.0;

    private Field(boolean isUnderwater, BasicFieldType cityFieldType) {
        setInterestedInMouse(true);
        this.underWater = isUnderwater;
        this.basicFieldType = cityFieldType;
        setWidth(GameBoard.CELL_SIZE_IN_PIXELS);
        setHeight(GameBoard.CELL_SIZE_IN_PIXELS);

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
        if (buildingType == null) return false;
        if (underWater || gameResources.money < 10 || this.buildingType == buildingType || isRock) {
            return false;
        }
        this.buildingType = buildingType;
        gameResources.money -= 10;
        FloodtownMusicPlayer.INSTANCE.play(SoundEffect.placing);
        return true;
    }

    public void setPosition(int x, int y) {
        setXPos(x);
        setYPos(y);
    }

    public void setUnderWater() {
        updateSprite();
        buildingType = BuildingType.empty;
        underWater = true;
    }

    public void onHover() {
        offsetDrawingYBecauseHover = -3;
    }

    public void render(@NotNull Graphics2D graphics2D) {
        updateSprite();
        double wave = 0;
        if (underWater) {
            wave = 1.4 * Math.sin(animationIteration/60.0 + getXPos() * 3 + getYPos() * 3) + 1.4;
        }
        SimpleRenderer.INSTANCE.renderImage(graphics2D, sprite, getXPos(), getYPos() + offsetDrawingYBecauseHover + wave, 1);
        offsetDrawingYBecauseHover = 0;
    }

    //must be called for iteration 0, 1, 2, 3, on every floodtown.main loop tick
    //it is because if on previous fields there was no power generator, but there will be on next field,
    //floodtown.game won't know that
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
                if (iteration == 2) {
                    gameResources.housing += 2 + connectionBonus;
                }
                break;
            case factory:
                if (currentBonus == BonusType.goodEconomy) modifier = 1.4;
                if (gameResources.getAvailablePower() > 0 && gameResources.getUnemployed() > 0 && iteration == 3) {
                    gameResources.usedPower += 1;
                    gameResources.workingInhabitants += 1;
                    if (addMoney) gameResources.money += (int) (1 * modifier) + connectionBonus;
                    if (gameResources.getUnemployed() > 0) {
                        gameResources.usedPower += 1;
                        gameResources.workingInhabitants += 1;
                        if (addMoney) gameResources.money += (int) (2 * modifier) + connectionBonus;
                    }
                } else if (gameResources.getUnemployed() > 0 && iteration == 3) {
                    gameResources.usedPower += 1;
                    gameResources.workingInhabitants += 1;
                    if (addMoney) gameResources.money += 1;
                }
                break;
        }
    }

    private void updateSprite() {
        sprite = CacheableTransforms.INSTANCE.scale(SpriteLoader.load(Sprites.noSprite), scale, CacheableTransforms.Quality.Best);
        if (underWater) {
            setSpriteToLoadedScaledDown(Sprites.underWater);
            return;
        }

        if (isRock) {
            setSpriteToLoadedScaledDown(Sprites.rock);
            return;
        }
        if (buildingType == BuildingType.empty) {
            switch (basicFieldType) {
                case plain:
                    setSpriteToLoadedScaledDown(Sprites.grassLand);
                    break;
                case bonusFarm:
                    setSpriteToLoadedScaledDown(Sprites.extraFarm);
                    break;
                case bonusPower:
                    setSpriteToLoadedScaledDown(Sprites.extraPower);
                    break;
            }
            return;
        }
        switch (buildingType) {
            case powerGenerator:
                if (basicFieldType == BasicFieldType.bonusPower) {
                    setSpriteToLoadedScaledDown(Sprites.coalPlantPlus);
                } else {
                    setSpriteToLoadedScaledDown(Sprites.coalPlant);
                }
                break;
            case farm:
                if (basicFieldType == BasicFieldType.bonusFarm) {
                    setSpriteToLoadedScaledDown(Sprites.farmPlus);
                } else {
                    setSpriteToLoadedScaledDown(Sprites.farm);
                }
                break;
            case housing:
                setSpriteToLoadedScaledDown(Sprites.house);
                break;
            case factory:
                setSpriteToLoadedScaledDown(Sprites.factory);
                break;
        }
    }

    private void setSpriteToLoadedScaledDown(String spriteName) {
        sprite =  CacheableTransforms.INSTANCE.scale(SpriteLoader.load(spriteName), scale, CacheableTransforms.Quality.Best);
    }

    public String toString() {
        return "Field: under water?:" + underWater;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public BasicFieldType getBasicFieldType() {
        return basicFieldType;
    }
}
