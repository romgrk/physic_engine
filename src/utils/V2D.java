package utils;

/**
 * V2D provides methods to deal with vectors and points in format of
 * a double[2]. As all methods are static and the operations are on 
 * raw datatypes, the operations are much faster, which is essential
 * to perform a real-time simulation.
 * @see Vector
 * @see Point
 * @author Romain
 *
 */
public abstract class V2D {

	public static Vector toVector (double[] v) {
		return new Vector(v[0], v[1]);
	}
	
	public static double[] toDouble (Point a, Point b) {
		return new double[]{b.getX() - a.getX(), b.getY() - a.getY()};
	}
	
	public static double[] toDouble (Vector v) {
		return new double[]{v.getX(), v.getY()};
	}
	
	public static void multiply (double[] v, double m) {
		v[0] *= m;
		v[1] *= m;
	}
	
	public static double[] getProjection (double[] a, double[] b) {
		if (b[0] == 0 && b[1] == 0)
			return new double[]{0, 0};
		
		double factor = (a[0]*b[0] + a[1]*b[1]) / (b[0]*b[0] + b[1]*b[1]);
		double[] result = new double[2];
		result[0] = factor * b[0];
		result[1] = factor * b[1];
		return result;
	}
	
	public static double[] getNormalizedVector(double[] v) {
		double len = Math.sqrt(v[0]*v[0] + v[1]*v[1]);
		len = (len == 0)? 1 : len;
		return new double[]{v[0]/len, v[1]/len};
	}
	
	public static void normalize(double[] v) {
		double len = Math.sqrt(v[0]*v[0] + v[1]*v[1]);
		v[0] = v[0]/len;
		v[1] = v[1]/len;
	}
	
	public static double getLength (double[] v) {
		return Math.sqrt(v[0]*v[0] + v[1]*v[1]);
	}
	
	public static void clamp (double[] v, double min, double max) {
		v[0] = (v[0] <= max) ? v[0] : max;
		v[0] = (v[0] >= min) ? v[0] : min;
		v[1] = (v[0] <= max) ? v[1] : max;
		v[1] = (v[0] >= min) ? v[1] : min;
	}
	
	public static void clampMinimum (double[] v, double min) {
		v[0] = (v[0] < min && v[0] > -min) ? 0 : v[0];
		v[1] = (v[1] < min && v[1] > -min) ? 0 : v[1];
	}
	
	public static double getCrossProduct (double x1, double y1, double x2, double y2) {
		return x1*y2 - x2*y1;
	}
	
	public static double getCrossProduct (double[] a, double[] b) {
		return a[0]*b[1] - a[1]*b[0];
	}
	
	public static double getDotProduct(double[] a, double[] b) {
		return a[0]*b[0] + a[1]*b[1];
	}
}
