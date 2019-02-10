import org.opensourcephysics.controls.*;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

public class PlanetKApp extends AbstractSimulation {

  PlotFrame frame = new PlotFrame("x", "y", "Kepler Orbit");

  MovingTObject planet = new MovingTObject();

  /**
   * Constructs the PlanetKApp.
   */
  public PlanetKApp() {
    frame.setSize(600, 600);
    frame.addDrawable(planet);
    frame.setPreferredMinMax(-1.2, 1.2, -1.2, 1.2);
    frame.setSquareAspect(true);
  }

  /**
   * Steps the time.
   */
  public void doStep() {
    //planet.trail.clear();

    for (int i = 0; i < planet.nspeed; i++) { // do 10 steps between screen draws
      planet.doStep(1.0); // advances time
    } // end of for-loop

    frame.setMessage("t = " + decimalFormat.format(planet.t));
  }

  /**
   * Initializes the animation using the values in the control.
   */
  public void initialize() {

    planet.p.x = control.getDouble("x");
    planet.v.x = control.getDouble("vx");
    planet.p.y = control.getDouble("y");
    planet.v.y = control.getDouble("vy");
    planet.dt = control.getDouble("dt");
    planet.t = 0.0;
    planet.nspeed = control.getInt("nspeed");
    planet.trail.clear();
    frame.setMessage("t = 0");
  }

  /**
   * Resets animation to a predefined state.
   */
  public void reset() {
    control.setValue("x", 10.0);
    control.setValue("vx", 0.00);
    control.setValue("y", 0.00);
    control.setValue("vy", 0.10);
    control.setValue("dt", 0.10);
    control.setValue("nspeed", 10);
    initialize();
  }

  /**
   * Starts the Java application.
   * 
   * @param args command line parameters
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new PlanetKApp());
  }
}
