package system;

import graphics.RenderingManager;
import graphics.DebugHud;
import interactions.InteractionManager;
import interactions.listeners.InteractionListener;

import javax.swing.*;
import java.awt.*;

public final class Window extends JFrame {

    private JPanel jPanel = new JPanel();
    private Canvas canvas = new Canvas();

    private InteractionManager interactionManager;
    private RenderingManager renderingManager;

    public Window(final String title) {
        setTitle(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        jPanel.add(canvas);
        jPanel.setPreferredSize(new Dimension(Settings.windowWidth, Settings.windowHeight));
        getContentPane().add(jPanel);
        pack();

        InteractionListener interactionListener = new InteractionListener();
        canvas.addKeyListener(interactionListener.getKeyListener());
        canvas.addMouseListener(interactionListener.getMouseListener());
        canvas.addMouseMotionListener(interactionListener.getMouseListener());

        canvas.setSize(Settings.windowWidth, Settings.windowHeight);

        interactionManager = new InteractionManager(interactionListener);

        renderingManager = new RenderingManager().setDebugHud(new DebugHud(new Settings(), interactionListener));

        setLocationRelativeTo(null);

        setVisible(true);

        canvas.createBufferStrategy(3);
    }

    public InteractionManager getInteractionManager() {
        return interactionManager;
    }

    public RenderingManager getRenderingManager() {
        return renderingManager;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void superPaintGraphics(Graphics2D graphics2D) {
        super.paint(graphics2D);
    }
}
