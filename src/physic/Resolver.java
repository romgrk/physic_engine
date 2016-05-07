package physic;

import java.util.ArrayList;

import objects.Constraint;
import objects.WObject;
import objects.World;
import utils.*;

/**
 * Calculate the response to the contacts between objects.
 * @author Romain
 *
 */
public class Resolver {

	private ArrayList<Contact> contacts;
	
	public Resolver () {}
	
	/**
	 * Calculate new velocities after collision
	 * @param worldContacts The contacts between objects
	 */
	public void resolveVelocity (ArrayList<Contact> worldContacts) {
		contacts = worldContacts;
		
		//double[] oldForce = WObject.m_oldForce;
		
		double[] mass = WObject.m_mass;
		double[] invMass = WObject.m_invMass;
		//double[] inertia = WObject.m_inertia;
		//double[] invInertia = WObject.m_invInertia;
		
		double[] position = WObject.m_position;
		double[] speed = WObject.m_speed;
		
		double[] rotation = WObject.m_rotation;
		
		double[] restitution = WObject.m_restitution;
		double[] friction = WObject.m_friction;
		
		for (int i = 0; i < contacts.size(); i++) {
			Contact data = contacts.get(i);
			
			if (!data.valid)
				continue;
			
			WObject objA = data.objA;
			WObject objB = data.objB;
			
			int 
			indexA = objA.index, indexB = objB.index,
			dIndexA = indexA << 1, dIndexB = indexB << 1;
			
			Vector normal = data.normal.getNormalizedVector();
			double[] tabNormal = {normal.getX(), normal.getY()};
			Vector tangent = normal.getRightNormal();
			
			Point p = data.position;
			
			double[] linearSpA = {speed[dIndexA], speed[dIndexA+1]};
			double[] linearSpB = {speed[dIndexB], speed[dIndexB+1]};
			double[] spA = {
				linearSpA[0] - ((p.getY() - position[dIndexA+1]) * rotation[indexA])
				,
				linearSpA[1] + ((p.getX() - position[dIndexA]) * rotation[indexA])
			};
			double[] spB = {
				linearSpB[0] - ((p.getY() - position[dIndexB+1]) * rotation[indexB])
				,
				linearSpB[1] + ((p.getX() - position[dIndexB]) * rotation[indexB])
			};
			double[] speedT = {
					linearSpA[0] - linearSpB[0],
					linearSpA[1] - linearSpB[1]
			};
			
			double 
			ma = mass[indexA], mb = mass[indexB],
			invMa = invMass[indexA], invMb = invMass[indexB];
			
			/*  --- LAWS ---
			 *  <n> : unit vector representing normal impulse
			 *  f : scalar magnitude of the impulse
			 *  
			 *  Conservation of momentum
			 *  ma <vaf> = ma<vai> + f<n>
			 *  <vaf> = <vai> + f<n> / ma
			 *  
			 *  Speed at point p, where r is distance from center to p
			 *  <vap> = <vai> + wa x r
			 */
			
			double k = V2D.getDotProduct(speedT, tabNormal);
			
			if (k < 0)  {
				continue;
			}
			
			// v1' = 2(m2)(v2) + (m1 - m2)(v1)
			//		--------------------------
			//				m1 + m2
			
			ma = (invMa == 0)? 1 : ((invMb == 0)? 0 : ma);
			mb = (invMb == 0)? 1 : ((invMa == 0)? 0 : mb);
			double
			invMaMb = 1 / (ma + mb),
			vai = V2D.toVector(spA).getProjection(normal).getLength(),
			vbi = -V2D.toVector(spB).getProjection(normal).getLength(),
			vaf = ((2 * mb * vbi + (ma - mb)*vai)) * invMaMb,
			vbf = ((2 * ma * vai + (mb - ma)*vbi)) * invMaMb;
			
			
			double[] impulse = {normal.getX(), normal.getY()};
			V2D.multiply(impulse, restitution[indexA]*0.5 + restitution[indexB]*0.5);

			double[] impulseA = {impulse[0], impulse[1]};
			double[] impulseB = {impulse[0], impulse[1]};
			
			V2D.multiply(impulseA, (vaf-vai)*mass[indexA]);
			V2D.multiply(impulseB, (vbf-vbi)*mass[indexB]);
			
			if (invMa == 0) {
				impulseA = new double[]{-impulseB[0], -impulseB[1]};
			}
			if (invMb == 0) {
				impulseB = new double[]{-impulseA[0], -impulseA[1]};
			}
			
			/*double[] forceT = {
					oldForce[dIndexA] - oldForce[dIndexB], 
					oldForce[dIndexA+1] - oldForce[dIndexB+1]
			};*/
			double[] tan = {tangent.getX(), tangent.getY()};
			double[] forceA = {
					spA[0] - impulseA[0],// - , 
					spA[1] - impulseA[1]// - 
					         };
			double[] forceB = {
					spB[0] - impulseB[0],// - , 
					spB[1] - impulseB[1]// - 
					         };
			//V2D.multiply(forceA, m);
			double[] frictionA = V2D.getNormalizedVector(V2D.getProjection(spA, tan));
			double[] frictionB = V2D.getNormalizedVector(V2D.getProjection(spB, tan));
			
			V2D.multiply(frictionA, -friction[indexA]*V2D.getLength(forceA)*World.dt);
			V2D.multiply(frictionB, -friction[indexB]*V2D.getLength(forceB)*World.dt);
			
			
			objA.applyImpulse(V2D.toVector(impulseA), p);
			objA.applyImpulse(V2D.toVector(frictionA), p);
			
			objB.applyImpulse(V2D.toVector(impulseB), p);
			objB.applyImpulse(V2D.toVector(frictionB), p);
			
		}
	}
	
