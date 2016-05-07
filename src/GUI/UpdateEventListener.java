package GUI;

/**
 * This interface let any class know when there is a change in the World.
 * @author Romain
 *
 */
public interface UpdateEventListener {
	public abstract void physicUpdate (UpdateEvent e);
}
