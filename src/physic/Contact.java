package physic;

import objects.WObject;
import utils.Point;
import utils.Segment;
import utils.Vector;

/**
 * Contact retains all relevant information to treat a collision
 * @author Romain
 *
 */
public class Contact {
	
	/*
	 * Contact applies from edge to vertex. 
	 */
	// The two objects colliding (or not)
	public WObject objA;
	public WObject objB;
	
	// Position of the contact point in World coordinates
	public Point position;
	
	// Normal of the contact point between the two bodies
	public Vector normal;
	
	public boolean vToV = false;
	
	public Point vertex = new Point();
	public Point vertex2 =  new Point();
	public Segment edge;
	
	public double penetration;
	
	public boolean valid = false;
}
