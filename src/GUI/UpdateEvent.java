package GUI;

import java.awt.AWTEvent;
import java.awt.Event;

import utils.Point;

/**
 * This class represent an UpdateEvent, that happens everytime
 * there is a physical change on the World.
 * @author Romain
 *
 */
public class UpdateEvent extends AWTEvent {
	private static final long serialVersionUID = 1L;
	
	public double time = 0;
	public Point mouse;
	
	public UpdateEvent() {
		super(new Event(0, 0, 0));
		
	}

}
