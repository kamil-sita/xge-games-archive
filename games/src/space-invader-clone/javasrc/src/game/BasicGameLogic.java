package game;

import graphics.Hud;
import graphics.RenderingManager;
import main.KeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.Timer;
import java.util.TimerTask;

public final class BasicGameLogic extends JFrame implements Runnable {

    public static final int WINDOW_WIDTH = 1600;
    public static final int WINDOW_HEIGHT = 900;

    public static final int TARGET_UPDATERATE = 60;
    public static final double NANOSECONDS_IN_SECOND = 1_000_000_000.0;
    public static final double MILISECONDS_IN_SECOND = 1_000.0;

    private Canvas canvas = new Canvas();
    private RenderingManager renderer;
    private StageManager stageManager;

    public BasicGameLogic() {
        setTitle("Cosmic adventure");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);

        setLocationRelativeTo(null); //relative to center of the screen

        add(canvas);

        setVisible(true);

        var hud = new Hud();
        renderer = new RenderingManager(hud);

        canvas.createBufferStrategy(3);

        var keyListener = new KeyListener();
        canvas.addKeyListener(keyListener);
        stageManager = new StageManager(keyListener, renderer, hud);

    }

    /**
     * Method that sets renderingHints depending on set quality of rendering and calls methods responsible for rendering graphics
     * of various game objects and showing them on screen
     */
    public void render() {
        var bufferStrategy = canvas.getBufferStrategy();
        var graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        if (Settings.HIGH_QUALITY_RENDERING) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        }

        super.paint(graphics);
        renderer.renderScreen(graphics);
        graphics.dispose();
        bufferStrategy.show();
    }

    //At the start of the app, before game files are loaded, nothing will render.
    //This boolean decides whether to show information screen about loading resources
    private boolean areResourcesProbablyInitialized = false;

    private static double fps;
    private static double ups;

    private int framesProcessed = 0;
    private int framesThatShouldBeProcessed = 1;

    /**
     * Main game method, that calls everything else (rendering, game logic, synchronizing)
     */
    @Override
    public void run() {
        long lastFrameRenderTime = System.nanoTime();
        long lastFrameUpdateTime = System.nanoTime();
        final double renderUpdateTimeInMiliseconds = MILISECONDS_IN_SECOND / TARGET_UPDATERATE;

        if (!areResourcesProbablyInitialized) {
            areResourcesProbablyInitialized = true;
            showLoadingScreen();
        }

        var timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                framesThatShouldBeProcessed++;
            }
        }, 0, (long) (renderUpdateTimeInMiliseconds));

        //main game loop
        while (true) {
            while (framesProcessed < framesThatShouldBeProcessed) {
                long now = System.nanoTime();
                stageManager.update();
                framesProcessed++;
                ups = NANOSECONDS_IN_SECOND / (now - lastFrameUpdateTime); //calculating ups
                lastFrameUpdateTime = now;
            }
            long now = System.nanoTime();
            render(); //drawing onto screen
            fps = NANOSECONDS_IN_SECOND / (now - lastFrameRenderTime); //calculating fps
            lastFrameRenderTime = now;

        }
    }


    /**
     * Screen that informs user that game is loading
     */
    private void showLoadingScreen() {
        var bufferStrategy = canvas.getBufferStrategy();
        var graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        super.paint(graphics);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Loading resources. Please wait. ", 300, 300);
        graphics.drawString("Tip: press L to change rendering mode", 300, 350);

        super.paint(graphics);
        graphics.dispose();
        bufferStrategy.show();
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
