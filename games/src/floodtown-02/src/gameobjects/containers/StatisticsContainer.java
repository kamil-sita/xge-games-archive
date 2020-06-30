package gameobjects.containers;

import game.GameResources;
import game.Sprites;
import gameobjects.IconAndText;
import graphics.SpriteLoader;
import interactions.GameObject;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public final class StatisticsContainer extends GameObject {

    IconAndText powerBar;
    IconAndText humanBar;
    IconAndText workerBar;
    IconAndText coinBar;
    IconAndText foodBar;

    public StatisticsContainer() {
        powerBar = new IconAndText("Power: ?", SpriteLoader.load(Sprites.power), 1000, 205);
        humanBar = new IconAndText("Inhabitants: ?", SpriteLoader.load(Sprites.human), 1000, 75);
        coinBar  = new IconAndText("Money: ?", SpriteLoader.load(Sprites.money), 1000, 10);
        foodBar  = new IconAndText("Food: ?", SpriteLoader.load(Sprites.farm), 1000, 270);
        workerBar  = new IconAndText("Unemployed: ?", SpriteLoader.load(Sprites.unemployed), 1000, 140);
    }

    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        powerBar.render(graphics2D);
        humanBar.render(graphics2D);
        workerBar.render(graphics2D);
        coinBar.render(graphics2D);
        foodBar.render(graphics2D);
    }

    public void updateInformation(GameResources resources) {
        humanBar.setText("Inhabitants: " + resources.getInhabitants());
        humanBar.setLogicalColor(resources.getInhabitants() > 0 && resources.getUnemployed() != 0);

        powerBar.setText("Power: " + resources.getAvailablePower());
        powerBar.setLogicalColor(resources.getAvailablePower() >= 0);

        foodBar.setText("Food: " + resources.getAvailableFood());
        foodBar.setLogicalColor(resources.getAvailableFood() > 0);

        coinBar.setText("Money: " + resources.money);
        coinBar.setLogicalColor(resources.money >= 10);

        workerBar.setText("Unemployed: " + resources.getUnemployed());
        workerBar.setLogicalColor(resources.getUnemployed() == 0);
    }
}
