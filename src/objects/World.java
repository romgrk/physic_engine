package objects;

import java.awt.Color;
import java.util.ArrayList;

import constants.Settings;

import physic.BroadPhase;
import physic.Contact;
import physic.ForceGenerator;
import physic.NarrowPhase;
import physic.Resolver;
import physic.WObjectPair;
import utils.F;
import utils.Point;
import utils.Vector;
import GUI.Scene;
import GUI.WDrawer;

/**
 * This class is the virtual representation of the world. 
 * It contains every visible object.
 * @author Romain
 *
 */
public class World {
	
	private Scene scene;
	private double width;
	private double height;
	
	private ArrayList<WObject> objects 			= new ArrayList<WObject>();
	private ArrayList<Contact> contacts 		= new ArrayList<Contact>();
	private ArrayList<Constraint> constraints 	= new ArrayList<Constraint>();
	private ArrayList<ForceGenerator> forces 	= new ArrayList<ForceGenerator>();
	private ArrayList<WObjectPair> pairs;
	
	private NarrowPhase narrow = new NarrowPhase();
	private Resolver resolver = new Resolver();
	
	private boolean running = false;
	
	private WTimer thread;
	public final static double ms = Settings.DEFAULT_TIMESTEP;
	public final static double dt = ms / 1000;
	public final static double dt2 = dt * dt;
	private double sceneMs = 0;
	
	private Vector gravity = Settings.DEFAULT_GRAVITY; // m/s²
	
	/**
	 * Constructor, takes in parameter the width and the height of the world.
	 * @param w width
	 * @param h height
	 */
	public World (double w, double h) {
		WObject.world = this;
		width = w;
		height = h;
		thread = new WTimer(this);
	}
	
	/**
	 * Width and height will be set when adding to a Scene.
	 */
	public World () {
		this(-1.0, -1.0);
	}
	
	/**
	 * Must be called at every x milliseconds, where x is the timestep.
	 */
	public void update () {
		step();
		sceneMs += ms;
		if (sceneMs % 30 == 0)
			scene.update();
	}
	
	/*
	 * Simulation cycle :
	 * Happens at a determined framerate dt, wich
	 * is used to calculate everything.
	 * - Calculate forces applying on each body
	 * - Update speed and position resulting from previous phase
	 * - Broadphase test : check for possible colliding objects
	 * - Narrow phase : detect colliding objects and generate Contacts
	 * - Collision resolution : Use Contacts to resolve collisions
	 */
	/**
	 * This method is called at every ms milliseconds, and goes
	 * trough the cycle of the simulation.
	 */
	private void step () {
		
		// Gravity, speed&pos update, etc.
		updateObjects();
		
		// Clear past forces
		clearForces();
		
		// Quick test, to discard unlikely probabilities
		pairs = BroadPhase.findPairs(objects);
		
		// Real test, generate a list of contacts
		contacts = narrow.findContacts(pairs);
		
		// Velocity and Position resolver
		resolver.resolveVelocity(contacts);
		resolver.resolvePosition(contacts);
		
		resolver.solveConstraints(constraints);
	}
	
	/**
	 * This method move objects to thei new positions and set their speed to
	 * the right value.
	 */
	private void updateObjects () {
		
		double[] force = WObject.m_force;
		double[] oldForce = WObject.m_oldForce;
		double[] speed = WObject.m_speed;
		double[] position = WObject.m_position;
		
		double[] torque = WObject.m_torque;
		double[] rotation = WObject.m_rotation;
		double[] orientation = WObject.m_orientation;
		
		double[] invMass = WObject.m_invMass;
		double[] invInertia = WObject.m_invInertia;
		
		double[] impulse = WObject.m_impulse;
		double[] aImpulse = WObject.m_aImpulse;
		
		double[] mass = WObject.m_mass;
		
		for (ForceGenerator f : forces) {
			f.apply(dt);
		}
		
		double[] vi = new double[2];
		double[] a = new double[2];
		for (int i = 0; i < objects.size(); i++) {
			
			int index = objects.get(i).index;
			int dIndex = index<<1;
			boolean move = invMass[index] != 0;
			boolean spin = invInertia[index] != 0;
			
			if (move) {
				force[dIndex] += gravity.getX() * mass[index];
				force[dIndex+1] += gravity.getY() * mass[index];
				
				//speed[dIndex] += impulse[dIndex] * invMass[index];
				//speed[dIndex+1] += impulse[dIndex+1] * invMass[index];
				force[dIndex] += impulse[dIndex] / dt;
				force[dIndex+1] += impulse[dIndex+1] / dt;
				
				oldForce[dIndex] = force[dIndex];
				oldForce[dIndex+1] = force[dIndex+1];
				
				//F.s(force[dIndex+1]);
				
				impulse[dIndex] = 0;
				impulse[dIndex+1] = 0;
				
				vi[0] = speed[dIndex];
				vi[1] = speed[dIndex+1];
				a[0] = force[dIndex] * invMass[index];
				a[1] = force[dIndex+1] * invMass[index];
				
				speed[dIndex] += a[0] * dt;
				speed[dIndex+1] += a[1] * dt;
				
				position[dIndex] += vi[0] * dt + 0.5 * a[0] * dt2;
				position[dIndex+1] += vi[1] * dt + 0.5 * a[1] * dt2;
			}
			
			if (spin) {
				
				torque[index] += aImpulse[index] / dt;
				
				double ri = rotation[index];
				double w = torque[index] * invInertia[index];
				
				//rotation[index] += aImpulse[index] * invInertia[index];
				//F.s("R +=" + aImpulse[index] * invInertia[index]);
				//F.s("R =" + rotation[index]);
				aImpulse[index] = 0;
				
				rotation[index] += w * dt;
				double limit = 2*Math.PI;
				if (rotation[index] > limit || rotation[index] < -limit)
					rotation[index] = limit * ((rotation[index]< 0)?-1:1);
				//rotation[index] *= 0.999;
				
				orientation[index] += ri * dt + 0.5 * w * dt2;
			}
		}
	}
	
