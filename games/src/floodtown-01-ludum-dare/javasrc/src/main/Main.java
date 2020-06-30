package main;

import game.BasicGameLogic;
import game.Settings;

public final class Main {
    public static void main(String[] args) {

        int widthFix = 0;
        int heightFix = 0;

        if (args.length >= 2) {
            try {
                widthFix = Integer.parseInt(args[0]);
                heightFix = Integer.parseInt(args[1]);
            } catch (Exception e) {
                badArgs();
            }
            BasicGameLogic.windowWidth += widthFix;
            BasicGameLogic.windowHeight += heightFix;
        }
        if (args.length >= 3) {
            try {
                Settings.HIGH_QUALITY_RENDERING = Boolean.parseBoolean(args[2]);
            } catch (Exception e) {
                badArgs();
            }
        }

        for (String s : args) {
            System.out.println(s);
        }

        System.setProperty("sun.java2d.translaccel", "True");
        System.setProperty("sun.java2d.opengl", "True");

        BasicGameLogic game = new BasicGameLogic();
        new Thread(game).start();
    }

    private static void badArgs() {
        System.out.println("Check passed arguments. Correct format:");
        System.out.println("floodtown.jar extraWidth extraHeight renderHighQuality");
    }
}
