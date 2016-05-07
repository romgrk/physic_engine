package objects;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import physic.Contact;

import constants.Settings;
import constants.WType;

import GUI.Scene;
import GUI.WDrawer;
import utils.*;

/**
 * Every object in the World extends WObject.
 * @author Romain
 *
 */
public abstract class WObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3406347756984473052L;
	
	// The world and scene where objects are added
	public static World world;
	public static Scene scene;
	
	// Flags
	public static final byte TYPE_CIRCLE	= 0;
	public static final byte TYPE_POLY		= 1;
	
	public static final byte AWAKE			= 1 << 1;
	
	public static final byte NOMOVE			= 1 << 2;
	public static final byte NOSPIN			= 1 << 3;
	
	public static final byte SELECTED		= 1 << 4;
	
	// All data
	public static byte[] m_flags 			= new byte[Settings.MAX_OBJECTS];
	
	public static double[] m_position 		= new double[Settings.MAX_OBJECTS*2];
	public static double[] m_orientation 	= new double[Settings.MAX_OBJECTS];
	
	public static double[] m_speed			= new double[Settings.MAX_OBJECTS*2];
	public static double[] m_rotation 		= new double[Settings.MAX_OBJECTS];
	
	public static double[] m_force 			= new double[Settings.MAX_OBJECTS*2];
	public static double[] m_torque 		= new double[Settings.MAX_OBJECTS];
	
	public static double[] m_oldForce		= new double[Settings.MAX_OBJECTS*2];
	
	public static double[] m_impulse 		= new double[Settings.MAX_OBJECTS*2];
	public static double[] m_aImpulse 		= new double[Settings.MAX_OBJECTS];
	
	public static double[] m_mass 			= new double[Settings.MAX_OBJECTS];
	public static double[] m_inertia 		= new double[Settings.MAX_OBJECTS];
	public static double[] m_invMass 		= new double[Settings.MAX_OBJECTS];
	public static double[] m_invInertia 	= new double[Settings.MAX_OBJECTS];
	
	public static double[] m_density		= new double[Settings.MAX_OBJECTS];
	public static double[] m_friction 		= new double[Settings.MAX_OBJECTS];
	public static double[] m_restitution 	= new double[Settings.MAX_OBJECTS];
	
	public static double[] m_radius 		= new double[Settings.MAX_OBJECTS];
	
	public static int nextID = 0;
	// Unique ID
	public final int index = nextID++;
	public final int dIndex = 2 * index;
	
	// Contacts with other objects
	protected ArrayList<Contact> contacts = new ArrayList<Contact>();

	// Color when drawing
	protected Color color = Color.BLACK;
	
	/**
	 * Initiliaze all data to zero.
	 */
	public WObject () {
		m_position[dIndex] 		= 0;
		m_position[dIndex+1] 	= 0;

		m_force[dIndex]			= 0;
		m_force[dIndex+1] 		= 0;
		
		m_oldForce[dIndex]		= 0;
		m_oldForce[dIndex+1]	= 0;
		
		m_speed[dIndex] 		= 0;
		m_speed[dIndex+1] 		= 0;
		
		m_impulse[dIndex] 		= 0;
		m_impulse[dIndex+1] 		= 0;

		m_orientation[index] 	= 0;
		
		m_rotation[index] 		= 0;
		
		m_torque[index] 		= 0;
		
		m_aImpulse[index] 		= 0;
		
		m_flags[index] 			= 0;
		
		m_density[index]		= Settings.AREA_DENSITY;
		m_friction[index] 		= Settings.DEFAULT_FRICTION;
		m_restitution[index] 	= Settings.DEFAULT_RESTITUTION;
		
		m_radius[index] 		= 0;
	}
	
	/**
	 * This method defines how an object will be draw.
	 * @param w The drawer
	 */
	public abstract void paint (WDrawer w);
	
	/**
	 * Calculate the area of the object.
	 * @return The area
	 */
	public abstract double calculateArea();
	
	/**
	 * Calculate the inertia of the object.
	 * @return The inertia
	 */
	public abstract double calculateMoI();
	
	/**
	 * This method verifies if the point is in the object.
	 * @param p The point to check
	 * @return True if the point is contained
	 */
	public abstract boolean contains (Point p);
	
	/**
	 * This method return how many points compose this object.
	 * @return The number of points.
	 */
	public abstract int getVertexCount();
	
	/**
	 * Return the normal to the vertice following the point at index i
	 * @param i Index of the point befor the vertice
	 * @return Right normal of the vertice
	 */
	public abstract Vector getNormal(int i);
	
	/**
	 * The point at index i will be returned. Note that in the case that
	 * i is superior to the number of vertex, it will be %ed to fit in the
	 * range of points.
	 * @param i Index of the point 
	 * @return The point at index i
	 */
	public abstract Point getPoint(int i);
	
	/**
	 * Given a vector v starting at the center of the object, it will
	 * return the farmost point of the object in that direction.
	 * @param v Vector pointing in the direction where the point is.
	 * @return Point
	 */
	public abstract Point getExtremePoint(Vector v);
	
	/**
	 * Returns the vertice following the point at index i.
	 * Note that i will be %ed to fit in the correct range.
	 * @param i Index of the point
	 * @return Vertice following point i
	 */
	public abstract Segment getEdge(int i);
	
	/**
	 * Returns a copy of the WObject.
	 * @return WObject identical to the WObject copied
	 */
	public abstract WObject getCopy();
	
	/**
	 * The object is stopped. Mass and inertia remain still, only speed and 
	 * rotation are affected.
	 */
	public void fix () {
		m_speed[dIndex] = 0;
		m_speed[dIndex+1] = 0;

		m_rotation[index] = 0;
	}
	
	/**
	 * Applies an impulse to the object at point p over one timestep.
	 * @param impulse The impulse to apply
	 * @param p Where to apply the impulse, in World Coordinates
	 */
	public void applyImpulse (Vector impulse, Point p) {		
		//p = worldToLocal(p);
		
		double[] i = {impulse.getX(), impulse.getY()};
		m_impulse[dIndex] += i[0];
		m_impulse[dIndex+1] += i[1];
		//F.s("impulse : " + impulse);
		
		double[] offset = {
				p.getX() - m_position[dIndex],
				p.getY() - m_position[dIndex+1]
		};
		m_aImpulse[index] += 
			V2D.getCrossProduct(offset, i);
		//F.s("biasImpulse : " + V2D.getCrossProduct(offset, i)); 
	}
	
	/** 
	 * For internal use. Calculates mass and inertia of the object.
	 */
	protected void init () {
		m_density[index] = Settings.AREA_DENSITY;
		
		m_mass[index] = calculateArea() * m_density[index] / 1000;
		m_invMass[index] = (m_mass[index] == Double.POSITIVE_INFINITY) ? 0 : 1/m_mass[index];
		
		m_inertia[index] = calculateMoI() / 1;
		m_invInertia[index] = (m_inertia[index] == Double.POSITIVE_INFINITY) ? 0 : 1/m_inertia[index];
	}
	
	public Vector getForce() {
		return new Vector(m_force[dIndex], m_force[dIndex+1]);
	}

	public Angle getTorque() {
		return new Angle(m_torque[index]);
	}
	
	public double getRestitution() {
		return m_restitution[index];
	}

	public void setRestitution(double restitution) {
		m_restitution[index] = restitution;
	}

	public double getFriction() {
		return m_friction[index];
	}

	public void setFriction(double friction) {
		m_friction[index] = friction;
	}

	public double getRay() {
		return m_radius[index];
	}
	
	public double getMass() { return m_mass[index]; }
	
	public double getMoI() { return m_inertia[index]; }
	
	public double getInverseMass () {
		return m_invMass[index];
	}
	
	public double getInverseMoI () {
		return m_invInertia[index];
	}
	
	public void setMass(double weight) {
		
		m_mass[index] = weight;
		m_invMass[index] = (m_mass[index] == Double.POSITIVE_INFINITY) ? 0 : 1/m_mass[index];
		
		
		if (m_invMass[index] == 0) {
			fix();
			m_flags[index] |= 1 << NOMOVE;
		}
	}

	public void setMoI(double i) {
		m_inertia[index] = i;
		m_invInertia[index] = (m_inertia[index] == Double.POSITIVE_INFINITY) ? 0 : 1/m_inertia[index];
		
		if (m_invInertia[index] == 0) {
			m_rotation[index] = 0;
			m_flags[index] |= 1 << NOSPIN;
		}
	}
	
	public Angle getOrientation() {
		return new Angle(m_orientation[index]);
	}

	public void setOrientation(Angle angle) {
		m_orientation[index] = angle.value();
	}

	public Point getPosition () {
		return new Point(m_position[dIndex], m_position[dIndex + 1]);
	}

	public void setPosition (Point position) {
		m_position[dIndex] = position.getX();
		m_position[dIndex + 1] = position.getY();
	}
	
	public void setPosition (double x, double y) {
		m_position[dIndex] = x;
		m_position[dIndex + 1] = y;
	}

	public WType getType() {
		if ((m_flags[index] & 0x01) == TYPE_POLY)
			return WType.POLYGON;
		else
			return WType.CIRCLE;
	}

	public Vector getSpeed() {
		return new Vector(m_speed[dIndex], m_speed[dIndex+1]);
	}

	public void setSpeed(Vector speed) {
		m_speed[dIndex] = speed.getX(); 
		m_speed[dIndex+1] = speed.getY(); 
	}

	public boolean isFixed () {
		return m_invMass[index] == 0 && m_invInertia[index] == 0;
	}
	
	public void setFixed (boolean b) {
		if (b) {
			m_flags[index] |= 1 << NOMOVE | 1 << NOSPIN;
			m_mass[index] = Double.POSITIVE_INFINITY;
			m_invMass[index] = 0;
			m_inertia[index] = Double.POSITIVE_INFINITY;
			m_invInertia[index] = 0;
			fix();
		} else {
			m_flags[index] &= ~(1 << NOMOVE | 1 << NOSPIN);
			init();
		}
 	}
	
	public void setNoSpin (boolean b) {
		if (b) {
			m_flags[index] |= 1 << NOSPIN;
			m_inertia[index] = Double.POSITIVE_INFINITY;
			m_invInertia[index] = 0;
			m_rotation[index] = 0;
		} else {
			m_flags[index] &= ~(1 << NOSPIN);
			m_inertia[index] = calculateMoI() / 1;
			m_invInertia[index] = (m_inertia[index] == Double.POSITIVE_INFINITY) ? 0 : 1/m_inertia[index];
		}
	}
	
	public boolean isUnspinnable () {
		return m_invInertia[index] == 0;
	}
	
	public void setNoMove (boolean b) {
		if (b) {
			m_flags[index] |= 1 << NOMOVE;
			m_mass[index] = Double.POSITIVE_INFINITY;
			m_invMass[index] = 0;
			m_speed[dIndex] = 0;
			m_speed[dIndex+1] = 0;
		} else {
			m_flags[index] &= ~(1 << NOMOVE);
			m_mass[index] = calculateArea() * m_density[index] / 1000;
			m_invMass[index] = (m_mass[index] == Double.POSITIVE_INFINITY) ? 0 : 1/m_mass[index];
		}
	}
	
	public boolean isUnmovable () {
		return m_invMass[index] == 0;
	}
	
	public boolean isSelected () {
		return (m_flags[index] & SELECTED) != 0;
	}
	
	public void setSelected (boolean b) {
		if (b) {
			m_flags[index] |= SELECTED; 
		} else {
			m_flags[index] &= ~SELECTED; 
		}
	}
	
	public Angle getRotation() {
		return new Angle(m_rotation[index]);
	}

	public void setRotation(Angle wspeed) {
		m_rotation[index] = wspeed.value();
	}
	
	public void setRotation(double wspeed) {
		m_rotation[index] = wspeed;
	}
	
	public String toString () {
		return new Point(m_position[dIndex], m_position[dIndex+1]) + " r=" + m_radius[index];
	}

	public double getDensity() {
		return m_density[index];
	}

	public void setDensity(double density) {
		m_density[index] = density;
		init();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Point localToWorld (Point l) {
		Point p = new Point(l);
		Vector v = new Vector(p.distanceTo(Point.ORIGIN));
		Angle na = new Angle(p).add(m_orientation[index]);
		v.multiply(na.cos(), na.sin());
		p.set(v);
		p.add(m_position[dIndex], m_position[dIndex+1]);
		return p;
	}
	
	public Point worldToLocal (Point w) {
		Point p = new Point(w.getX() - m_position[dIndex], w.getY() -  m_position[dIndex+1]);
		Vector v = new Vector(p.distanceTo(Point.ORIGIN));
		Angle na = new Angle(p).sub(m_orientation[index]);
		v.multiply(na.cos(), na.sin());
		p.set(v);
		return p;
	}
	
}
