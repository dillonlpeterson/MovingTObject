public class Velocity {
    double x = 0.;
    double y = 0.;

    public static Velocity add(Velocity v1, Velocity v2) {
        Velocity velocity = new Velocity();
        velocity.x = v1.x + v2.x;
        velocity.y = v1.y + v2.y;

        return velocity;
    }
}