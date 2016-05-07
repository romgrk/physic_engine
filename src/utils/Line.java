
package utils;

/**
 * Line provides methods to deal with infinite lines
 * @author Romain
 *
 */
public class Line {
	
	protected double a;
	protected double b;
	
	Line (double na, double nb) {
		a = na;
		b = nb;
	}
	
	public Line (Point p1, Point p2) {
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		if (dx == 0 && dy == 0) {
			a = Double.NEGATIVE_INFINITY;
			b = 0;
		} else if (dx == 0.0) {
			a = Double.POSITIVE_INFINITY;
			b = p1.getX();
		} else {
			a = dy / dx;
			b = p1.getY() - p1.getX() * a;
		}
		
	}
	
	public Line (Vector v) {
		this(Point.ORIGIN, v);
	}
	
	public Line (Point p, Vector v) {
		this(p, new Point(p).add(v));
	}

	public Line (Segment segment) {
		this(segment.getP1(), segment.getP2());
	}

	public double getA() {
		return a;
	}
	
	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}
	
	public void addB (double n) {
		b += n;
	}
	
	public double xAt (double y) {
		if (a == Double.POSITIVE_INFINITY)
			return b;
		return (y - b) / a;
	}
	
	public double yAt (double x) {
		if (a == Double.POSITIVE_INFINITY) 
			return Double.NEGATIVE_INFINITY;
		return a * x + b;
	}
	
	public Point intersection (Line l)
	{
		if (l.a == a) 
			return null;
		
		double x;
		double y;
		if (a == Double.POSITIVE_INFINITY) {
			x = b;
			y = l.yAt(x);
		} else if (l.a == Double.POSITIVE_INFINITY) {
			x = l.b;
			y = yAt(x);
		} else {
			x = (l.b - b) / (a - l.a);
			y = yAt(x);
		}
		return new Point(x, y); 
	}
	
	public Point intersection (Segment s) {
		return s.intersection(this);
	}
	
	public Point closestPointTo (Point p) {
		Point p1;
		Point p2;
		if (Math.abs(a) <= 1) {
			p1 = new Point(0, yAt(0));
			p2 = new Point(10, yAt(10));
		} else {
			p1 = new Point(xAt(0), 0);
			p2 = new Point(xAt(10), 10);
		}
		
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();

		if ((dx == 0) && (dy == 0)) {
		    return p1;
		}

		double u = 
			((p.getX() - p1.getX()) * dx + (p.getY() - p1.getY()) * dy) 
			/ 
			(dx*dx + dy*dy);

		return new Point(p1.getX() + u * dx, p1.getY() + u * dy);
	}
	
	public boolean equals (Line l) {
		return (a == l.a && b == l.b);
	}
}

/*
PVector segIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) 
{ 
  float bx = x2 - x1; 
  float by = y2 - y1; 
  float dx = x4 - x3; 
  float dy = y4 - y3;
  float b_dot_d_perp = bx * dy - by * dx;
  if(b_dot_d_perp == 0) {
    return null;
  }
  float cx = x3 - x1;
  float cy = y3 - y1;
  float t = (cx * dy - cy * dx) / b_dot_d_perp;
  if(t < 0 || t > 1) {
    return null;
  }
  float u = (cx * by - cy * bx) / b_dot_d_perp;
  if(u < 0 || u > 1) { 
    return null;
  }
  return new PVector(x1+t*bx, y1+t*by);
}
*/
