package utils;

/**
 * Segment is an extension of Line to deal with segment of lines.
 * @author Romain
 *
 */
public class Segment extends Line {
	
	protected Point p1;
	protected Point p2;
	
	public Segment(Point p1, Point p2) {
		super(p1, p2);
		
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Segment() {
		this(Point.ORIGIN, Point.ORIGIN);
	}

	public Segment(Point p, Vector v) {
		this(p, p.copy().add(v));
	}

	public Point intersection (Segment s) {
		Point p = intersection(new Line(s.p1, s.p2));
		
		if (p == null)
			return null;
		
		double 	
		x1 = Math.min(p1.getX(), p2.getX()),
		x2 = Math.max(p1.getX(), p2.getX()),
		x3 = Math.min(s.p1.getX(), s.p2.getX()),
		x4 = Math.max(s.p1.getX(), s.p2.getX());
		
		if (p.getX() >= x1 
			&& p.getX() <= x2 
			&& p.getX() >= x3 
			&& p.getX() <= x4) {
			return p;
		} else {
			return null;
		}
		
	}
	
	public Point intersection (Line l) {
		Point p = super.intersection(l);
		
		if (p == null)
			return null;
		
		double 	
		x1 = Math.min(p1.getX(), p2.getX()),
		x2 = Math.max(p1.getX(), p2.getX());
		
		if (p.getX() >= x1 && p.getX() <= x2) {
			return p;
		} else {
			return null;
		}
	}

	public Point getP1 () {
		return p1;
	}
	
	public Point getP2 () {
		return p2;
	}
	
	public double getLength () {
		return p1.distanceTo(p2);
	}
	
	public Point getPointRelativeTo (double d) {
		double dx = p1.getX() + d*(p2.getX() - p1.getX());
		double dy = p1.getY() + d*(p2.getY() - p1.getY());
		return new Point(dx, dy);
	}
	
	public Point closestPointTo (Point p) {
		
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();

		if ((dx == 0) && (dy == 0)) {
		    return p1;
		}

		double u = ((p.getX() - p1.getX()) * dx + (p.getY() - p1.getY()) * dy) / (dx*dx + dy*dy);

		Point closestPoint;
		if (u < 0) {
		    closestPoint = p1;
		} else if (u > 1) {
		    closestPoint = p2;
		} else {
		    closestPoint = new Point(p1.getX() + u * dx, p1.getY() + u * dy);
		}

		return closestPoint;
	}
}
