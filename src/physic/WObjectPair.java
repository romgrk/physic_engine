package physic;

import objects.WObject;

/** 
 * This class is only used to store a pair of possibly colliding objects.
 * @author Romain
 *
 */
public class WObjectPair {
	public WObjectPair (WObject o1, WObject o2) {
		object1 = o1;
		object2 = o2;
	}
	public WObject object1;
	public WObject object2;
}
