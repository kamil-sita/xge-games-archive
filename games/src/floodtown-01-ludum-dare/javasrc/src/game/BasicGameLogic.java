package game;

import hud.DebugHud;
import graphics.RenderingManager;
import main.KeyListener;
import main.MouseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public final class BasicGameLogic extends JFrame implements Runnable {

    public static int windowWidth = 1280;
    public static int windowHeight = 720;

    public static final double NANOSECONDS_IN_SECOND = 1_000_000_000.0;

    private Canvas canvas = new Canvas();
    private RenderingManager renderer;
    private StageManager stageManager;

    public BasicGameLogic() {
        setTitle("Floodtown");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(0, 0, windowWidth, windowHeight);
        setResizable(false);

        setLocationRelativeTo(null); //relative to center of the screen

        add(canvas);
        canvas.setSize(windowWidth, windowHeight);


        setVisible(true);

        DebugHud hud = new DebugHud();
        renderer = new RenderingManager(hud);

        canvas.createBufferStrategy(3);

        System.out.println(getWidth() + " " + getHeight());


        KeyListener keyListener = new KeyListener();
        MouseListener mouseListener = new MouseListener();
        canvas.addKeyListener(keyListener);
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
        stageManager = new StageManager(keyListener, mouseListener, renderer, hud);

    }

    /**
     * Method that sets renderingHints depending on set quality of rendering and calls methods responsible for rendering graphics
     * of various game objects and showing them on screen
     */
    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        if (Settings.HIGH_QUALITY_RENDERING) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        }

        if (renderer.hasThingsToRender()) {
            super.paint(graphics);
            renderer.renderScreen(graphics);
        }
        graphics.dispose();
        bufferStrategy.show();
    }

    //At the start of the app, before game files are loaded, grassLand will render.
    //This boolean decides whether to show information screen about loading resources
    private boolean areResourcesProbablyInitialized = false;

    private static double fps;


    /**
     * Main game method, that calls everything else (rendering, game logic, synchronizing)
     */
    @Override
    public void run() {

        if (!areResourcesProbablyInitialized) {
            areResourcesProbablyInitialized = true;
            showLoadingScreen();
        }

        long now = System.nanoTime();
        long lastEndingTime = System.nanoTime();

        //main game loop
        while (true) {
            if (System.nanoTime() > NANOSECONDS_IN_SECOND/60 + now) {
                now = System.nanoTime();
                stageManager.update();
                render(); //drawing onto screen
                fps = NANOSECONDS_IN_SECOND / (System.nanoTime() - lastEndingTime);
                lastEndingTime = System.nanoTime();
            }
        }
    }


    /**
     * Screen that informs user that game is loading
     */
    private void showLoadingScreen() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        super.paint(graphics);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, windowWidth, windowHeight);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Loading resources. Please wait. ", 300, 300);

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

}
