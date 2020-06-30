package system;

import graphics.RenderingManager;

import java.awt.*;
import java.awt.image.BufferStrategy;

public final class GameLoop implements Runnable {

    public static final double NANOSECONDS_IN_SECOND = 1_000_000_000.0;

    private Window window;
    private StageManager stageManager;

    public GameLoop(Window window, StageManager stageManager) {
        this.window = window;
        this.stageManager = stageManager;
    }

    private static double fps;
    private static double ups;

    private boolean running = true;

    /**
     * Main game method, that calls everything else (rendering, floodtown.game logic, synchronizing)
     */
    @Override
    public void run() {

        long now = System.nanoTime();
        long lastEndingTime = System.nanoTime();

        updateLoop();

        //graphics and interactions loop
        while (running) {
            if (nextUpateWithRate(Settings.targetFramerate, now)) {
                now = System.nanoTime();
                window.getInteractionManager().update();
                render();
                fps = NANOSECONDS_IN_SECOND / (System.nanoTime() - lastEndingTime);
                lastEndingTime = System.nanoTime();
            }
        }
    }

    private void updateLoop() {
        new Thread(() -> {
            long now = System.nanoTime();
            long lastEndingTime = System.nanoTime();

            while (running) {
                if (nextUpateWithRate(Settings.targetUpdateRate, now)) {
                    now = System.nanoTime();
                    stageManager.tick();
                    ups = NANOSECONDS_IN_SECOND / (System.nanoTime() - lastEndingTime);
                    lastEndingTime = System.nanoTime();
                }
            }
        }).start();
    }

    private boolean nextUpateWithRate(int rate, long now) {
        return System.nanoTime() > NANOSECONDS_IN_SECOND / rate + now;
    }


    /**
     * Method that sets renderingHints depending on set quality of rendering and calls methods responsible for rendering graphics
     * of various floodtown.game objects and showing them on screen
     */
    public void render() {
        Canvas canvas = window.getCanvas();
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

        setRenderingHintsAccordingToSettings(graphics);

        RenderingManager renderer = window.getRenderingManager();

        if (renderer.hasThingsToRender()) {
            window.superPaintGraphics(graphics);
            renderer.renderScreen(graphics);
        }

        graphics.dispose();
        bufferStrategy.show();
    }

    private void setRenderingHintsAccordingToSettings(Graphics2D graphics) {
        Settings.applySettings(graphics);
    }


    /**
     * Returns frame per second count calculated by loop in run() method.
     */
    public static double getFps() {
        return fps;
    }

    public static double getUps() {
        return ups;
    }
}
