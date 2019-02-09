public class Position {
    double x = 0.;
    double y = 0.;

    public static Position add(Position p1, Position p2) {
        Position pos = new Position();
        pos.x = p1.x + p2.x;
        pos.y = p1.y + p2.y;

        return pos;
    }
}