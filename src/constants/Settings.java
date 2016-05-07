package constants;

import java.awt.Color;
import java.awt.Font;

import GUI.Application;

import objects.WObject;

import utils.Vector;

/**
 * This class contains all the constants needed by other classes.
 * @author Romain
 *
 */
public class Settings {
	
	public static final int DEFAULT_TIMESTEP		=	10;
	
	public static final int MAX_OBJECTS				=	1000;
	public static final double AREA_DENSITY			=	1.0;
	public static final double DEFAULT_RESTITUTION	=	0.7;
	public static final double DEFAULT_FRICTION		=	0.6;
	
	public static final Vector DEFAULT_GRAVITY		=	new Vector(0, -80);
	
	public static Color DEFAULT_COLOR				= new Color(17, 220, 17, 150);
	public static final Color MOUSE_OVER			= new Color(220, 220, 17, 150);
	public static final Color SELECTED				= new Color(220, 17, 17, 150);
	public static final Color GRID_COLOR			= Color.LIGHT_GRAY;
	
	public static final String DATA_DIRECTORY 	= "Worlds";
	
	public static final int OPTIONPANEWIDTH = 100;

	public static final Font DEFAULT_FONT = new Font("Comic Sans ms", Font.PLAIN, 11);
	
	public static int APPLICATIONX = 20;
	public static int APPLICATIONY = 20;
	
	public static Application app;
	
	// Global variables
	public static WObject selectedObject = null;
}
