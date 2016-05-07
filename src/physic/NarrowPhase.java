package physic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import constants.WType;

import objects.WCircle;
import objects.WObject;
import objects.WPolygon;
import utils.F;
import utils.Line;
import utils.Point;
import utils.Segment;
import utils.V2D;
import utils.Vector;

/**
 * NarrowPhase perform a more accurate test to determine if and how pairs
 * of objects are colliding.
 * @author Romain
 *
 */
public class NarrowPhase {
	
	private static ArrayList<Contact> contacts;
	
	/**
	 * Check types of objects, and then call the appropriate method to check
	 * if there is a collision.
	 * @param pairs The pairs of possibly colliding objects
	 * @return The Contacts representing a collision between two objects
	 */
	public static ArrayList<Contact> findContacts (ArrayList<WObjectPair> pairs) {
		
		// Return the list of all objects in contact
		contacts = new ArrayList<Contact>();
		
		// Iterate trough each pair of possibly touching objects
		for (int i = 0; i < pairs.size(); i++) {
			
			WObjectPair pair = pairs.get(i);
			
			WObject objA = pair.object1;
			WObject objB = pair.object2;
			
			int indexA = objA.index, indexB = objB.index;
			byte types = (byte) (
			(WObject.m_flags[indexB] & 1) 
			+ ((WObject.m_flags[indexA] & 1 ) << 1 )
			);
			/* AB
			 * 00 : 0x0 Circle Circle
			 * 01 : 0x1 Circle Poly
			 * 10 : 0x2 Poly Circle
			 * 11 : 0x3 Poly Poly
			 */
			switch (types) {
			case 0 : check((WCircle)objA, (WCircle)objB); break;
			case 1 : check((WCircle)objA, (WPolygon)objB); break;
			case 2 : check((WCircle)objB, (WPolygon)objA); break;
			case 3 : break;//check2((WPolygon)objA, (WPolygon)objB); break;
			default : F.s("Type error");
			}
		}
		
		return contacts;
	}
	
	/**
	 * Test a circle against a polygon.
	 * @param c Circle object
	 * @param w Polygon object
	 */
	public static void check (WCircle c, WPolygon w) {
		
		Contact contact = new Contact();
		contact.penetration = 100000;
		
		int indexC = c.index, indexW = w.index;
		
		Point center = c.getPosition();
		
		// First, test circle against each edge
		for (int i = 0; i < w.getVertexCount(); i++) {
			Segment edge = w.getEdge(i);
			
			double d = center.distanceTo(edge);
			if (d > c.getRay())
				continue;
			
			Vector normal = w.getNormal(i);
			Point pC = c.getExtremePoint(normal.getInverseVector());
			
			if (!w.contains(pC))
				continue;
			
			double overlap = pC.distanceTo(edge);
			
			if (overlap < contact.penetration) {
				contact.penetration = overlap;
				contact.position = pC; // TODO find correct position
				contact.normal = normal.getInverseVector();
				contact.vToV = false;
				contact.vertex = pC;
				contact.edge = edge;
				contact.valid = true;
			}
		}
		
		// Second, test circle against each point
		Point pC = new Point();
		Point pW = new Point();
		Vector CpW = new Vector();
		for (int i = 0; i < w.getVertexCount(); i++) {
			pW.set(w.getPoint(i));
			
			double d = pW.distanceTo(center);
			if (d > c.getRay())
				continue;
			
			CpW.set(pW, center);
			pC.set(c.getExtremePoint(CpW.getInverseVector()));
			
			double overlap = pC.distanceTo(pW);
			if (w.contains(pC) && overlap < contact.penetration) {
				contact.penetration = overlap;
				contact.position = pC; // TODO find correct position
				contact.normal = new Vector(pW, pC);
				contact.vToV = true;
				contact.vertex = pC.copy();
				contact.vertex2 = pW.copy();
				contact.valid = true;
			}
		}
		
		if (contact.valid) {
			contact.objA = c;
			contact.objB = w;
			contacts.add(contact);
		}
		
	}
	
