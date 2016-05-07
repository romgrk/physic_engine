package utils;

/**
 * This class provides methods with short names to quickly use them 
 * while debugging.
 * @author Romain
 *
 */
public class F {
	
	/** 
	 * Returns a random number within the range specified.
	 * @param min The minimum number
	 * @param max The maximum number
	 * @return A random number between min and max
	 */
	public static double r (double min, double max) {
		double r = Math.random();
		double range = max - min;
		r = r*range;
		r += min;
		return r;
	}
	
	/**
	 * Display a string on the console
	 * @param msg The string to display
	 */
	public static void s (String msg) {
		System.out.println(msg);
	}
	
	/**
	 * Display an integer on the console
	 * @param msg The integer to display
	 */
	public static void s (int msg) {
		System.out.println(msg);
	}
	
	/**
	 * Display a double on the console
	 * @param msg The double to display
	 */
	public static void s (double msg) {
		System.out.println(msg);
	}
}
