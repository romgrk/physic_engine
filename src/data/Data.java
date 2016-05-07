package data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import objects.WObject;
import objects.World;

/**
 * This class has only static methods, that are used to handle information
 * in order to load and save the World.
 * @author Romain
 *
 */
public class Data {
	
	/**
	 * Saves the WorldData object as the filename given.
	 * @param data Data to be saved
	 * @param fileName Name of the file that will be created
	 */
	public static void save (WorldData data, String fileName){
		try{
			FileOutputStream out = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(data);
			oos.flush();
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,
				    "Error while saving file '" + fileName + "'.",
				    "Save",
				    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a WorldData object from the specified file.
	 * @param fileName The name of the file where is saved the object
	 * @return The object loaded
	 */
	public static WorldData load (String fileName) {
		WorldData data = null;
		try{
			FileInputStream in = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(in);
			data = (WorldData) (ois.readObject());
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,
				    "Error while loading file '" + fileName + "'.",
				    "Load",
				    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * For internal use. Set World and WObject to what is in the WorldData object.
	 * @param data World that needs to be loaded
	 * @param world The actual World object
	 */
	public static void match (WorldData data, World world) {
		
		world.setObjects(data.objects);
		world.setGravity(data.gravity);
				
		WObject.m_flags = data.m_flags;
				
		WObject.m_position = data.m_position;
		WObject.m_orientation = data.m_orientation;
				
		WObject.m_speed = data.m_speed;
		WObject.m_rotation = data.m_rotation;
				
		WObject.m_force = data.m_force;
		WObject.m_torque = data.m_torque;
				
		WObject.m_oldForce = data.m_oldForce;
				
		WObject.m_impulse = data.m_impulse;
		WObject.m_aImpulse = data.m_aImpulse;
				
		WObject.m_mass = data.m_mass;
		WObject.m_inertia = data.m_inertia;
		WObject.m_invMass = data.m_invMass;
		WObject.m_invInertia = data.m_invInertia;
				
		WObject.m_density = data.m_density;
		WObject.m_friction = data.m_friction;
		WObject.m_restitution = data.m_restitution;
				
		WObject.m_radius = data.m_radius;
				
		WObject.nextID = data.nextID;
	}
}
