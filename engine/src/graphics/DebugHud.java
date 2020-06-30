package graphics;

import system.GameLoop;
import system.Settings;
import interactions.listeners.InteractionListener;
import interactions.listeners.MouseListener;

import java.awt.*;

public final class DebugHud {

    private Settings settings;
    private InteractionListener interactionListener;

    public DebugHud(Settings settings, InteractionListener interactionListener) {
        this.settings = settings;
        this.interactionListener = interactionListener;
    }

    private final int FRAMES_TO_AVERAGE_OVER = 15;

    private double[] fps = new double[FRAMES_TO_AVERAGE_OVER];
    private double[] ups = new double[FRAMES_TO_AVERAGE_OVER];

    public void renderDebugInfo(Graphics2D graphics2D) {
        for (int i = 0; i + 1 < FRAMES_TO_AVERAGE_OVER; i++) {
            fps[i] = fps[i+1];
            ups[i] = ups[i+1];
        }
        fps[FRAMES_TO_AVERAGE_OVER - 1] = GameLoop.getFps();
        ups[FRAMES_TO_AVERAGE_OVER - 1] = GameLoop.getUps();

        double averagedFps = 0;
        double averagedUps = 0;

        for (int i = 0; i < FRAMES_TO_AVERAGE_OVER; i++) {
            averagedFps += fps[i];
            averagedUps += ups[i];
        }

        averagedFps /= FRAMES_TO_AVERAGE_OVER;
        averagedUps /= FRAMES_TO_AVERAGE_OVER;

        if (settings.showDebug) {
            graphics2D.setColor(new Color(255, 255, 255, 166));
            graphics2D.setFont(new Font("Arial", Font.PLAIN, 12));
            graphics2D.drawString("FPS: " + String.format("%.1f", averagedFps), 5, 17);
            graphics2D.drawString("UPS: " + String.format("%.1f", averagedUps), 5, 30);
            MouseListener mouseListener = interactionListener.getMouseListener();
            graphics2D.drawString("Mouse pos: ("  + mouseListener.x + ":" + mouseListener.y + ")", 5, 43);
            graphics2D.drawString("isMousePressed: " + mouseListener.isClicking, 5, 56);
        }
    }
}
