package graphics;

import main.MouseListener;

import java.awt.*;

public abstract class GameObject {

    protected int xPos;
    protected int yPos;
    protected int width;
    protected int height;

    public abstract void onHover(Graphics2D graphics2D);
    public abstract void render(Graphics2D graphics2D);

    public boolean isMouseOver(MouseListener mouseListener) {
        int mouseX = mouseListener.x;
        int mouseY = mouseListener.y;
        boolean xCorrect = mouseX >= xPos && mouseX < xPos + width;
        boolean yCorrect = mouseY >= yPos && mouseY < yPos + height;
        return xCorrect && yCorrect;
    }
}
