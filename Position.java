public class Position {
    double x = 0.;
    double y = 0.;

    public double r() {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    public static Position add(Position p1, Position p2) {
        Position pos = new Position();
        pos.x = p1.x + p2.x;
        pos.y = p1.y + p2.y;

        return pos;
    }

    public static Position sub(Position p1, Position p2) {
        Position pos = new Position();
        pos.x = p1.x - p2.x;
        pos.y = p1.y - p2.y;

        return pos;
    }
}