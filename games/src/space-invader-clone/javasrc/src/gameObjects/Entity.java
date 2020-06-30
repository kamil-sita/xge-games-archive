package gameObjects;

import game.BaseGameplayStage;
import game.Position;
import graphics.RenderPriority;
import graphics.Renderable;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Entity implements Renderable, Removable {

    protected BufferedImage entityImage = null;
    protected boolean isVisible = true;
    protected BaseGameplayStage baseGameplayStage;
    protected Camera camera;
    protected Position position;
    protected boolean renderOnTop = false;
    protected Position onScreenPosition;
    protected StringBuilder additionalDebug = new StringBuilder();
    private double rotation = 0;

    public Entity(BaseGameplayStage baseGameplayStage, Position position) {
        this.baseGameplayStage = baseGameplayStage;
        this.camera = this.baseGameplayStage.getCamera();
        this.position = position;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void removeIfNeeded() {
        if (isSetToBeRemoved()) {
            baseGameplayStage.removeEntity(this);
        }
    }

    @Override
    public void render(Graphics2D graphics2D, boolean isDebugging) {

        onScreenPosition = new Position();
        onScreenPosition.x = position.x;
        onScreenPosition.y = position.y;
        camera.applyCamera(onScreenPosition);
        onScreenPosition.x -= entityImage.getWidth()/2.0;
        onScreenPosition.y -= entityImage.getHeight()/2.0;

        var posX = (int) onScreenPosition.x;
        var posY = (int) onScreenPosition.y;

        if (isVisible && entityImage != null) {

            AffineTransform transform = new AffineTransform();

            transform.translate(onScreenPosition.x, onScreenPosition.y);
            transform.translate(entityImage.getWidth()/2, entityImage.getHeight()/2);
            transform.rotate(rotation);
            transform.translate(-entityImage.getWidth()/2, -entityImage.getHeight()/2);

            graphics2D.drawImage(entityImage, transform, null);

        }

        if (isDebugging) {
            debugRender(graphics2D, posX, posY);
        }
    }

    protected void debugRender(Graphics2D graphics2D, int posX, int posY) {

        var context = graphics2D.getFontRenderContext();
        graphics2D.setFont(new Font("Courier", Font.PLAIN, 11));
        var font = graphics2D.getFont();

        String debugInfo = "Position: " + ((int)position.x) + " : " + ((int)position.y) + ", on screen position: " + posX + " : " + posY + ". ";
        if (additionalDebug != null) {
            debugInfo += additionalDebug.toString();
        }

        var bounds = font.getStringBounds(debugInfo, context);

        int debugMessagePosX = posX + entityImage.getWidth();
        int debugMessagePosY = posY + entityImage.getHeight();

        graphics2D.setColor(Color.BLUE);
        graphics2D.setStroke(new BasicStroke(1.0f));
        graphics2D.drawRect(posX, posY, entityImage.getWidth(), entityImage.getHeight());
        graphics2D.setColor(Color.RED);
        graphics2D.fillRect(debugMessagePosX, debugMessagePosY, (int) bounds.getWidth() + 1, (int) bounds.getHeight() + 1);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(debugInfo, debugMessagePosX, debugMessagePosY + (int) bounds.getHeight() - 1);
    }

    public RenderPriority getRenderPriority() {
        return RenderPriority.foregroundTop;
    }

    public boolean isRenderOnTop() {
        return renderOnTop;
    }

    public void setRenderOnTop(boolean renderOnTop) {
        this.renderOnTop = renderOnTop;
    }

    public void setEntityImage(BufferedImage entityImage) {
        this.entityImage = entityImage;
    }

    public BufferedImage getEntityImage() {
        return entityImage;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public boolean isSetToBeRemoved() {
        return false;
    }
}
