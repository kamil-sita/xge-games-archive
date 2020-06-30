package main;

import game.BasicGameLogic;

public final class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.translaccel", "True");
        System.setProperty("sun.java2d.opengl", "True");

        var game = new BasicGameLogic();
        new Thread(game).start();
    }
}