	/**
	 * Test two circles for a collision
	 * @param c1 First circle
	 * @param c2 Second circle
	 */
	public static void check (WCircle c1, WCircle c2) {
		
		Vector CC = new Vector(c1.getPosition(), c2.getPosition());
		double distance = CC.getLength();
		
		if (distance > c1.getRay() + c2.getRay()) {
			return;
		}
			
		Contact contact = new Contact();
		
		Vector penetration = CC.getNormalizedVector().multiply(c1.getRay());
		penetration.sub(CC.getNormalizedVector().multiply(distance - c2.getRay()));
		Point pPos = 
			new Point(c1.getPosition()).add(
					CC.getNormalizedVector().multiply(
							distance - c2.getRay()))
							;
		
		// Vector CC
		Vector v1 = c1.getSpeed();
		Vector v2 = c2.getSpeed();
		// Relative speed
		Vector v = v1.copy().sub(v2);
		
		double rr = c1.getRay() + c2.getRay();
		double dVC2 = c2.getPosition().distanceTo(new Line(c1.getPosition(), v));
		double d1L = Math.sqrt(rr*rr - dVC2*dVC2);
		
		Vector delta;
		double f;
		if (v.isZero()) {
			delta = new Vector();
			f = 0;
		} else {
			delta = v.getNormalizedVector().multiply(d1L);
			delta.sub(CC.getProjection(v));
			f = delta.getLength() / v.getLength();
		}
		
		Vector delta1 = v1.copy().multiply(f);
		Vector delta2 = v2.copy().multiply(f);
		
		Point pPos1 = new Point(c1.getPosition());
		
		pPos1.sub(delta1);
		
		Point pPos2 = new Point(c2.getPosition());
		pPos2.sub(delta2);
		
		Point collisionPoint = pPos1.copy()
			.add(
				CC.getNormalizedVector()
					.multiply(
							c1.getRay()
				)
			)
		;
			
		contact.position = collisionPoint;
		contact.penetration = c1.getRay() + c2.getRay() - distance;
		contact.normal = CC;
		contact.objA = (c1);
		contact.objB = (c2);
		contact.vToV = true;
		contact.vertex = c1.getExtremePoint(CC);
		contact.vertex2 = c2.getExtremePoint(CC.getInverseVector());
		contact.valid = true;

		contacts.add(contact);
	}
	
	/**
	 * Test two polygons for a collision
	 * @param wp1 First polygon
	 * @param wp2 Second polygon
	 */
	public void check2 (WPolygon wp1, WPolygon wp2) {
		
		int index = 0;
		boolean onWp1 = true;
		double overlap = 1000000;
		
		for (int i = 0; i < wp1.getVertexCount(); i++) {
			
			Vector axis = wp1.getNormal(i);
			Line ground = new Line(axis);
			
			Point pA1 = ground.closestPointTo(wp1.getExtremePoint(axis));
			Point pA2 = ground.closestPointTo(wp1.getExtremePoint(axis.getInverseVector()));
			
			Point pB1 = ground.closestPointTo(wp2.getExtremePoint(axis));
			Point pB2 = ground.closestPointTo(wp2.getExtremePoint(axis.getInverseVector()));
			
			double[] AA = V2D.toDouble(pA1, pA2);
			double[] BB = V2D.toDouble(pB1, pB2);
			double[] A1B2 = V2D.toDouble(pA1, pB2);
			double[] B1A2 = V2D.toDouble(pB1, pA2);
			
			double dAA = V2D.getLength(AA);
			double dBB = V2D.getLength(BB);
			double dA1B2 = V2D.getLength(A1B2);
			double dB1A2 = V2D.getLength(B1A2);
			
			if (dA1B2 > dB1A2 && dAA + dB1A2 + dBB > dA1B2) {
				// Then contact B1 A2
				//F.s("collision");
			} else if (dB1A2 > dA1B2 && dAA + dA1B2 + dBB > dB1A2) {
				// Then contact A1 B2
				//F.s("collision");
			} else {
				// Then seperated
				F.s("Separated");
				return;
			}
			
			
		}
		
		// So we found no separating axis
		F.s("collision");
		
	}
	

