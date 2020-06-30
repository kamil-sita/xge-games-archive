package gameObjects;

import game.GameplayStage;
import game.Position;
import graphics.FlyweightGraphicsLoader;

import java.awt.image.BufferedImage;

public final class Mine extends PhysicalEntity {

    private int spriteId;
    private BufferedImage[] sprites;
    private double animationIteration;

    public Mine(GameplayStage gameplayStage, Position position) {
        super(gameplayStage, position, Double.POSITIVE_INFINITY);
        loadMineSprites();
        setCollisionScaling(0.25);
        spriteId = (int) (Math.random() * sprites.length);
        setEntityImage(sprites[spriteId]);
        animationIteration = 4 * Math.random();
    }

    public void updateAnimationAndCheckCollision(PhysicalEntity player) {
        animationUpdate();
        if (isColliding(player)) player.dealDamage(getHealthPoints());
    }

    public void animationUpdate() {
        if (shouldGetNextFrame()) {
            nextAnimationFrame();
        }
    }

    private final double MINIMUM_ITERATION_VALUE_FOR_NEXT_FRAME = 2.0;

    private boolean shouldGetNextFrame() {
        final double ITERATION_STEP = 1.15;
        animationIteration += ITERATION_STEP;
        return animationIteration >= MINIMUM_ITERATION_VALUE_FOR_NEXT_FRAME;
    }

    private void nextAnimationFrame() {
        animationIteration -= MINIMUM_ITERATION_VALUE_FOR_NEXT_FRAME;
        spriteId++;
        spriteId%=sprites.length;
        setEntityImage(sprites[spriteId]);
    }

    private void loadMineSprites() {
        final int MINE_SPRITES_COUNT = 95;
        sprites = new BufferedImage[MINE_SPRITES_COUNT];
        for (int i = 0; i < MINE_SPRITES_COUNT; i++) {
            sprites[i] = FlyweightGraphicsLoader.getFileFrom(("/entities/mine/" + String.format("%04d", i + 1) + ".png"));
        }
    }


}
