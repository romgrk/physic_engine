package utils;

/**
 * Angle provides methods to deal with angles in radians and degrees.
 * @author Romain
 *
 */
public class Angle {
	
	public static final double PI_5 	= 0.5 * Math.PI;
	public static final double PI 		= Math.PI;
	public static final double PI2 		= 2 * Math.PI;
	
	private double angle = 0;
	
	public static void main (String[]ar) {
		Angle a = new Angle(3*PI_5);
		Angle b = new Angle(3*PI);
		Angle c = new Angle(PI_5);
		Angle d = new Angle(-1.5*PI);
		System.out.println(a.value());
		System.out.println(b.value());
		System.out.println(c.value());
		System.out.println(d.value());
		
		System.out.println(a.valueInDeg());
		System.out.println(b.valueInDeg());
		System.out.println(c.valueInDeg());
		System.out.println(d.valueInDeg());
		
		Angle e = new Angle(1.0, 0.0);
		Angle f = new Angle(-1.0, 0.0);
		System.out.println(e);
		System.out.println(f);
	}
	
	public Angle (int degree) {
		setAngle(degree);
	}
	
	public Angle (double rad) {
		setAngle(rad);
	}
	
	public Angle (Point p) {
		this(p.getX(), p.getY());
	}
	
	public Angle (Vector v) {
		this(v.getX(), v.getY());
	}
	
	public Angle (double x, double y) {
		if (x == 0 && y == 0) {
			setAngle(0);
			return;
		}
		
		double h = Math.sqrt(x*x + y*y);
		double asin = Math.asin(y/h);
		Angle a = new Angle(asin);
		
		if ( x < 0 ) {
			if (asin > 0) {
				a.add(2 * (Math.PI/2 - a.value()));
			} else {
				a.add(-2 * (a.value() - 3*Math.PI/2));
			} 
		}
		
		setAngle(a);
	}
	
	public Angle(Angle a) {
		angle = a.value();
	}

	public Angle copy () {
		return new Angle(angle);
	}
	
	public Angle add (Angle a) {
		angle += a.angle;
		return this;
	}
	
	public Angle add (double rad) {
		angle += rad;
		return this;
	}
	
	public Angle add (int degree) {
		angle += degToRad(degree);
		return this;
	}
	
	public Angle sub (Angle a) {
		angle -= a.angle;
		return this;
	}
	
	public Angle sub (double rad) {
		angle -= rad;
		return this;
	}
	
	public Angle sub (int degree) {
		angle -= degToRad(degree);
		return this;
	}
	
	public double value () {
		return angle;
	}
	
	public int valueInDeg () {
		return radToDeg(angle);
	}

	public void setAngle(Angle a) {
		angle = a.value();
	}
	
	public void setAngle(double rads) {
		angle = rads;
	}
	
	public void setAngle(int degree) {
		angle = degToRad(degree);
	}

	public static int radToDeg (double rad) {
		return (int)(rad / 2 / Math.PI * 360);
	}

	public static double degToRad (int degree) {
		return (degree * 2 * Math.PI / 360);
	}

	public static double absoluteRad (double rad) {
		if (rad < 0) {
			while (rad < 0) {
				rad += PI2;
			}
			return rad;
		} else {
			return rad % (2*Math.PI);
		}
	}

	public static int absoluteDeg (int degree) {
		if (degree < 0) {
			while (degree < 0) {
				degree += 360;
			}
			return degree;
		} else {
			return degree % (360);
		}
	}
	
	public double cos () {
		return Math.cos(angle);
	}
	
	public double sin () {
		return Math.sin(angle);
	}
	
	public boolean equals (Angle a) {
		return (a.angle == angle);
	}
	
	public String toString () {
		return absoluteDeg(radToDeg(angle)) + "deg";
	}
}
