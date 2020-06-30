package main;

import stages.FloodtownStageManager;
import system.GameLoop;
import system.Window;

public final class Main {
    public static void main(String[] args) {

        Window window = new Window("Floodtown 0.2.1");
        GameLoop game = new GameLoop(window, new FloodtownStageManager(window.getInteractionManager(), window.getRenderingManager()));

        if (args.length >= 1) {
            System.out.println("Floodtown no longer uses command line arguments");
        }

        System.setProperty("sun.java2d.translaccel", "True");
        System.setProperty("sun.java2d.opengl", "True");

        new Thread(game).start();
    }
}
