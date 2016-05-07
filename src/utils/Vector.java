package utils;

/**
 * Vector provides methods to deal with vectors with more
 * simplicity where speed is not essential.
 * @see V2D
 * @author Romain
 *
 */
public class Vector {
	
	public static final Vector X_AXIS = new Vector(1, 0);
	public static final Vector Y_AXIS = new Vector(0, 1);
	public static final Vector NULL = new Vector(0, 0);
	
	private double x;
	private double y;
	
	public Vector (double nx, double ny) {
		x = nx;
		y = ny;
	}
	
	public Vector (double r) {
		x = r;
		y = r;
	}
	
	public Vector (Angle a) {
		x = a.cos();
		y = a.sin();
	}
	
	public Vector(Point a, Point b) {
		x = b.getX() - a.getX();
		y = b.getY() - a.getY();
	}

	public Vector(Point point) {
		this(Point.ORIGIN, point);
	}

	public Vector(Vector v) {
		x = v.x;
		y = v.y;
	}

	public Vector(Segment s) {
		this(s.getP1(), s.getP2());
	}

	public Vector() {
		x = 0;
		y = 0;
	}

	public Vector addX (double nx) {
		x += nx;
		return this;
	}
	
	public Vector addY (double ny) {
		y += ny;
		return this;
	}
	
	public Vector add (double nx, double ny) {
		x += nx;
		y += ny;
		return this;
	}
	
	public Vector add (Vector b) {
		x += b.x;
		y += b.y;
		return this;
	}
	
	public Vector sub (Vector b) {
		x -= b.x;
		y -= b.y;
		return this;
	}
	
	public Vector getAdditionVector (Vector b) {
		return new Vector(x+b.x, y+b.y);
	}
	
	public Vector getInverseVector() {
		return new Vector(-x, -y);
	}
	
	public Vector multiply (double m) {
		x = x*m;
		y = y*m;
		return this;
	}
	
	public Vector multiply (double m1, double m2) {
		x = x*m1;
		y = y*m2;
		return this;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void set(double x, double y) {
		x = 0;
		y = 0;
	}
	
	public void set(Point a, Point b) {
		x = b.getX() - a.getX();
		y = b.getY() - a.getY();
	}
	
	public double getLength () {
		return Math.sqrt(x*x + y*y);
	}
	
	public Vector setLength (double length) {
		x *= length / Math.sqrt(x*x + y*y);
		y *= length / Math.sqrt(x*x + y*y);
		return this;
	}
	
	public double getA () {
		if (x == 0 && y == 0) {
			return Double.NEGATIVE_INFINITY;
		} else if (x == 0) {
			return Double.POSITIVE_INFINITY;
		} else {
			return y / x;
		}
	}
	
	public Vector getNormalizedVector () {
		double length =  Math.sqrt(x*x + y*y);
		double nx = x / length;
		double ny = y / length;
		return new Vector(nx, ny);
	}
	
	public Vector getRightNormal () {
		return new Vector(-y, x);
	}
	
	public Vector getLeftNormal () {
		return new Vector(y, -x);
	}
	
	public double getCrossProduct (Vector b) {
		return x*b.y - y*b.x;
	}
	
	public double getDotProduct (Vector b) {
		return b.x * x + b.y * y;
	}
	
	public Vector getProjection (Vector b) {
		if (b.x == 0 && b.y == 0)
			return new Vector();
		
		double dp = getDotProduct(b);
		double px = ( dp / (b.x*b.x + b.y*b.y) ) * b.x;
		double py = ( dp / (b.x*b.x + b.y*b.y) ) * b.y;
		return new Vector(px, py);
	}

	public Vector copy() {
		return new Vector(x, y);
	}

	public void toZero() {
		x = 0;
		y = 0;
	}
	
	public boolean isZero() {
		return x == 0 && y == 0;
	}

	public void set(Vector g) {
		x = g.x;
		y = g.y;
	}
	
	public String toString () {
		return "(" + x + ", " + y + ")";
	}
	
	public boolean equals (Vector v) {
		return x == v.x && y == v.y;
	}

	public double getDotProduct(Point point) {
		return point.getX() * x + point.getY() * y;
	}
}
