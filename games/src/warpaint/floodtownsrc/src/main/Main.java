package main;

import stages.WarpaintStageManager;
import system.GameLoop;
import system.Window;

public final class Main {
    public static void main(String[] args) {

        Window window = new Window("Warpaint 0.1");
        GameLoop game = new GameLoop(window, new WarpaintStageManager(window.getInteractionManager(), window.getRenderingManager()));

        System.setProperty("sun.java2d.translaccel", "True");
        System.setProperty("sun.java2d.opengl", "True");

        new Thread(game).start();
    }
}
