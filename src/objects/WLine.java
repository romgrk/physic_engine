package objects;

import java.util.ArrayList;

import GUI.WDrawer;

import utils.Angle;
import utils.Point;
import utils.Vector;

/**
 * WLine is an extension of WPolygon
 * @author Romain
 *
 */
public class WLine extends WPolygon {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6958334115173835702L;
	private double length;
	
	/**
	 * Constructor, creates a line from 2 points
	 * @param a First point
	 * @param b Second point
	 */
	public WLine(Point a, Point b) {
		Vector AB = new Vector(a, b);
		length = AB.getLength();
		Angle omega = new Angle(AB);
		ArrayList<Point> pts = new ArrayList<Point>();
		pts.add(new Point(0, 0));
		pts.add(new Point(0, 2));
		pts.add(new Point(length, 2));
		pts.add(new Point(length, 0));
		
		setPoints(pts);
		m_orientation[index] = omega.value();
		setPosition(a.add(AB.multiply(0.5)));
		m_radius[index] = length / 2;
	}
	
	/**
	 * Creates a WLine from a length
	 * @param len The length of the line
	 */
	public WLine(double len) {
		this(new Point(0, 0), new Point(len, 0));
	}
	
	public void paint (WDrawer w) {
		Angle oA = new Angle(m_orientation[index]);
		Angle oB = new Angle(m_orientation[index] + Math.PI);
		Point a = getPosition().add(
				new Vector(oA).multiply(m_radius[index])
		);
		Point b = getPosition().add(
				new Vector(oB).multiply(m_radius[index])
		);
		
		w.setStroke(2);
		w.setColor(color);
		w.drawLine(a, b);
	}
}
