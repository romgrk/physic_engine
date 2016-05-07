package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.*;

import constants.Settings;

import objects.WObject;
import objects.World;
import utils.Point;

/**
 * The class Scene is the canvas that hold the World.
 * It also calls every class that need to paint on the World on repaint().
 * @see Drawable
 * @author Romain
 *
 */
public class Scene extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5934728852804619111L;

	private Color bgColor = Color.WHITE;
	
	private InformationPanel infoPane;
	
	private boolean grid = true;
	private int gridSpacing = 50;
	
	private World world;
	
	private ArrayList<Drawable> painters = new ArrayList<Drawable>();
	private ArrayList<UpdateEventListener> updates = new ArrayList<UpdateEventListener>();
	
	/**
	 * Constructor, takes the World object and start it.
	 * @param w The world that will be displayed on the Scene
	 */
	public Scene (World w) {
		WObject.scene = this;
		
		world = w;
		world.setScene(this);
		world.start();
		
		setFocusable(true);
	}
	
	/**
	 * Redefinition of method paint. Calls all objects implementing Drawable and pass
	 * them a WDrawer.
	 * @see WDrawer
	 */
	public void paint (Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.addRenderingHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON ));
		
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		WDrawer drw = new WDrawer(g, getWidth(), getHeight());
		
		if (grid) {
			drw.setColor(Settings.GRID_COLOR);
			int temp;
			temp = getWidth() / gridSpacing;
			for (int i = 0; i <= temp; i++) {
				drw.drawLine(i*gridSpacing, 0, i*gridSpacing, getHeight());
			}
			
			temp = getHeight() / gridSpacing;
			for (int i = 0; i <= temp; i++) {
				drw.drawLine(0, i*gridSpacing, getWidth(), i*gridSpacing);
			}
		}
		
		world.paint(drw);
		
		for (int i = 0; i < painters.size(); i++) {
			painters.get(i).paint(drw);
		}
	}
	
	/**
	 * Refresh the scene.
	 */
	public void update () {
		
		for (int i = 0; i < updates.size(); i++) {
			UpdateEvent ue = new UpdateEvent();
			ue.mouse = new Point(getMousePosition());
			ue.time = world.getMs();
			updates.get(i).physicUpdate(ue);
		}
		repaint();
		if(infoPane !=null)infoPane.repaint();
	}
	
	/**
	 * Adds an object that will be called on each repaint().
	 * @param d The object that will draw on the scene.
	 */
	public void addDrawable (Drawable d) {
		painters.add(d);
	}
	
	/**
	 * Removes an object from the list of objects that draw on the scene.
	 * @param d The object to remove
	 */
	public void removeDrawable (Drawable d) {
		painters.remove(d);
	}
	
	public void addUpdateEventListener (UpdateEventListener d) {
		updates.add(d);
	}
	
	public void removeUpdateEventListener (UpdateEventListener d) {
		updates.remove(d);
	}
	
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	public void toggleGrid(){
		if(grid){grid = false;}else{grid = true;}
		repaint();
	}
	public void setInfoPane(InformationPanel informations){
		this.infoPane = informations;
	}
}
