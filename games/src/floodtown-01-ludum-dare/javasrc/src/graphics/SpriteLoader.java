package graphics;

import game.ResourceIO;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class SpriteLoader {

    public static final String nothing = "/graphics/nothing.png";
    public static final String grassLand = "/graphics/grassLand.png";
    public static final String extraFarm = "/graphics/extraFarm.png";
    public static final String extraPower = "/graphics/extraPower.png";
    public static final String rock = "/graphics/rocks.png";
    public static final String underWater = "/graphics/underWater.png";
    public static final String noSprite = "/graphics/noSprite.png";

    public static final String farm = "/graphics/farm.png";
    public static final String farmPlus = "/graphics/farmPlus.png";
    public static final String office = "/graphics/office.png";
    public static final String coalPlant = "/graphics/coalplant.png";
    public static final String coalPlantPlus = "/graphics/coalplantPlus.png";
    public static final String house = "/graphics/home.png";

    public static final String unemployed = "/graphics/unemployed.png";

    public static final String human = "/graphics/human.png";
    public static final String power = "/graphics/power.png";
    public static final String money = "/graphics/coin.png";

    public static final String background = "/graphics/wood.png";

    public static final String clock0 = "/graphics/clock0.png";
    public static final String clock3 = "/graphics/clock3.png";
    public static final String clock6 = "/graphics/clock6.png";
    public static final String clock9 = "/graphics/clock9.png";
    public static final String clockArrow = "/graphics/clockArrow.png";

    public static final String overclock = "/graphics/overclock.png";

    public static final String tutorial = "/graphics/tutorial0.png";
    public static final String gotIt = "/graphics/gotit.png";

    public static final String vignette = "/graphics/vignette.png";
    public static final String noise = "/graphics/noise.png";

    public static final String paperWithTips = "/graphics/tldr.png";

    public static final String backShadow = "/graphics/readabilityShadow.png";

    public static final String tutorialIcon = "/graphics/tutorial.png";
    public static final String restartIcon = "/graphics/restart.png";

    public static final String kilroy = "/graphics/kilroy.png"; //was here

    public static final String trophy = "/graphics/trophy.png";
    public static final String gameOverBack = "/graphics/gameOverBack.png";

    private static HashMap<String, BufferedImage> graphics = new HashMap<>();

    public static BufferedImage load(String location) {
        if (graphics.get(location) == null) {
            graphics.put(location, ResourceIO.loadImg(location));
        }
        return graphics.get(location);
    }



}
