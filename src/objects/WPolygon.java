package objects;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

import utils.Angle;
import utils.ConvexHull;
import utils.Point;
import utils.Segment;
import utils.Vector;
import GUI.WDrawer;

/**
 * This class defines the Polygon shape used by World.
 * @author Romain
 *
 */
public class WPolygon extends WObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6957960145450313190L;
	protected ArrayList<Point> points;
	
	/**
	 * Create a new instance of convex polygon
	 * @param p ArrayList of points that are in the polygon.
	 * A convex polygon will be created from this list.
	 * 	
	 */
	public WPolygon (ArrayList<Point> p) {
		m_flags[index] |= TYPE_POLY;
		setPoints(p);
	}
	
	public WPolygon () {
		m_flags[index] |= TYPE_POLY;
	}
	
	public void setPoints(ArrayList<Point> p) {
		points = p;
		new ConvexHull();
		// Get the convex hull points
		points = ConvexHull.quickHull(p);
		
		double mx = 0;
		double my = 0;
		double m = 0;
		
		for (int i = 0; i < points.size(); i++) {
			Segment s = new Segment(points.get(i), 
					points.get((i+1 == points.size())?0:i+1));
			double w = s.getLength();
			m += w;
			mx += w * s.getPointRelativeTo(0.5).getX();
			my += w * s.getPointRelativeTo(0.5).getY();
			
			
		}
		
		Point cm = new Point(mx/m, my/m);
		setPosition(cm);
		
		m_inertia[index] = 1;
		for (int i = 0; i < points.size(); i++) {
			Point c = points.get(i);
			c.add(-cm.getX(), -cm.getY());
			
			double d = c.distanceTo(Point.ORIGIN);
			m_radius[index] = (d > m_radius[index]) ? d : m_radius[index] ;
		}
		
		init();
	}
	public boolean contains (Point p) {
		
		boolean c = false;
		for (int i = 0, j = getVertexCount()-1; i < getVertexCount(); j = i++) {
			if ( ((getPoint(i).getY()>p.getY()) != (getPoint(j).getY()>p.getY())) &&
			(p.getX() < (getPoint(j).getX()-getPoint(i).getX()) * (p.getY()-getPoint(i).getY()) / (getPoint(j).getY()-getPoint(i).getY()) + getPoint(i).getX()) )
				c = !c;
		}
		return c;
	}
	
	public Point getExtremePoint (Vector v) {
		int index = 0;
		
		Vector point = new Vector(points.get(0));
		
		double val = point.getDotProduct(v);
		
		for (int i = 1; i < points.size(); i++)	{
			double x = new Vector(points.get(i)).getDotProduct(v);
			
			if (x > val) {
				val = x;
				index = i;
			}
		}
		
		return getPoint(index);
	}
	
	public Vector getNormal (int i) {
		int j = (i+1) % points.size();
		return new Vector(getPoint(i), getPoint(j)).getRightNormal();
	}
	
	public Segment getEdge (int i) {
		int j = (i+1) % points.size();
		return new Segment(getPoint(i), getPoint(j));
	}
	
	public int getVertexCount () {
		return points.size();
	}
	
	public Point getPoint (int i) {
		
		i = i % points.size();
		
		return localToWorld(points.get(i));
	}
	
	public Point getLocalPoint (int i) {
		
		i = i % points.size();
		
		return points.get(i);
	}
	
	public void paint(WDrawer w) {
		int[] xs = new int[points.size()];
		int[] ys = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			Point a = points.get(i);
			
			Angle na = new Angle(a);
			na.add(m_orientation[index]);
			
			xs[i] = (int) (m_position[dIndex] + a.distanceTo(Point.ORIGIN) * na.cos() );
			ys[i] = (int) w.getInverseY(m_position[dIndex+1] + a.distanceTo(Point.ORIGIN) * na.sin() );
			
		}
		Polygon poly = new Polygon(xs, ys, points.size());
		
		w.setColor(color);
		w.getG().fillPolygon(poly);
		w.setStroke(2);
		w.setColor(Color.BLACK);
		w.getG().drawPolygon(poly);
	}
	
	public double calculateArea () {
		double area = 0;
		
		if (points == null)
			return Double.NEGATIVE_INFINITY;
		for (int i = 0, j = 1; i < points.size(); i++, j++) {
			j = (j==points.size())?0:j;
			area += points.get(i).getX() * points.get(j).getY();
			area -= points.get(j).getX() * points.get(i).getY();
		}
		area /= 2;
		// Math.abs... because area can be negative or positive, 
		// depending on the cw/ccw ordering of the vertices.
		return Math.abs(area);
	}
	
	public double calculateMoI () {
		double MoI = 0;
		Point pCM = new Point(m_position[dIndex], m_position[dIndex+1]);
		for (int i = 0; i < points.size(); i++) {
			Point pI = getPoint(i);
			Point pJ = getPoint(i + 1);
			
			Segment ICM = new Segment(pI, pCM);
			Point pJonICM = ICM.closestPointTo(pJ);
			
			Point pCMP = new Point(
					(pCM.getX() + pI.getX() + pJ.getX()) / 3,
					(pCM.getY() + pI.getY() + pJ.getY()) / 3
			);
			
			double h = pJ.distanceTo(ICM);
			double a = pJonICM.distanceTo(pI);
			double b = pI.distanceTo(pCM);
			double d = pCM.distanceTo(pCMP);
			
			MoI += 
				(b*b*b*h - b*b*h*a + b*h*a*a + b*h*h*h) / 36
				+ b * h / 2 * m_density[index] * d*d
			;
		}
		return MoI / 100; // TODO
	}
	
	public WObject getCopy(){
		@SuppressWarnings("unchecked")
		WObject copy = new WPolygon((ArrayList<Point>)(this.points.clone()));
		copy.setColor(color);
		copy.setMass(m_mass[index]);
		copy.setOrientation(new Angle(m_orientation[index]));
		copy.setNoMove(isUnmovable());
		copy.setNoSpin(isUnspinnable());
		copy.setDensity(m_density[index]);
		copy.setFriction(m_friction[index]);
		copy.setRestitution(m_restitution[index]);
		copy.setSpeed(new Vector(m_speed[index],m_speed[index+1]));
		copy.setRotation(new Angle(m_rotation[index]));
		return copy;
	}
	
	public ArrayList<Point> getPoints() {
		return this.points;
	}

}
