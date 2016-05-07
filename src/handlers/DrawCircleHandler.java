package handlers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import objects.WCircle;

import utils.Point;
import constants.Settings;

import GUI.Drawable;
import GUI.Scene;
import GUI.WDrawer;

/**
 * 
 * This class manage the creation of new circles
 * @author Yanick Sévigny
 * 
 *
 */

public class DrawCircleHandler
extends Handler
implements MouseListener, MouseMotionListener, Drawable{
	
	private Point p1, p2;
	private boolean dragging = false;
	private int xDragged, yDragged;

	/**
	 * Constructor takes in parameter the scene to manage
	 * @param s Scene
	 */
	public DrawCircleHandler(Scene s) {
		super(s);
		
		s.addMouseListener(this);
		s.addMouseMotionListener(this);
		s.addDrawable(this);
	}
	
	/**
	 * The method paint defines what the scene is going to show when a creation of a circle will be occuring
	 * It takes in the WDrawer which will be shown in the scene
	 * @param w WDrawer
	 */
	public void paint (WDrawer w){
		if (dragging) {
			w.setColor(Settings.GRID_COLOR);
			w.fillCircle(p1, 2);
			w.drawLine(p1, new Point(xDragged, yDragged));
			w.drawCircle(p1, p1.distanceTo(new Point(xDragged, yDragged)));
		}
	}
	
	//Unused mouseListener method
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	
	//Redefines the method mouseDragged when this class is active
	public void mouseDragged(MouseEvent e){
		if(!active)
			return;
		dragging = true;
		xDragged = e.getX();
		yDragged = scene.getHeight() - e.getY();
		scene.repaint();
	}
	
	//Redefines the method mousePressed when this class is active
	public void mousePressed(MouseEvent e) {
		if(!active)
			return;
		p1 = new Point(e.getX(), scene.getHeight() - e.getY());
	}

	//Redefines the method mouseReleased when this class is active
	public void mouseReleased(MouseEvent e) {
		if(!active)
			return;
		dragging = false;
		p2 = new Point(e.getX(), scene.getHeight() - e.getY());
		if(p1.distanceTo(p2) > 10){
			WCircle circle = new WCircle(p1.distanceTo(p2));
			circle.setPosition(p1);
			circle.setColor(Settings.DEFAULT_COLOR);
			scene.getWorld().getObjects().add(circle);
			scene.repaint();
		}
	}
}
