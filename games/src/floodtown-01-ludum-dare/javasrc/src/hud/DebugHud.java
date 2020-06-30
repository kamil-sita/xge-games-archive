package hud;

import game.BasicGameLogic;
import game.Settings;
import game.StageManager;
import main.MouseListener;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public final class DebugHud {

    public DebugHud() {

    }

    final int FRAMES_TO_AVERAGE_OVER = 60;

    private double[] fps = new double[FRAMES_TO_AVERAGE_OVER];

    public void renderDebugInfo(Graphics2D graphics2D) {
        for (int i = 0; i + 1 < FRAMES_TO_AVERAGE_OVER; i++) {
            fps[i] = fps[i+1];
        }
        fps[FRAMES_TO_AVERAGE_OVER - 1] = BasicGameLogic.getFps();

        double averagedFps = 0;

        for (int i = 0; i < FRAMES_TO_AVERAGE_OVER; i++) {
            averagedFps += fps[i];
        }

        averagedFps /= FRAMES_TO_AVERAGE_OVER;

        graphics2D.setColor(new Color(255, 255, 255, 166));
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 12));
        if (Settings.SHOW_DEBUG) {
            graphics2D.drawString("FPS: " + String.format("%.1f", averagedFps), 5, 17);
            MouseListener mouseListener = StageManager.getMouseListener();
            graphics2D.drawString("Mouse pos: ("  + mouseListener.x + ":" + mouseListener.y + ")", 5, 43);
            graphics2D.drawString("isMousePressed: " + mouseListener.isClicking, 5, 56);
        }
    }
}
