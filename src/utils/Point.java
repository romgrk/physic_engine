package utils;

import java.io.Serializable;

/**
 * Point provides methods to deal with points in double format
 * @author Romain
 *
 */
public class Point implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3872702477580851244L;
	
	private double x;
	private double y;
	
	public static final Point ORIGIN = new Point(0, 0);
	
	public Point (double nx, double ny) {
		x = nx;
		y = ny;
	}
	
	public Point(Point p) {
		x = p.x;
		y = p.y;
	}

	public Point() {
		x = 0;
		y = 0;
	}
	
	public Point(java.awt.Point p) {
		if (p == null)
			return;
		x = p.x;
		y = p.y;
	}

	public Point(Vector v) {
		x = v.getX();
		y = v.getY();
	}

	public Point copy () {
		return new Point(x, y);
	}
	
	public Point addX (double nx) {
		x += nx;
		return this;
	}
	
	public Point addY (double ny) {
		y += ny;
		return this;
	}
	
	public Point add (double nx, double ny) {
		x += nx;
		y += ny;
		return this;
	}
	
	public Point add (Point p) {
		x += p.x;
		y += p.y;
		return this;
	}
	
	public Point sub (Point p) {
		x -= p.x;
		y -= p.y;
		return this;
	}
	
	public Point add (Vector v) {
		x += v.getX();
		y += v.getY();
		return this;
	}
	
	public Point sub(Vector v) {
		x -= v.getX();
		y -= v.getY();
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
	
	public void set (Point p) {
		x = p.x;
		y = p.y;
	}
	
	public void set (Vector v) {
		x = v.getX();
		y = v.getY();
	}
	
	public void set (double nx, double ny) {
		x = nx;
		y = ny;
	}
	
	public double distanceTo (Point p) {
		double dx = (p.x-x)*(p.x-x);
		double dy = (p.y-y)*(p.y-y);
		return Math.sqrt(dx + dy);
	}
	
	public double distanceTo (Line line) {
		double a = line.getA();
		Point A;
		Point B;
		if (Math.abs(a) <= 1) {
			A = new Point(0, line.yAt(0));
			B = new Point(10, line.yAt(10));
		} else {
			A = new Point(line.xAt(0), 0);
			B = new Point(line.xAt(10), 10);
		}
		double d = new Vector(A, B).getCrossProduct(new Vector(B, this));
		d /= A.distanceTo(B);
		
		return Math.abs(d);
	}
	
	public double distanceTo (Segment seg) {
		Point A = seg.getP1();
		Point B = seg.getP2();
		Vector AB = new Vector(A, B);
		Vector BA = new Vector(B, A);
		Vector BC = new Vector(B, this);
		Vector AC = new Vector(A, this);
		
		double d = AB.getCrossProduct(BC);
		d /= A.distanceTo(B);
		
		double dot1 = AB.getDotProduct(BC);
        if(dot1 > 0) return distanceTo(B);
        double dot2 = BA.getDotProduct(AC);
        if(dot2 > 0) return distanceTo(A);
        
		return Math.abs(d);
	}
	
	public double squareDistanceTo (Point p) {
		double dx = (p.x-x)*(p.x-x);
		double dy = (p.y-y)*(p.y-y);
		return dx + dy;
	}
	
	public boolean equals (Point p) {
		return (x == p.x && y == p.y);
	}
	
	public String toString () {
		return ("(" + x + ", " + y + ")");
	}

	
	
}
