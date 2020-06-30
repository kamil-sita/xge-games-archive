package game;

public class Position {
    public double x;
    public double y;

    public Position() {

    }

    public Position (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void copyPositionOnto(Position position) {
        position.x = x;
        position.y = y;
    }
}