	/**
	 * Resolve position to make sure two objects in contact will not be
	 * colliding on the next frame.
	 * @param worldContacts The list of contacts
	 */
	public void resolvePosition (ArrayList<Contact> worldContacts) {
		contacts = worldContacts;
		
		double[] position = WObject.m_position;
		double[] mass = WObject.m_mass;
		double[] invMass = WObject.m_invMass;
		
		for (int i = 0; i < contacts.size(); i++) {
			
			Contact data = contacts.get(i);
			WObject objA = data.objA;
			WObject objB = data.objB;
			
			int indexA = objA.index, indexB = objB.index;
			Vector normal = data.normal.getNormalizedVector();
			
			double 
			ma = mass[indexA], mb = mass[indexB],
			invMa = invMass[indexA], invMb = invMass[indexB];
			ma = (invMa == 0)? 1 : ((invMb == 0)? 0 : ma);
			mb = (invMb == 0)? 1 : ((invMa == 0)? 0 : mb);
			double
			invMaMb = 1 / (ma + mb);
			double diff = data.penetration*0.8; // TODO should we adjust ? should we remove ?
			
			position[indexA<<1] -= normal.getX() * diff * (mb * invMaMb);
			position[(indexA<<1)+1] -= normal.getY() * diff * (mb * invMaMb);
			
			position[indexB<<1] += normal.getX() * diff * (ma * invMaMb);
			position[(indexB<<1)+1] += normal.getY() * diff * (ma * invMaMb);
			//double ma = mass[indexA], mb 
			
		}
	}
	
	/**
	 * Calculate the impulse to resolve constraints.
	 * @param constraints List of constraints
	 * @see Constraint
	 */
	public void solveConstraints (ArrayList<Constraint> constraints) {
		double[] mass = WObject.m_mass;
		double[] invMass = WObject.m_invMass;
		
		for (int i = 0; i < constraints.size(); i++) {
			Constraint k = constraints.get(i);
			
			WObject objA = k.objA;
			WObject objB = k.objB;
			
			Point pA = k.objA.localToWorld(k.pointA);
			Point pB = k.objB.localToWorld(k.pointB);
			
			int 
			indexA = objA.index, indexB = objB.index;
			//dIndexA = indexA << 1, dIndexB = indexB << 1;
			
			double[] AB = {pB.getX() - pA.getX(), pB.getY() - pA.getY()};
			double diff = V2D.getLength(AB) - k.distance;
			//diff *= 0.01;
			
			if (diff == 0)
				continue;
			
			double 
			ma = mass[indexA], mb = mass[indexB],
			invMa = invMass[indexA], invMb = invMass[indexB];
			
			ma = (invMa == 0)? 1 : ((invMb == 0)? 0 : ma);
			mb = (invMb == 0)? 1 : ((invMa == 0)? 0 : mb);
			
			double
			fa = mb / (ma + mb),
			fb = ma / (ma + mb);
			
			V2D.normalize(AB);
			V2D.multiply(AB, diff);
			
			double[] impulseA = {AB[0], AB[1]};
			V2D.multiply(impulseA, fa*mass[indexA]*World.dt);
			double[] impulseB = {AB[0], AB[1]};
			V2D.multiply(impulseB, -fb*mass[indexB]*World.dt);
			
			/*F.s(diff + "; ");
			F.s(V2D.toVector(impulseA) + " " + V2D.toVector(impulseB));
			F.s(""+V2D.toVector(AB));
			F.s(fa + " " + fb);*/
			//F.s(invMaMb + " " + ma + " " + mb + " ");
			
			objA.applyImpulse(V2D.toVector(impulseA), pA);
			objB.applyImpulse(V2D.toVector(impulseB), pB);
		}
	}
}
