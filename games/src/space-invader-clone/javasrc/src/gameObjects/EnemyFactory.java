package gameObjects;

import game.GameplayStage;
import game.Position;
import graphics.FlyweightGraphicsLoader;

public class EnemyFactory {

    public static Enemy createAsteroidEnemy(GameplayStage gameplayStage, final Position position) {
        final double HEALTH_POINTS = 10 + 10 * Math.random();
        final double POSITION_VARIANCE_Y = 150;
        final double POSITION_VARIANCE_X = 1000;
        final double TIME_SHIFT_VARIANCE = 30;
        final double MAX_AMPLITUDE_1 = 100 + 30 * Math.random();
        final double MAX_AMPLITUDE_2 = 50 + 15 * Math.random();
        final double VIBRATION_SPEED_1 = 0.02 + 0.01 * Math.random();
        final double VIBRATION_SPEED_2 = 0.01 + 0.02 * Math.random();
        final double ASTEROID_SPEED = -4 + Math.random() * 0.1; //slightly lower than player
        final double ROTATION_SPEED = -0.005 + 0.01 * Math.random();

        var clonedPosition = new Position(position.x, position.y);

        clonedPosition.y += Math.random() * POSITION_VARIANCE_Y - POSITION_VARIANCE_Y/2;
        clonedPosition.x += Math.random() * POSITION_VARIANCE_X - POSITION_VARIANCE_X/2;

        Position ignoredPosition = new Position(0, 0); //it will be always overriden by originPosition, so it does not matter as long as it exists

        var enemy = new Enemy(gameplayStage, ignoredPosition, HEALTH_POINTS, FlyweightGraphicsLoader.getFileFrom(FlyweightGraphicsLoader.asteroid)) {
            private double iteration = TIME_SHIFT_VARIANCE * Math.random();
            private final Position originPosition = clonedPosition;

            @Override
            protected void positionUpdate() {
                setVisible(true);
                setRotation(iteration * ROTATION_SPEED);

                position.y = originPosition.y + iteration * ASTEROID_SPEED;
                position.x = originPosition.x + Math.sin(iteration * VIBRATION_SPEED_1) * MAX_AMPLITUDE_1 + Math.sin(iteration * VIBRATION_SPEED_2) * MAX_AMPLITUDE_2;
                iteration++;
            }
        };

        enemy.setCollisionScaling(0.85);
        return enemy;
    }

}
