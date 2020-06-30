package gameObjects;

import game.GameplayStage;
import game.Position;
import graphics.RenderPriority;

import java.awt.*;
import java.awt.geom.AffineTransform;

public final class ParallaxEntity extends Entity {

    private double parallaxDepth;

    public ParallaxEntity(GameplayStage gameplayStage, Position position) {
        super(gameplayStage, position);
    }

    @Override
    public void render(Graphics2D graphics2D, boolean isDebugging) {
        if (isVisible && entityImage != null) {
            onScreenPosition = new Position();
            onScreenPosition.x = position.x;
            onScreenPosition.y = position.y;
            camera.applyCamera(onScreenPosition);
            onScreenPosition.x -= entityImage.getWidth()/2.0;
            onScreenPosition.y -= entityImage.getHeight()/2.0;

            parallaxDepth = getRandomDepth();

            //aplying parallax
            onScreenPosition.x += (position.x - camera.getPosition().x) * parallaxDepth;
            onScreenPosition.y += (position.y - camera.getPosition().y) * parallaxDepth;
            AffineTransform t = new AffineTransform();
            t.translate(onScreenPosition.x, onScreenPosition.y);
            graphics2D.drawImage(entityImage, t, null);

        }
    }

    public RenderPriority getRenderPriority() {
        return RenderPriority.background;
    }

    public void setParallaxDepth(double parallaxDepth) {
        this.parallaxDepth = parallaxDepth;
    }

    private double getRandomDepth() {
        return 2 * Math.random() * Math.random() * Math.random() - 0.4;
    }
}
