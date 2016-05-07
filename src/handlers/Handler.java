package handlers;

import GUI.Scene;

/**
 * This class is the base of any class that connects the user
 * and the World.
 * @author Romain
 *
 */
public abstract class Handler {
	protected boolean active = false;
	protected Scene scene;
	
	public Handler (Scene s) {
		scene = s;
	}
	
	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public double getInverseY (double y) {
		return scene.getHeight() - y;
	}
	public int getInverseY (int y) {
		return scene.getHeight() - y;
	}
}
