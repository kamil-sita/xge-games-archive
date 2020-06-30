package gameObjects;

import game.Position;

public final class Camera {
    public Position position = new Position();
    private Entity followedEntity;

    private double xDiff;
    private double yDiff;

    public Camera () {
        position.x = 0;
        position.y = 0;
    }

    public void applyCamera(Position pos) {
        pos.x -= position.x;
        pos.y -= position.y;
    }

    public Position getPosition() {
        return position;
    }

    public void updatePos() {
        final double FOLLOWING_COEFFICIENT = 3; //the closer value is to 1, the more camera follows ship movement instead of staying in place
        if (followedEntity == null) return;
        position.x = (xDiff + followedEntity.getPosition().x)/FOLLOWING_COEFFICIENT;
        position.y = yDiff + followedEntity.getPosition().y;
    }

    public void setFollowedEntity(Entity e) {
        followedEntity = e;
        if (e != null) {
            xDiff = position.x - e.getPosition().x;
            yDiff = position.y - e.getPosition().y;
        }
    }



}
