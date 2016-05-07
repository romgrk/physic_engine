package physic;

import java.util.ArrayList;

import objects.WObject;

/**
 * BroadPhase perform a quick test to find the most likely pairs of
 * object that could be colliding.
 * @author Romain
 *
 */
public class BroadPhase {
	
	/**
	 * Perform a test on the circular hull of each object.
	 * @param objects The list of objects to test
	 * @return The list of possibly colliding objects
	 */
	public static ArrayList<WObjectPair> findPairs (ArrayList<WObject> objects) {
		
		ArrayList<WObjectPair> pairs = new ArrayList<WObjectPair>();
		
		double[] invMass = WObject.m_invMass;
		
		for (int i = 0; i < objects.size(); i++) {
			WObject objA = objects.get(i);
			for (int j = i+1; j < objects.size(); j++) {
				WObject objB = objects.get(j);
				double sqD = objA.getPosition().squareDistanceTo(objB.getPosition());
				double r = objA.getRay() + objB.getRay();
				if (sqD < r*r && !(invMass[objA.index] == 0 && invMass[objB.index] == 0)) {
					pairs.add(new WObjectPair(objA, objB));
				}
			}
		}
		return pairs;
	}
}
