package graphics;

import game.BasicGameLogic;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public final class Hud {

    private ArrayList<HudTask> taskQueue = new ArrayList<>();

    private int translucency = 192;

    public Hud() {

    }

    public void addHudInfoToQueue(String information, double time) {
        var hudTask = new HudTask();
        hudTask.taskType = TaskType.message;
        hudTask.string = information;
        hudTask.time = time;
        taskQueue.add(hudTask);
    }

    public void setHealthPercentageBar(double percent, String additionalInformation, BarType barType) {
        for (int i = 0; i < taskQueue.size(); i++) {
            var hudTask = taskQueue.get(i);
            if (hudTask.taskType == TaskType.bar && hudTask.barType == BarType.hp) {
                taskQueue.remove(i);
                i--;
            }
        }
        var hudTask = new HudTask();
        hudTask.taskType = TaskType.bar;
        hudTask.barType = barType;
        if (additionalInformation != null) hudTask.string = additionalInformation;
        hudTask.doubleValue = percent;
        taskQueue.add(hudTask);
    }

    /**
     * Renders all elements in queue
     * @param graphics2D graphics object
     */
    public void renderAllInQueue(Graphics2D graphics2D) {
        renderFpsAndUps(graphics2D);
        int hudInfoIndex = 0;
        for (HudTask task : taskQueue) {
            switch (task.taskType) {
                case message:
                    renderHudInfo(task, graphics2D, hudInfoIndex);
                    hudInfoIndex++;
                    break;
                case bar:
                    renderBar(graphics2D, getColor(ColorType.healthBar), 0, "HP: " + String.format("%3.1f", 100 * task.doubleValue), task.doubleValue);
                case other:
                    break;
            }
        }

        for (int i = 0; i < taskQueue.size(); i++) {
            if (taskQueue.get(i).removeAfterIteration) {
                taskQueue.remove(i);
                i--;
            }
        }

    }

    private int iteration = 0;
    private double fps;
    private double ups;

    private void renderFpsAndUps(Graphics2D graphics2D) {
        if (iteration % 4 == 0) {
            fps = BasicGameLogic.getFps();
            ups = BasicGameLogic.getUps();
        }
        graphics2D.setColor(new Color(255, 255, 255, 255));
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics2D.drawString("FPS: " + String.format("%.1f", fps), 5, 17);
        graphics2D.drawString("UPS: " + String.format("%.1f", ups), 5, 30);
        iteration++;
    }

    private void renderHudInfo(HudTask task, Graphics2D graphics2D, int index) {

        if (System.nanoTime() / BasicGameLogic.NANOSECONDS_IN_SECOND >= task.time + task.currentTime / BasicGameLogic.NANOSECONDS_IN_SECOND) {
            task.removeAfterIteration = true;
            return;
        }

        String message = task.string;

        final int POSITION_X = 50;
        final int POSITION_Y = 800 - index * 40;

        graphics2D.setFont(new Font("Arial", Font.PLAIN, 15));
        var font = graphics2D.getFont();

        var bounds = font.getStringBounds(message, graphics2D.getFontRenderContext());

        graphics2D.setColor(getColor(ColorType.main));
        graphics2D.fillRect(POSITION_X, POSITION_Y, (int) bounds.getWidth(), (int) bounds.getHeight());
        graphics2D.setColor(getColor(ColorType.additional));
        graphics2D.fillRect(POSITION_X, POSITION_Y + (int) bounds.getHeight(), (int) bounds.getWidth(), 6);
        graphics2D.setColor(getColor(ColorType.text));
        graphics2D.drawString(message, POSITION_X, POSITION_Y + (int) bounds.getHeight());
    }

    private void renderBar(Graphics2D graphics2D, Color color, int offSet, String information, double percent) {
        graphics2D.setColor(color);
        graphics2D.setFont(new Font("Arial", Font.BOLD, 18));
        graphics2D.drawString(information, 1400, 800);
        graphics2D.fillRect(1380, 820, (int) (percent * 150), 20);
    }


    private Color getColor(ColorType colorType) {
        switch (colorType) {
            case main:
                return new Color(43, 29, 92, translucency);
            case additional:
                return new Color(65, 49, 153, translucency);
            case text:
                return new Color(215, 215, 215, translucency);
            case healthBar:
                return new Color(240, 30, 30, translucency);
        }
        return null;
    }

    private class HudTask {
        long currentTime;
        TaskType taskType;
        String string;
        double doubleValue;
        double time;
        boolean removeAfterIteration = false;
        BarType barType;

        public HudTask() {
            currentTime = System.nanoTime();
        }
    }

    private enum TaskType {
        message, bar, other
    }

    private enum ColorType {
        main, additional, text, healthBar
    }

    public enum BarType {
        shield, hp
    }


}