	/** 
	 * This clear forces at each end of cycle of the simulation.
	 */
	private void clearForces() {
		for (int i = 0; i < objects.size(); i++) {
			//F.s(objects.get(i).index);
			int dIndex = objects.get(i).index << 1;
			
			WObject.m_force[dIndex] = 0;
			WObject.m_force[dIndex+1] = 0;
			
			WObject.m_torque[dIndex >> 1] = 0;
		}
	}
	
	/**
	 * Start the simulation if there is a scene to display.
	 */
	public void start () {
		if (!running && scene != null) {
			running = true;
			thread = new WTimer(this);
			thread.start();
		}
	}
	
	/**
	 * Stops the simulation if its not already done.
	 */
	public void stop () {
		if (running) {
			running = false;
			thread.interrupt();
		}
	}
	class WTimer extends Thread {
		private World w;
		public WTimer (World w) {
			this.w = w;
		}
		public void run () {
			while (true) {
				w.update();
				try {
					sleep((long) ms);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
	/**
	 * Call all the objects of the world to draw themselves and then
	 * draw all constraints of the world.
	 * @param w
	 */
	public void paint (WDrawer w) {		
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).paint(w);
		}
		
		w.setColor(Color.BLACK);
		w.setStroke(1);
		for (int i = 0; i < constraints.size(); i++) {
			Constraint k = constraints.get(i);
			Point 
			pA = k.objA.localToWorld(k.pointA),
			pB = k.objB.localToWorld(k.pointB);
			w.fillCircle(pA, 3);
			w.fillCircle(pB, 3);
			w.drawLine(pA, pB);
		}
	}
	
	public void addObject (WObject wo) {
		objects.add(wo);
		scene.repaint();
	}
	public void removeObject (WObject wo) {
		// Remove the object from the world
		objects.remove(wo);
		// And then check for any link or force that applies to this object,
		// and remove it.
		for (Constraint k : constraints) {
			if (k.objA == wo || k.objB == wo) {
				constraints.remove(k);
			}
		}
		//for (ForceGenerator f : forces) {
			// TODO verify if f is applying to this object
			//if (true) {
			//	forces.remove(f);
			//}
		//}
	}
	public void addConstraint (Constraint k) {
		constraints.add(k);
	}
	public void removeConstraint (Constraint k) {
		constraints.remove(k);
	}
	public void addForce (ForceGenerator f) {
		forces.add(f);
	}
	public void removeForce (ForceGenerator f) {
		forces.remove(f);
	}
	public void clear () {
		objects = new ArrayList<WObject>();
		constraints = new ArrayList<Constraint>();
		forces = new ArrayList<ForceGenerator>();
		WObject.nextID = 0;
	}
	
	//General get/set functions
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public void setDimension (double w, double h) {
		width = w;
		height = h;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
		
		if (getWidth() == -1.0)
			setDimension(scene.getWidth(), scene.getHeight());
	}
	public Scene getScene() {
		return scene;
	}
	public ArrayList<WObject> getObjects() {
		return objects;
	}
	public void setObjects(ArrayList<WObject> objects) {
		this.objects = objects;
	}
	public double getMs() {
		return ms;
	}
	public double getDt() {
		return dt;
	}
	public void setGravity(double g) {
		gravity.setY(g);
	}
	
	public double getGravity () {
		return gravity.getY();
	}


	public ArrayList<Contact> getContacts() {
		return contacts;
	}
	public ArrayList<Constraint> getLinks() {
		return constraints;
	}
	public ArrayList<ForceGenerator> getForces() {
		return forces;
	}

	public boolean isRunning() {
		return running;
	}
}

