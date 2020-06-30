package game;

import graphics.SpriteLoader;

import java.awt.image.BufferedImage;

public enum BonusType {
    none,
    superCows,
    superCoal,
    goodEconomy;

    public static  BonusType getRandomBonus() {
        double r = Math.random();
        if (r < 0.333333) {
            return superCows;
        } else if (r < 0.66666) {
            return goodEconomy;
        }
        return superCoal;
    }

    public String getText() {
        switch (this) {
            case none:
                return "No bonus.";
            case superCows:
                return "More food.";
            case superCoal:
                return "More power.";
            case goodEconomy:
                return "More money.";
        }
        return "unknown";
    }

    public BufferedImage getSprite() {
        switch (this) {
            case none:
                return SpriteLoader.load(Sprites.nothing);
            case superCows:
                return SpriteLoader.load(Sprites.farmPlus);
            case superCoal:
                return SpriteLoader.load(Sprites.coalPlantPlus);
            case goodEconomy:
                return SpriteLoader.load(Sprites.money);
        }
        return null;
    }
}
