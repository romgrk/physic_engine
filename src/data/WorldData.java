package data;

import java.io.Serializable;
import java.util.ArrayList;

import objects.WObject;

/**
 * The class WorldData contains all necessary data to represent the World.
 * It is hence used to save the World by serializing it.
 * @author Romain
 *
 */
public class WorldData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public double gravity;
	public ArrayList<WObject> objects;
	
	public byte[] m_flags 			= WObject.m_flags;
	
	public double[] m_position 		= WObject.m_position;
	public double[] m_orientation 	= WObject.m_orientation;
	
	public double[] m_speed			= WObject.m_speed;
	public double[] m_rotation 		= WObject.m_rotation;
	
	public double[] m_force 			= WObject.m_force;
	public double[] m_torque 		= WObject.m_torque;
	
	public double[] m_oldForce		= WObject.m_oldForce;
	
	public double[] m_impulse 		= WObject.m_impulse;
	public double[] m_aImpulse 		= WObject.m_aImpulse;
	
	public double[] m_mass 			= WObject.m_mass;
	public double[] m_inertia 		= WObject.m_inertia;
	public double[] m_invMass 		= WObject.m_invMass;
	public double[] m_invInertia 	= WObject.m_invInertia;
	
	public double[] m_density		= WObject.m_density;
	public double[] m_friction 		= WObject.m_friction;
	public double[] m_restitution 	= WObject.m_restitution;
	
	public double[] m_radius 		= WObject.m_radius;
	
	public int nextID = WObject.nextID;
	
}
