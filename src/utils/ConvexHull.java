package utils;
import java.util.ArrayList;


/**
 * ConvexHull provides a method to have the hull of a cloud of points.
 * @author Romain
 *
 */
public class ConvexHull {
	
	/**
	 * Check the points in parameter to retrieve the convex hull.
	 * For a list under 3 points, it only returns the same list.
	 * @param points The cloud of points
	 * @return The list of points forming the convex hull
	 */
	public static ArrayList<Point> quickHull(ArrayList<Point> points) {
		ArrayList<Point> convexHull = new ArrayList<Point>();
		if (points.size() < 3) return points;
		// find extremals
		int minPoint = -1, maxPoint = -1;
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getX() < minX) {
				minX = (int) points.get(i).getX();
				minPoint = i;
				} 
				if (points.get(i).getX() > maxX) {
				maxX = (int) points.get(i).getX();
				maxPoint = i;       
			}
		}
		Point A = points.get(minPoint);
		Point B = points.get(maxPoint);
		convexHull.add(A);
		convexHull.add(B);
		points.remove(A);
		points.remove(B);
		
		ArrayList<Point> leftSet = new ArrayList<Point>();
		ArrayList<Point> rightSet = new ArrayList<Point>();
		
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (pointLocation(A,B,p) == -1)
				leftSet.add(p);
			else
				rightSet.add(p);
		}
		hullSet(A,B,rightSet,convexHull);
		hullSet(B,A,leftSet,convexHull);
		
		return convexHull;
	}
	
	public static void hullSet(Point A, Point B, ArrayList<Point> set, ArrayList<Point> hull) {
		int insertPosition = hull.indexOf(B);
		if (set.size() == 0) return;
		if (set.size() == 1) {
			Point p = set.get(0);
			set.remove(p);
			hull.add(insertPosition,p);
			return;
		}
		int dist = Integer.MIN_VALUE;
		int furthestPoint = -1;
		for (int i = 0; i < set.size(); i++) {
			Point p = set.get(i);
			int distance  = distance(A,B,p);
			if (distance > dist) {
				dist = distance;
				furthestPoint = i;
			}
		}
		Point P = set.get(furthestPoint);
		set.remove(furthestPoint);
		hull.add(insertPosition,P);
		
		// Determine who's to the left of AP
		ArrayList<Point> leftSetAP = new ArrayList<Point>();
		for (int i = 0; i < set.size(); i++) {
			Point M = set.get(i);
			if (pointLocation(A,P,M)==1) {
				//set.remove(M);
				leftSetAP.add(M);
			}
		}
		
		// Determine who's to the left of PB
		ArrayList<Point> leftSetPB = new ArrayList<Point>();
		for (int i = 0; i < set.size(); i++) {
			Point M = set.get(i);
			if (pointLocation(P,B,M)==1) {
				//set.remove(M);
				leftSetPB.add(M);
			}
		}
		hullSet(A,P,leftSetAP,hull);
		hullSet(P,B,leftSetPB,hull);
	}
	
	public static int pointLocation(Point A, Point B, Point P) {
		int cp1 = (int) ((B.getX()-A.getX())*(P.getY()-A.getY()) - (B.getY()-A.getY())*(P.getX()-A.getX()));
		return (cp1>0)?1:-1;
	}
	
	public static int distance(Point A, Point B, Point C) {
		int ABx = (int) (B.getX()-A.getX());
		int ABy = (int) (B.getY()-A.getY());
		int num = (int) (ABx*(A.getY()-C.getY())-ABy*(A.getX()-C.getX()));
		if (num < 0) num = -num;
		return num;
	}
}