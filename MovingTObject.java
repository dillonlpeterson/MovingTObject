import java.awt.*;
import org.opensourcephysics.display.*;

public class MovingTObject implements Drawable {

	double dt, t;
	double E;
	private double r, r22;

	int nspeed = 10;

	Position p;
	Velocity v;
	Acceleration a;

	Position r1;
	Position r2;

	double rcos, rsin, xrot, yrot;

	Trail trail = new Trail();

	public MovingTObject() {
		p = new Position();
		v = new Velocity();
		a = new Acceleration();

		r1 = new Position();
		r1.x = 0.5;
		r1.y = 0.0;

		r2 = new Position();
		r2.x = -0.5;
		r2.y = 0.0;
		System.out.println("A new moving object is created.");
	}

	// Set object's energy
	public void energy() {
		E = 0.5 * (v.x * v.x + v.y * v.y) - 1. / Math.sqrt(p.x * p.x + p.y * p.y);
	}

	// Implemented Restricted Three-Body, Time-Dependent Acceleration
	public void accel() {
		// Position r1
		r1.x = 0.5*Math.cos(t);
		r1.y = 0.5 * Math.sin(t);

		// Position r2
		r2.x = -r1.x;
		r2.y = -r1.y;

		Position d1 = new Position();
		d1 = Position.sub(p, r1);

		Position d2 = new Position();
		d2 = Position.sub(p, r2);

		a.x = -0.5 * (d1.x/(Math.pow(d1.r(),3)) + d2.x/(Math.pow(d2.r(),3)));
		a.y = -0.5 * (d1.y/(Math.pow(d1.r(),3)) + d2.y/(Math.pow(d2.r(),3)));
	}

	// Increment position given timestep coefficient cof
	public void positionstep(double cof) {
		p.x = p.x + v.x * dt * cof;
		p.y = p.y + v.y * dt * cof;
	}

	// Increment velocity given timestep coefficient cof
	public void velocitystep(double cof) {
		accel();
		v.x = v.x + a.x * dt * cof;
		v.y = v.y + a.y * dt * cof;
	}

	// Simply calls velocitystep and positionstep within it.
	public void sym1astep(double cof) {
		velocitystep(cof);
		positionstep(cof);
	}

	// Basically the same as sym1astep, but order of methods is reversed.
	public void sym1bstep(double cof) {
		positionstep(cof);
		velocitystep(cof);
	}
	
	public void sym2astep(double cof) {
		sym1astep(0.5 * cof);
		sym1bstep(0.5 * cof);
	}

	// Modified sym2bstep to keep track of the time
	public void sym2bstep(double cof) {
		// Position step at dt/2 
		positionstep(0.5*cof);
		accel();
		t = t + 0.5 * cof * dt;
		// Velocity step at dt 
		velocitystep(cof);
		// Position step at dt/2
		positionstep(0.5*cof);

		t = t + 0.5 * cof * dt;
	}

	// Computes and returns Acceleration object given initial Acceleration and timestep coefficient cof.
	public Acceleration getAccel(double cof, Acceleration a0) {
		// Compute position given a coefficient for the timestep
		double x = p.x + v.x * dt * cof + 0.5 * Math.pow(cof*dt, 2) * a0.x;
		double y = p.y + v.y * dt * cof + 0.5 * Math.pow(cof*dt, 2) * a0.y;
		double r2 = p.x * p.x + p.y * p.y;
		double r = Math.sqrt(r2);

		Acceleration acc = new Acceleration();
		acc.x = -x / r / r2;
		acc.y = -y / r / r2;
		return acc;
	}

	// Runge Kutta 2nd Order Algoritm 
	public void RK2step(double cof) {
		Acceleration ax = getAccel(0.5*cof, a);

		// Find position after timestep dt.
		p.x = p.x + v.x*dt*cof + (1/2)*Math.pow(dt*cof,2)*a.x; 
		p.y = p.y + v.y*dt*cof + (1/2)*Math.pow(dt*cof,2)*a.y; 
		// Velocity Step using ax12 and ay12 as found above:

		v.x = v.x + ax.x * dt*cof;
		v.y = v.y + ax.y * dt*cof;

		accel();
	}
	
	// Runge Kutta 4th Order Algorithm
	public void RK4() {
		double s = Math.pow(2, 1/3);
		double eps = 1/(2-s);
		RK2step(eps * 0.5);
		RK2step(eps * 0.5);
		double p1x = p.x;
		double p1y = p.y;
		double p1vx = v.x;
		double p1vy = v.y;

		RK2step(eps);
		double p2x = p.x;
		double p2y = p.y;
		double p2vx = v.x;
		double p2vy = v.y;

		p.x = (4/3) * p1x - (1/3) * p2x;
		p.y = (4/3) * p1y - (1/3) * p2y;
		v.x = (4/3) * p1vx - (1/3) * p2vx;
		v.y = (4/3) * p1vy - (1/3) * p2vy;
	}

	// 4th order Forest-Ruthe.
	public void FR4() {
		double s = Math.pow(2, 1/3);

		double cof = (1/(2-s));

		// First Iteration
		velocitystep(0.5*cof);
		positionstep(cof);
		velocitystep(0.5*cof);

		// Second Iteration
		velocitystep(-s*0.5*cof);
		positionstep(-s * cof);
		velocitystep(-s * 0.5 * cof);

		// Third Iteration
		velocitystep(0.5 * cof);
		positionstep(cof);
		velocitystep(0.5 * cof);

	}

	public void doStep(double cof){
		this.sym2bstep(1.0);
		double rcos = Math.cos(t);
		double rsin = Math.sin(t);
		//xpix = panel.xToPix(p.x)-irad;
		//ypix = panel.yToPix(p.y)-irad;
		double xrot = rcos * p.x + rsin * p.y;
		double yrot = -rsin * p.x + rcos * p.y;
		trail.addPoint(xrot, yrot);
	 }
	 
	// Changed for co-rotating frame.
	public void draw(DrawingPanel panel, Graphics g) {
		// Create sun (Colored BLUE) at the position of r1
		// irad == Radius of Sun
		int irad=8;
		int xpix = panel.xToPix(0.5)-irad;
		int ypix = panel.yToPix(0.0)-irad;   
		g.setColor(Color.BLUE);
		g.fillOval(xpix, ypix, 2*irad, 2*irad);
		// Create another sun (Colored GREEN) at the position of r2
		xpix = panel.xToPix(-0.5) - irad;
		ypix = panel.yToPix(0.0) - irad;
		g.setColor(Color.GREEN);
		g.fillOval(xpix, ypix, 2*irad, 2*irad);
		irad=5;            //smaller moving planet
		xpix = panel.xToPix(xrot) - irad;
		ypix = panel.yToPix(yrot) - irad;
		g.setColor(Color.RED);
		g.fillOval(xpix, ypix, 2*irad, 2*irad);
		trail.draw(panel, g);
	}
}