	/**
	 * Test two polygons for a collision
	 * @param w1 First polygon
	 * @param w2 Second polygon
	 */
	public void check (WPolygon w1, WPolygon w2) {
		
		Contact[] contacts = {new Contact(), new Contact()};
		
		int best = 0;
		int other = 1;
		
		contacts[0].penetration = 100000;
		contacts[1].penetration = 100000;
		
		//Vector WW = new Vector(w1.getPosition(), w2.getPosition());
		
		// for each edge of body 1, check closest vertex of body 2
		for (int n = 0; n < w1.getVertexCount(); n++)
		{
			Vector axis = w1.getNormal(n);
			
			if (axis.equals(Vector.NULL)) { continue; }
			
			int index = 0;
			double val = w2.getPoint(0).distanceTo(w1.getEdge(n));
			for (int i = 0; i < w2.getVertexCount(); i++) {
				double tempval = w2.getPoint(i).distanceTo(w1.getEdge(n));
				if (tempval < val) {
					val = tempval;
					index = i;
				}
			}
			Point vertex2 = w2.getPoint(index);
			if (w2.getExtremePoint(axis.getInverseVector()).distanceTo(w1.getEdge(n)) < val) {
				vertex2 = w2.getExtremePoint(axis.getInverseVector());
			}
			
			if (!w1.contains(vertex2)) {
				continue;
			}
			
			double overlap = vertex2.distanceTo(w1.getEdge(n));
			
			int here;
			
			if (overlap < contacts[best].penetration )
			{
				if (vertex2.equals(contacts[best].vertex)) {
					here = best;
				} else {
					here = other;
					other = best;
					best = here;
				}
				
			} else if (overlap < contacts[other].penetration) {
				
				if (vertex2.equals(contacts[best].vertex)) {
					continue;
				} else {
					here = other;
				}
			} else {
				continue;
			}
			
			contacts[here].penetration = overlap;
			contacts[here].objA = w1;
			contacts[here].objB = w2;
			contacts[here].normal = axis;
			contacts[here].position = vertex2; // TODO
			contacts[here].edge = w1.getEdge(n);
			contacts[here].vertex = vertex2;
			contacts[here].valid = true;
		}
		
		
		for (int n = 0; n < w2.getVertexCount(); n++)
		{
			Vector axis = w2.getNormal(n);
			
			if (axis.equals(Vector.NULL)) { continue; }
			
			int index = 0;
			double val = w1.getPoint(0).distanceTo(w2.getEdge(n));
			for (int i = 0; i < w1.getVertexCount(); i++) {
				double tempval = w1.getPoint(i).distanceTo(w2.getEdge(n));
				if (tempval < val) {
					val = tempval;
					index = i;
				}
			}
			Point vertex1 = w1.getPoint(index);
			if (w1.getExtremePoint(axis.getInverseVector()).distanceTo(w2.getEdge(n)) < val) {
				vertex1 = w1.getExtremePoint(axis.getInverseVector());
			}
			
			if (!w2.contains(vertex1)) {
				continue;
			}
			
			double overlap = vertex1.distanceTo(w2.getEdge(n));
			
			int here;
			
			if (overlap < contacts[best].penetration )
			{
				if (vertex1.equals(contacts[best].vertex)) {
					here = best;
				} else {
					here = other;
					other = best;
					best = here;
				}
				
			} else if (overlap < contacts[other].penetration) {
				
				if (vertex1.equals(contacts[best].vertex)) {
					continue;
				} else {
					here = other;
				}
			} else {
				continue;
			}
			
			contacts[here].penetration = overlap;
			contacts[here].objA = w2;
			contacts[here].objB = w1;
			contacts[here].normal = axis;
			contacts[here].position = vertex1; // TODO
			contacts[here].edge = w2.getEdge(n);
			contacts[here].vertex = vertex1;
			contacts[here].valid = true;
		}
		
		
		if (contacts[0].valid ) 
			NarrowPhase.contacts.add(contacts[0]); 
		if (contacts[1].valid) 
			NarrowPhase.contacts.add(contacts[1]); 
	}
	
}
